package com.romaskull.summarytgbot.service;

import com.romaskull.summarytgbot.dto.ChatGptRequest;
import com.romaskull.summarytgbot.dto.ChatGptResponse;
import com.romaskull.summarytgbot.dto.GptMessage;
import com.romaskull.summarytgbot.dto.GptRole;
import com.romaskull.summarytgbot.entity.ChatMessage;
import com.romaskull.summarytgbot.properties.GptProperties;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.IntStream;

@Slf4j
@RequiredArgsConstructor
@Service
public class ChatGptService {

    private final GptProperties gptProperties;
    private final RestTemplate restTemplate;

    public String requestGptSummarization(List<ChatMessage> messages) {
        final StringBuilder sb = new StringBuilder();

        messages.sort(Comparator.comparing(ChatMessage::getCreatedAt));
        int batchSize = gptProperties.getMaxBatchSize();

        IntStream.iterate(0, i -> i < messages.size(), i -> i + batchSize)
                .mapToObj(i -> messages.subList(i, Math.min(i + batchSize, messages.size())))
                .forEachOrdered(chatMessages -> {
                    final List<GptMessage> messagesConverted = new ArrayList<>();
                    createGptInstruction(messagesConverted, GptRole.SYSTEM, gptProperties.getSystemInstruction());

                    chatMessages.forEach(msg ->
                            createGptInstruction(messagesConverted, GptRole.USER,
                                    msg.getSenderName() + ": " + msg.getMessage()));

                    createGptInstruction(messagesConverted, GptRole.ASSISTANT, gptProperties.getAssistantInstruction());

                    ChatGptRequest chatGptRequest = createChatGptRequest(messagesConverted);
                    ChatGptResponse body = restTemplate.postForEntity(
                                    gptProperties.getUrl(), chatGptRequest, ChatGptResponse.class)
                            .getBody();

                    log.info("{}", body);
                    sb.append(body != null && body.choices() != null && !body.choices().isEmpty()
                            ? body.choices().get(0).message().content()
                            : "Ошибка генерации саммари по блоку сообщений");
                    sb.append("\n\n");
                });

        return sb.toString();
    }

    private ChatGptRequest createChatGptRequest(List<GptMessage> messagesConverted) {
        return new ChatGptRequest(
                gptProperties.getModel(),
                gptProperties.getMaxTokens(),
                gptProperties.getTemperature(),
                messagesConverted);
    }

    private void createGptInstruction(List<GptMessage> messagesConverted, GptRole system, String systemInstruction) {
        GptMessage systemMessage = new GptMessage(system, systemInstruction);
        messagesConverted.add(systemMessage);
    }
}
