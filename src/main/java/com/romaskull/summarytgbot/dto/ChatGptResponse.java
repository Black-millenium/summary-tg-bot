package com.romaskull.summarytgbot.dto;

import java.util.List;


public record ChatGptResponse(String id, String object, Long created,
                              String model, com.romaskull.summarytgbot.dto.ChatGptResponse.UsageInfo usage,
                              List<ChoicesResult> choices) {

    public record UsageInfo(Long promptTokens, Long completionTokens, Long totalTokens) {
    }

    public record ChoicesResult(Message message, String finishReason,
                                Long index) {
    }

    public record Message(String role, String content) {
    }
}
