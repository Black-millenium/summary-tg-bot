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

@Slf4j
@RequiredArgsConstructor
@Service
public class ChatGptService {

    private final GptProperties gptProperties;
    private final RestTemplate restTemplate;

    public String requestGptSummarization(List<ChatMessage> messages) {
        final List<GptMessage> messagesConverted = new ArrayList<>();

        GptMessage systemMessage = new GptMessage(GptRole.SYSTEM, gptProperties.getSystemInstruction());
        messagesConverted.add(systemMessage);

        messages.stream()
                .sorted(Comparator.comparing(ChatMessage::getCreatedAt))
                .map(msg -> new GptMessage(GptRole.USER, msg.getSenderName() + ": " + msg.getMessage()))
                .forEach(messagesConverted::add);

        GptMessage assistantMessage = new GptMessage(GptRole.ASSISTANT, gptProperties.getAssistantInstruction());
        messagesConverted.add(assistantMessage);

        ChatGptRequest chatGptRequest = new ChatGptRequest(
                gptProperties.getModel(),
                gptProperties.getMaxTokens(),
                gptProperties.getTemperature(),
                messagesConverted);

        ChatGptResponse body = restTemplate.postForEntity(
                        gptProperties.getUrl(), chatGptRequest, ChatGptResponse.class)
                .getBody();

        log.info("{}", body);

        if (body != null && body.choices() != null && !body.choices().isEmpty()) {
            return body.choices().get(0).message().content();
        } else {
            throw new RuntimeException("Error ChatGPT response");
        }
    }
}
