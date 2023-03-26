package com.romaskull.summarytgbot.service;

import com.romaskull.summarytgbot.entity.ChatMessage;
import com.romaskull.summarytgbot.properties.GptProperties;
import com.romaskull.summarytgbot.properties.TelegarmProperties;
import com.romaskull.summarytgbot.repository.TelegramChatMessageRepository;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.EntityType;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.MessageEntity;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Service
public class TelegramBotSummaryService extends TelegramLongPollingBot {

    private static final String SUMGPTBOT = "@sumgptbot";

    private final TelegarmProperties telegarmProperties;
    private final ChatGptService chatGptService;
    private final TelegramChatMessageRepository historyRepository;
    private final GptProperties gptProperties;

    public TelegramBotSummaryService(TelegarmProperties telegarmProperties,
                                     ChatGptService chatGptService,
                                     TelegramChatMessageRepository historyRepository,
                                     GptProperties gptProperties) {
        super(telegarmProperties.getToken());
        this.telegarmProperties = telegarmProperties;
        this.chatGptService = chatGptService;
        this.historyRepository = historyRepository;
        this.gptProperties = gptProperties;

        log.info("Summary service initialized");
    }

    @Override
    public String getBotUsername() {
        return telegarmProperties.getUsername();
    }

    @SneakyThrows
    @Override
    public void onUpdateReceived(Update update) {
        final Message message = update.getMessage();
        List<MessageEntity> entities;

        if (message != null && !StringUtils.isBlank(message.getText())) {
            Long chatId = message.getChatId();

            if (isGroupMessage(message) && (entities = message.getEntities()) != null) {
                for (MessageEntity entity : entities) {
                    if (isBotCalling(message, entity)) {
                        String messageText = extractMessageText(message);
                        try {
                            int messageCount = Integer.parseInt(messageText);

                            if (messageCount > gptProperties.getMaxHistory()) {
                                throw new RuntimeException("Значение максимального количества " +
                                        "записей больше допустимого");
                            }

                            List<ChatMessage> msgs = historyRepository.findLatestMessages(chatId, messageCount);
                            String summaryResult = chatGptService.requestGptSummarization(msgs);

                            sendMessage(chatId, summaryResult);
                        } catch (NumberFormatException e) {
                            sendMessage(chatId, "Неопознанный параметр. " +
                                    "Введите только количество сообщений для краткого анализа.");
                        } catch (RuntimeException e) {
                            log.error("{}", e.getMessage(), e);
                            sendMessage(chatId, e.getMessage());
                        }
                    }
                }
            } else {
                final ChatMessage chatMessage = new ChatMessage();

                chatMessage.setChatId(chatId);
                chatMessage.setMessage(message.getText());
                chatMessage.setSenderName(message.getFrom().getUserName());
                chatMessage.setSenderId(message.getFrom().getId());
                chatMessage.setCreatedAt(LocalDateTime.now());

                historyRepository.save(chatMessage);
            }
        }
    }

    private boolean isGroupMessage(Message message) {
        return message.isGroupMessage() || message.isSuperGroupMessage();
    }

    private String extractMessageText(Message message) {
        return message.getText().replace(SUMGPTBOT, "").strip();
    }

    private boolean isBotCalling(Message message, MessageEntity entity) {
        return EntityType.MENTION.equals(entity.getType())
                && SUMGPTBOT.equals(entity.getText())
                && message.getText().startsWith(SUMGPTBOT);
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
}
