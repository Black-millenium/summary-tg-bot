package com.romaskull.summarytgbot.properties;

import lombok.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Value
@ConfigurationProperties(prefix = "app.dialogue-bot")
public class DialogueProperties {
    String username;
    String token;
    String botPath;
    String secretToken;
    String acceptMessage;
}
