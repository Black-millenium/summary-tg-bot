package com.romaskull.summarytgbot.properties;

import lombok.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Value
@ConfigurationProperties(prefix = "app.summary-bot")
public class SummaryBotProperties {

    String username;

    String token;

    String botPath;

    String secretToken;

    String acceptMessage;

    String systemInstruction;

    String summarizationInstruction;
}
