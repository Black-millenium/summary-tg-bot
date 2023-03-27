package com.romaskull.summarytgbot.dto;

import lombok.Value;

@Value
public class GptMessage {
    GptRole role;
    String content;
}
