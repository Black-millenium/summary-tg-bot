package com.romaskull.summarytgbot.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties(prefix = "app.telegram")
public class TelegarmProperties {
    String username;
    String token;
}
