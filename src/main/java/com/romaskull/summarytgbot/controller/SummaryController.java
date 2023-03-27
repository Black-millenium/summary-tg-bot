package com.romaskull.summarytgbot.controller;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.updatesreceivers.ServerlessWebhook;

@Slf4j
@RequiredArgsConstructor
@RestController
public class SummaryController {

    private final ServerlessWebhook webhook;

    @SneakyThrows
    @PostMapping("/{botPath}")
    public void updateReceived(@RequestBody Update request,
                               @PathVariable String botPath) {
        webhook.updateReceived(botPath, request);
    }
}
