package com.romaskull.summarytgbot.config;

import com.romaskull.summarytgbot.bot.DialogueBot;
import com.romaskull.summarytgbot.bot.SummaryBot;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.updatesreceivers.ServerlessWebhook;

@Slf4j
@RequiredArgsConstructor
@Component
public class ListenerConfiguration {

    @SneakyThrows
    @Bean
    public ServerlessWebhook getWebhook(SummaryBot summaryBot, DialogueBot dialogueBot) {
        ServerlessWebhook webhook = new ServerlessWebhook();
        webhook.registerWebhook(summaryBot);
        webhook.registerWebhook(dialogueBot);
        return webhook;
    }
}
