package com.romaskull.summarytgbot.util;

import com.romaskull.summarytgbot.dto.ChatGptRequest;
import com.romaskull.summarytgbot.dto.GptMessage;
import com.romaskull.summarytgbot.dto.GptRole;
import com.romaskull.summarytgbot.properties.GptProperties;

import java.util.List;

public final class GptUtil {

    private GptUtil() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }

    public static ChatGptRequest createChatGptRequest(List<GptMessage> messages, GptProperties gptProperties) {
        return new ChatGptRequest(gptProperties.getModel(),
                gptProperties.getMaxTokens(),
                gptProperties.getTemperature(),
                messages);
    }

    public static void createGptInstruction(List<GptMessage> messages, GptRole role, String content) {
        GptMessage systemMessage = new GptMessage(role, content);
        messages.add(systemMessage);
    }
}
