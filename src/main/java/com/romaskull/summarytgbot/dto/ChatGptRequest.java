package com.romaskull.summarytgbot.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Value;

import java.util.List;

@Value
public class ChatGptRequest {

    String model;

    @JsonProperty("max_tokens")
    int maxTokens;

    double temperature;

    List<GptMessage> messages;
}
