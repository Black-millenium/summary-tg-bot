package com.romaskull.summarytgbot.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public record ChatGptRequest(String model,
                             @JsonProperty("max_tokens") int maxTokens,
                             double temperature,
                             List<GptMessage> messages) {
}
