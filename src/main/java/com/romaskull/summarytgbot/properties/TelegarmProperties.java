package com.romaskull.summarytgbot.properties;

import lombok.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Value
@ConfigurationProperties(prefix = "app.telegram")
public class TelegarmProperties {
    String username;
    String token;
}
