package com.romaskull.summarytgbot.properties;

import lombok.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Value
@ConfigurationProperties(prefix = "app.gpt")
public class GptProperties {
    String token;
    String organization;
    double temperature;
    String model;
    String url;
    int maxHistory;
    String systemInstruction;
    String assistantInstruction;
}
