package com.romaskull.summarytgbot.util;

import com.romaskull.summarytgbot.dto.GptMessage;
import com.romaskull.summarytgbot.dto.GptRole;
import lombok.experimental.UtilityClass;

import java.util.List;

@UtilityClass
public class GptUtil {

    public static void createGptInstruction(List<GptMessage> messagesConverted, GptRole system, String systemInstruction) {
        GptMessage systemMessage = new GptMessage(system, systemInstruction);
        messagesConverted.add(systemMessage);
    }
}
