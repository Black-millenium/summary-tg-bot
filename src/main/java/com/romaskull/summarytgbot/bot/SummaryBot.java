package com.romaskull.summarytgbot.bot;

import com.romaskull.summarytgbot.entity.ChatMessage;
import com.romaskull.summarytgbot.properties.GptProperties;
import com.romaskull.summarytgbot.properties.SummaryBotProperties;
import com.romaskull.summarytgbot.repository.TelegramChatMessageRepository;
import com.romaskull.summarytgbot.service.ChatGptService;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.util.StopWatch;
import org.springframework.util.StringUtils;
import org.telegram.telegrambots.bots.TelegramWebhookBot;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.MessageEntity;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import static com.romaskull.summarytgbot.util.SummaryUtil.extractMessageText;
import static com.romaskull.summarytgbot.util.SummaryUtil.getDisplayableName;
import static com.romaskull.summarytgbot.util.SummaryUtil.isBotCalling;
import static com.romaskull.summarytgbot.util.SummaryUtil.isGroupMessage;

@Slf4j
@Service
public class SummaryBot extends TelegramWebhookBot {

    private static final String SUMGPTBOT = "@sumgptbot";

    private final SummaryBotProperties summaryBotProperties;
    private final ChatGptService chatGptService;
    private final TelegramChatMessageRepository historyRepository;
    private final GptProperties gptProperties;

    public SummaryBot(SummaryBotProperties summaryBotProperties,
                      ChatGptService chatGptService,
                      TelegramChatMessageRepository historyRepository,
                      GptProperties gptProperties) {
        super(summaryBotProperties.getToken());
        this.summaryBotProperties = summaryBotProperties;
        this.chatGptService = chatGptService;
        this.historyRepository = historyRepository;
        this.gptProperties = gptProperties;

        log.info("Summary service initialized");
    }

    @Override
    public String getBotUsername() {
        return summaryBotProperties.getUsername();
    }

    @SneakyThrows
    @Override
    public BotApiMethod<?> onWebhookUpdateReceived(Update update) {
        final Message message = update.getMessage();
        List<MessageEntity> entities;

        if (StringUtils.hasLength(message.getText())) {
            Long chatId = message.getChatId();

            if (StringUtils.hasLength(extractMessageText(SUMGPTBOT, message))) {
                if (isGroupMessage(message) && (entities = message.getEntities()) != null) {
                    for (MessageEntity entity : entities) {
                        if (isBotCalling(SUMGPTBOT, message, entity)) {
                            String messageText = extractMessageText(SUMGPTBOT, message);
                            try {
                                int messageCount = Integer.parseInt(messageText);

                                if (messageCount > gptProperties.getMaxHistory() || messageCount < 1) {
                                    sendMessage(chatId, "Не понимаю чё такое. " +
                                            "Пойму только число в диапазоне: [1; " + gptProperties.getMaxHistory() + "]");
                                    continue;
                                }

                                StopWatch stopWatch = new StopWatch("mongo-select");
                                stopWatch.start();
                                List<ChatMessage> msgs = historyRepository.findByChatId(chatId,
                                        PageRequest.of(0, messageCount));
                                stopWatch.stop();

                                log.info("Time for select {} messages from Mongo: {} ms",
                                        messageCount, stopWatch.getLastTaskTimeMillis());

                                CompletableFuture<String> summaryResult = chatGptService.requestGptSummarization(msgs);
                                summaryResult.thenAccept(s -> {
                                    try {
                                        sendMessage(chatId, s);
                                    } catch (Exception e) {
                                        log.error("{}", e.getMessage(), e);
                                        sendMessage(chatId, "Что-то поломалось в процессе ожидания ответа");
                                    }
                                });

                                return new SendMessage(String.valueOf(update.getMessage().getChatId()),
                                        summaryBotProperties.getAcceptMessage());
                            } catch (NumberFormatException e) {
                                sendMessage(chatId, "Не понимаю чё такое. Пойму только число.");
                            } catch (RuntimeException e) {
                                log.error("{}", e.getMessage(), e);
                                sendMessage(chatId, "Что-то где-то поломалось");
                            }
                        }
                    }
                } else if (isGroupMessage(message)) {
                    final ChatMessage chatMessage = new ChatMessage();

                    chatMessage.setChatId(chatId);
                    chatMessage.setMessageId(Long.valueOf(message.getMessageId()));
                    chatMessage.setMessage(message.getText());
                    chatMessage.setSenderName(getDisplayableName(message.getFrom()));
                    chatMessage.setSenderId(message.getFrom().getId());
                    chatMessage.setCreatedAt(LocalDateTime.now());

                    historyRepository.save(chatMessage);
                }
            }
        }
        return null;
    }

    private void sendMessage(Long chatId, String text) {
        SendMessage message = new SendMessage();
        message.setChatId(chatId);
        message.setText(text);
        try {
            execute(message);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    @Override
    public String getBotPath() {
        return summaryBotProperties.getBotPath();
    }
}
