package com.romaskull.summarytgbot.service;

import com.romaskull.summarytgbot.dto.ChatGptRequest;
import com.romaskull.summarytgbot.dto.ChatGptResponse;
import com.romaskull.summarytgbot.dto.GptMessage;
import com.romaskull.summarytgbot.dto.GptRole;
import com.romaskull.summarytgbot.entity.ChatMessage;
import com.romaskull.summarytgbot.entity.Dialogue;
import com.romaskull.summarytgbot.properties.GptProperties;
import com.romaskull.summarytgbot.properties.SummaryBotProperties;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import static com.romaskull.summarytgbot.util.GptUtil.createChatGptRequest;
import static com.romaskull.summarytgbot.util.GptUtil.createGptInstruction;

@Slf4j
@RequiredArgsConstructor
@Service
public class ChatGptService {

    private final GptProperties gptProperties;
    private final SummaryBotProperties summaryBotProperties;
    private final RestTemplate restTemplate;

    @Async
    public CompletableFuture<String> gptDialogue(List<Dialogue> dialogue) {
        final CompletableFuture<String> future = new CompletableFuture<>();
        final List<GptMessage> dialogueHistory = new ArrayList<>();

        dialogue.sort(Comparator.comparing(Dialogue::getCreatedAt));

        createGptInstruction(dialogueHistory, GptRole.SYSTEM, summaryBotProperties.getSystemInstruction());

        dialogue.forEach(message -> createGptInstruction(dialogueHistory, message.getGptRole(), message.getMessage()));

        try {
            future.complete(getGptResponse(dialogueHistory));
        } catch (Exception e) {
            future.complete("Извини, я не могу сейчас ответить. Плохо себя чувствую.");
            log.error("{}", e.getMessage(), e);
        }

        return future;
    }

    @Async
    public CompletableFuture<String> requestGptSummarization(List<ChatMessage> messages) {
        final CompletableFuture<String> result = new CompletableFuture<>();
        final StringBuilder sb = new StringBuilder();

        messages.sort(Comparator.comparing(ChatMessage::getCreatedAt));
        int batchSize = gptProperties.getMaxBatchSize();

        for (int i = 0, counter = 1; i < messages.size(); i += batchSize, counter++) {
            List<ChatMessage> chatMessages = messages.subList(i, Math.min(i + batchSize, messages.size()));
            log.info("Total Messages: {}. Batch number: {}, batch size: {}",
                    messages.size(), counter, chatMessages.size());

            final List<GptMessage> messagesConverted = new ArrayList<>();
            createGptInstruction(messagesConverted, GptRole.SYSTEM, summaryBotProperties.getSystemInstruction());

            chatMessages.forEach(msg -> createGptInstruction(messagesConverted, GptRole.USER,
                    msg.getSenderName() + ": " + msg.getMessage()));

            createGptInstruction(messagesConverted, GptRole.USER, summaryBotProperties.getSummarizationInstruction());

            try {
                sb.append(getGptResponse(messagesConverted));
            } catch (Exception e) {
                sb.append("Ошибка генерации саммари по блоку сообщений");
                log.error(e.getMessage());
            }

            sb.append("\n\n");
        }

        result.complete(sb.toString());
        return result;
    }

    private String getGptResponse(List<GptMessage> messages) {
        ChatGptResponse body = executeGptRequest(messages);
        log.info("{}", body);

        if (body != null && body.choices() != null && !body.choices().isEmpty()) {
            return body.choices().get(0).message().content();
        } else {
            log.error("Response body: {}", body);
            return "Ошибка получения ответа от внешнего API";
        }
    }

    private ChatGptResponse executeGptRequest(List<GptMessage> messages) {
        ChatGptRequest request = createChatGptRequest(messages, gptProperties);
        return restTemplate.postForEntity(gptProperties.getUrl(), request, ChatGptResponse.class).getBody();
    }
}
