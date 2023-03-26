package com.romaskull.summarytgbot.config;

import com.romaskull.summarytgbot.service.TelegramBotSummaryService;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.TelegramBotsApi;

@Slf4j
@RequiredArgsConstructor
@Component
public class RegistrationBotListener {

    private final TelegramBotsApi telegramBotsApi;
    private final TelegramBotSummaryService telegramBotSummaryService;

    @SneakyThrows
    @EventListener
    public void onApplicationEvent(ContextRefreshedEvent event) {
        telegramBotsApi.registerBot(telegramBotSummaryService);
        log.info("Bot registered");
    }
}
