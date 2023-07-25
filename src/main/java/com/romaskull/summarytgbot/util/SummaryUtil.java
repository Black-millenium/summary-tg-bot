package com.romaskull.summarytgbot.util;

import lombok.experimental.UtilityClass;
import org.springframework.util.StringUtils;
import org.telegram.telegrambots.meta.api.objects.EntityType;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.MessageEntity;
import org.telegram.telegrambots.meta.api.objects.User;

import java.util.ArrayList;
import java.util.List;

@UtilityClass
public class SummaryUtil {

    public static boolean isGroupMessage(Message message) {
        return message.isGroupMessage() || message.isSuperGroupMessage();
    }

    public static String extractMessageText(String botName, Message message) {
        return message.getText().strip()
                .replace(botName, "").strip();
    }

    public static boolean isBotCalling(String botName, Message message, MessageEntity entity) {
        return EntityType.MENTION.equals(entity.getType())
                && botName.equals(entity.getText())
                && message.getText().startsWith(botName);
    }

    public static String getDisplayableName(User from) {
        List<String> result = new ArrayList<>();

        if (!StringUtils.hasLength(from.getFirstName()) && !StringUtils.hasLength(from.getLastName())) {
            result.add(from.getUserName());
        } else {
            if (StringUtils.hasLength(from.getFirstName())) {
                result.add(from.getFirstName());
            }

            if (StringUtils.hasLength(from.getLastName())) {
                result.add(from.getLastName());
            }
        }

        return String.join(" ", result);
    }
}
