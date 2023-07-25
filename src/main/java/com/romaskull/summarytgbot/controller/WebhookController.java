package com.romaskull.summarytgbot.controller;

import com.romaskull.summarytgbot.properties.DialogueProperties;
import com.romaskull.summarytgbot.properties.SummaryBotProperties;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.auth.AuthenticationException;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.updatesreceivers.ServerlessWebhook;

@Slf4j
@RequiredArgsConstructor
@RestController
public class WebhookController {

    private final ServerlessWebhook webhook;
    private final SummaryBotProperties summaryBotProperties;
    private final DialogueProperties dialogueProperties;

    @SneakyThrows
    @PostMapping("/{botPath}")
    public BotApiMethod<?> handle(@PathVariable String botPath,
                                  @RequestBody Update update,
                                  @RequestHeader("X-Telegram-Bot-Api-Secret-Token") String authSecret,
                                  HttpServletResponse httpServletResponse) {
        if (summaryBotProperties.getSecretToken().equals(authSecret)
                || dialogueProperties.getSecretToken().equals(authSecret)) {
            return webhook.updateReceived(botPath, update);
        }

        httpServletResponse.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        httpServletResponse.sendError(HttpServletResponse.SC_UNAUTHORIZED, "SC_UNAUTHORIZED");
        throw new AuthenticationException("Unauthorized");
    }
}
