package com.romaskull.summarytgbot.properties;

import lombok.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Value
@ConfigurationProperties(prefix = "app.gpt")
public class GptProperties {
    String token;
    String organization;
    double temperature;
    int maxBatchSize;
    int maxHistory;
    int maxTokens;
    String model;
    String url;
    String systemInstruction;
    String summarizationInstruction;
}
