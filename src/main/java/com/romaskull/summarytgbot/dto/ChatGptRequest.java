package com.romaskull.summarytgbot.dto;

import java.util.List;

public record ChatGptRequest(String model, List<GptMessage> messages) {
}
