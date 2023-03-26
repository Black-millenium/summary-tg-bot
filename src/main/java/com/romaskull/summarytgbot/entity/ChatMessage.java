package com.romaskull.summarytgbot.entity;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDateTime;

@Data
@Table("TELEGRAM_CHAT_MESSAGES")
public class ChatMessage {

    @Id
    private Long id;

    private Long chatId;

    private String message;

    private String senderName;

    private Long senderId;

    private LocalDateTime createdAt;
}
