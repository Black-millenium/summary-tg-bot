package com.romaskull.summarytgbot.config;

import com.romaskull.summarytgbot.properties.GptProperties;
import lombok.SneakyThrows;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestTemplate;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

@Configuration
public class BeanConfiguration {

    @SneakyThrows
    @Bean
    public TelegramBotsApi getBotsApi() {
        return new TelegramBotsApi(DefaultBotSession.class);
    }

    @Bean
    public RestTemplate gptRestTemplate(GptProperties gptProperties) {
        return new RestTemplateBuilder()
                .defaultHeader("OpenAI-Organization", gptProperties.getOrganization())
                .defaultHeader(HttpHeaders.AUTHORIZATION, "Bearer " + gptProperties.getToken())
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE + ";charset=UTF-8")
                .build();
    }
}
