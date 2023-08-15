package com.romaskull.summarytgbot.bot;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.romaskull.summarytgbot.dto.GptRole;
import com.romaskull.summarytgbot.entity.Dialogue;
import com.romaskull.summarytgbot.entity.DialogueCounters;
import com.romaskull.summarytgbot.properties.DialogueProperties;
import com.romaskull.summarytgbot.properties.GptProperties;
import com.romaskull.summarytgbot.repository.DialogueCountersRepository;
import com.romaskull.summarytgbot.repository.DialogueRepository;
import com.romaskull.summarytgbot.service.ChatGptService;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.bots.TelegramWebhookBot;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import static com.romaskull.summarytgbot.util.SummaryUtil.getDisplayableName;

@Slf4j
@Service
public class DialogueBot extends TelegramWebhookBot {

    private static final String ASKER = "@gpt_asker_bot";

    private final ChatGptService chatGptService;
    private final DialogueProperties dialogueBotProperties;
    private final ObjectMapper mapper;
    private final DialogueRepository dialogueRepository;
    private final DialogueCountersRepository dialogueCountersRepository;
    private final GptProperties gptProperties;

    public DialogueBot(ChatGptService chatGptService,
                       DialogueProperties dialogueBotProperties,
                       ObjectMapper mapper,
                       DialogueRepository dialogueRepository,
                       DialogueCountersRepository dialogueCountersRepository,
                       GptProperties gptProperties) {
        super(dialogueBotProperties.getToken());

        this.chatGptService = chatGptService;
        this.dialogueBotProperties = dialogueBotProperties;
        this.mapper = mapper;
        this.dialogueRepository = dialogueRepository;
        this.dialogueCountersRepository = dialogueCountersRepository;
        this.gptProperties = gptProperties;
    }

    @SneakyThrows
    @Override
    public BotApiMethod<?> onWebhookUpdateReceived(Update update) {
        final Message message = update.getMessage();

        log.info(mapper.writeValueAsString(update));

        Long chatId = message.getChatId();

        if (message.getText() != null) {
            final DialogueCounters counter = dialogueCountersRepository.findBySenderId(message.getFrom().getId())
                    .orElseGet(() -> dialogueCountersRepository.save(new DialogueCounters(
                            message.getFrom().getId(), getDisplayableName(message.getFrom()), 0L)));

            log.info("{}", counter);

            if (message.getText().startsWith("/start")) {
                return new SendMessage(String.valueOf(chatId), dialogueBotProperties.getAcceptMessage());
            } else if (message.getText().startsWith("/new")) {
                counter.setDialogueNumber(counter.getDialogueNumber() + 1);
                dialogueCountersRepository.save(counter);
                return new SendMessage(String.valueOf(chatId), "Хорошо, давай поговорим о чем-нибудь другом");
            } else {
                saveMessage(chatId, message, counter);

                List<Dialogue> dialogue = dialogueRepository.findAllByChatIdAndDialogueNumber(
                        chatId,
                        counter.getDialogueNumber(),
                        PageRequest.of(0, gptProperties.getMaxBatchSize()));

                send(dialogue);
            }
        }

        return null;
    }

    private void send(List<Dialogue> dialogueHistory) {
        CompletableFuture<String> future = chatGptService.gptDialogue(dialogueHistory);
        future.thenAccept(s -> {
            try {
                Dialogue message = dialogueHistory.get(0);
                saveResponse(message.getChatId(), s, message.getDialogueNumber());
                sendMessage(message.getChatId(), s);
            } catch (Exception e) {
                log.error("{}", e.getMessage(), e);
                sendMessage(dialogueHistory.get(0).getChatId(),
                        "Извини, я не могу сейчас ответить. Плохо себя чувствую.");
            }
        });
    }

    @Override
    public String getBotPath() {
        return dialogueBotProperties.getBotPath();
    }

    @Override
    public String getBotUsername() {
        return dialogueBotProperties.getUsername();
    }

    private void sendMessage(Long chatId, String text) {
        SendMessage message = new SendMessage();
        message.setChatId(chatId);
        message.setText(text);
        try {
            execute(message);
        } catch (TelegramApiException e) {
            log.error("{}", e.getMessage(), e);
        }
    }

    private void saveMessage(Long chatId,
                             Message message,
                             DialogueCounters counter) {
        final Dialogue dialogue = new Dialogue();

        dialogue.setChatId(chatId);
        dialogue.setMessageId(Long.valueOf(message.getMessageId()));
        dialogue.setMessage(message.getText());
        dialogue.setSenderName(getDisplayableName(message.getFrom()));
        dialogue.setSenderId(message.getFrom().getId());
        dialogue.setCreatedAt(LocalDateTime.now());
        dialogue.setDialogueNumber(counter.getDialogueNumber());
        dialogue.setGptRole(GptRole.USER);

        dialogueRepository.save(dialogue);
    }

    private void saveResponse(Long chatId, String message, Long dialogueNumber) {
        final Dialogue dialogue = new Dialogue();

        dialogue.setChatId(chatId);
        dialogue.setMessage(message);
        dialogue.setCreatedAt(LocalDateTime.now());
        dialogue.setDialogueNumber(dialogueNumber);
        dialogue.setGptRole(GptRole.ASSISTANT);

        dialogueRepository.save(dialogue);
    }
}
