package com.romaskull.summarytgbot.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;
import java.util.StringJoiner;


public record ChatGptResponse(String id,
                              String object,
                              Long created,
                              String model,
                              @JsonProperty("usage") UsageInfo usage,
                              List<ChoicesResult> choices) {

    public record UsageInfo(@JsonProperty("prompt_tokens") Long promptTokens,
                            @JsonProperty("completion_tokens") Long completionTokens,
                            @JsonProperty("total_tokens") Long totalTokens) {
    }

    public record ChoicesResult(Message message,
                                @JsonProperty("finish_reason") String finishReason,
                                Long index) {
    }

    public record Message(String role,
                          String content) {

        @Override
        public String toString() {
            return new StringJoiner(", ", Message.class.getSimpleName() + "[", "]")
                    .add("role='" + role + "'")
                    .toString();
        }
    }
}
