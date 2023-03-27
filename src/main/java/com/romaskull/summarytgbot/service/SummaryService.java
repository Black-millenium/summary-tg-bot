package com.romaskull.summarytgbot.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.romaskull.summarytgbot.entity.ChatMessage;
import com.romaskull.summarytgbot.properties.GptProperties;
import com.romaskull.summarytgbot.properties.TelegarmProperties;
import com.romaskull.summarytgbot.repository.TelegramChatMessageRepository;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.telegram.telegrambots.bots.TelegramWebhookBot;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.MessageEntity;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static com.romaskull.summarytgbot.util.SummaryUtil.extractMessageText;
import static com.romaskull.summarytgbot.util.SummaryUtil.getDisplayableName;
import static com.romaskull.summarytgbot.util.SummaryUtil.isBotCalling;
import static com.romaskull.summarytgbot.util.SummaryUtil.isGroupMessage;

@Slf4j
@Service
public class SummaryService extends TelegramWebhookBot {

    private final TelegarmProperties telegarmProperties;
    private final ChatGptService chatGptService;
    private final TelegramChatMessageRepository historyRepository;
    private final GptProperties gptProperties;
    private final ObjectMapper mapper;

    public SummaryService(TelegarmProperties telegarmProperties,
                          ChatGptService chatGptService,
                          TelegramChatMessageRepository historyRepository,
                          GptProperties gptProperties,
                          ObjectMapper mapper) {
        super(telegarmProperties.getToken());
        this.telegarmProperties = telegarmProperties;
        this.chatGptService = chatGptService;
        this.historyRepository = historyRepository;
        this.gptProperties = gptProperties;
        this.mapper = mapper;

        log.info("Summary service initialized");
    }

    @Override
    public String getBotUsername() {
        return telegarmProperties.getUsername();
    }

    @SneakyThrows
    @Override
    public BotApiMethod<?> onWebhookUpdateReceived(Update update) {
        final Message message = update.getMessage();
        List<MessageEntity> entities;

        if (message != null && StringUtils.hasLength(message.getText())) {
            Long chatId = message.getChatId();

            if (isGroupMessage(message) && (entities = message.getEntities()) != null) {
                for (MessageEntity entity : entities) {
                    if (isBotCalling(message, entity)) {
                        String messageText = extractMessageText(message);
                        try {
                            int messageCount = Integer.parseInt(messageText);

                            if (messageCount > gptProperties.getMaxHistory() || messageCount < 1) {
                                sendMessage(chatId, "Система не в состоянии понять введеное вами значение. " +
                                        "Диапазон: [1; " + gptProperties.getMaxHistory() + "]");
                                continue;
                            }

                            List<ChatMessage> msgs = new ArrayList<>(
                                    historyRepository.findByChatIdOrderByMessageIdDesc(chatId,
                                            PageRequest.of(0, gptProperties.getMaxHistory())));

                            String summaryResult = chatGptService.requestGptSummarization(msgs);

                            sendMessage(chatId, summaryResult);
                        } catch (NumberFormatException e) {
                            sendMessage(chatId, "Неопознанный параметр. " +
                                    "Введите только количество сообщений для краткого анализа.");
                        } catch (RuntimeException e) {
                            log.error("{}", e.getMessage(), e);
                            sendMessage(chatId, "Произошла внутренняя ошибка");
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
        return telegarmProperties.getUsername();
    }
}
