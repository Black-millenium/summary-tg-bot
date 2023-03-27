package com.romaskull.summarytgbot.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
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
    String assistantInstruction;
}
