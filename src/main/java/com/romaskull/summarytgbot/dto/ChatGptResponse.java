package com.romaskull.summarytgbot.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.ToString;
import lombok.Value;

import java.util.List;


@Value
public class ChatGptResponse {
    String id;
    String object;
    Long created;
    String model;
    UsageInfo usage;
    List<ChoicesResult> choices;

    @Value
    public static class UsageInfo {

        @JsonProperty("prompt_tokens")
        Long promptTokens;

        @JsonProperty("completion_tokens")
        Long completionTokens;

        @JsonProperty("total_tokens")
        Long totalTokens;
    }

    @Value
    public static class ChoicesResult {
        Message message;

        @JsonProperty("finish_reason")
        String finishReason;

        Long index;
    }

    @Value
    public static class Message {
        String role;

        @ToString.Exclude
        String content;
    }
}
