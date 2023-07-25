package com.romaskull.summarytgbot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.scheduling.annotation.EnableAsync;

@EnableAsync
@ConfigurationPropertiesScan
@EnableConfigurationProperties
@SpringBootApplication
public class SummaryTgBotApplication {

    public static void main(String[] args) {
        SpringApplication.run(SummaryTgBotApplication.class, args);
    }

}
