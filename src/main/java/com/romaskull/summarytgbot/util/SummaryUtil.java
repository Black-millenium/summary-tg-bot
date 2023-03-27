package com.romaskull.summarytgbot.util;

import lombok.experimental.UtilityClass;
import org.springframework.util.StringUtils;
import org.telegram.telegrambots.meta.api.objects.EntityType;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.MessageEntity;
import org.telegram.telegrambots.meta.api.objects.User;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@UtilityClass
public class SummaryUtil {

    private static final String SUMGPTBOT = "@sumgptbot";

    public static boolean isGroupMessage(Message message) {
        return message.isGroupMessage() || message.isSuperGroupMessage();
    }

    public static String extractMessageText(Message message) {
        return message.getText().replace(SUMGPTBOT, "").strip();
    }

    public static boolean isBotCalling(Message message, MessageEntity entity) {
        return EntityType.MENTION.equals(entity.getType())
                && SUMGPTBOT.equals(entity.getText())
                && message.getText().startsWith(SUMGPTBOT);
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

        return result.stream().collect(Collectors.joining(" ", "[", "]"));
    }
}
