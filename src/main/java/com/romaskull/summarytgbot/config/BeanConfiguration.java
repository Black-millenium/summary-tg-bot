package com.romaskull.summarytgbot.config;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.romaskull.summarytgbot.properties.GptProperties;
import com.romaskull.summarytgbot.service.SummaryService;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestTemplate;
import org.telegram.telegrambots.updatesreceivers.ServerlessWebhook;

@Configuration
public class BeanConfiguration {

    @Bean
    public ServerlessWebhook getServerlessWebhook(SummaryService summaryService) {
        ServerlessWebhook webhook = new ServerlessWebhook();
        webhook.registerWebhook(summaryService);
        return webhook;
    }

    @Primary
    @Bean
    public RestTemplate gptRestTemplate(GptProperties gptProperties) {
        return new RestTemplateBuilder()
                .defaultHeader("OpenAI-Organization", gptProperties.getOrganization())
                .defaultHeader(HttpHeaders.AUTHORIZATION, "Bearer " + gptProperties.getToken())
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE + ";charset=UTF-8")
                .build();
    }

    @Bean
    public ObjectMapper getObjectMapper() {
        return JsonMapper.builder()
                .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
                .addModule(new JavaTimeModule())
                .build();
    }
}
