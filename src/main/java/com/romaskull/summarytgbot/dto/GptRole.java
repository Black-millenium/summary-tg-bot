package com.romaskull.summarytgbot.dto;

import com.fasterxml.jackson.annotation.JsonValue;
import lombok.ToString;

@ToString
public enum GptRole {
    SYSTEM("system"),
    USER("user"),
    ASSISTANT("assistant");

    private final String roleName;

    @JsonValue
    public String getRoleName() {
        return roleName;
    }

    GptRole(String roleName) {
        this.roleName = roleName;
    }
}
