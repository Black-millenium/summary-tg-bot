package com.romaskull.summarytgbot.entity;

import lombok.Data;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.index.CompoundIndexes;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.time.LocalDateTime;

@Data
@CompoundIndexes({
        @CompoundIndex(name = "messages_in_chat", def = "{'chat_id': 1, 'message_id': -1}")
})
@Document("telegram_chat_messages")
public class ChatMessage {

    @Field("chat_id")
    private Long chatId;

    @Field("message_id")
    private Long messageId;

    @Field("message")
    private String message;

    @Field("sender_name")
    private String senderName;

    @Field("sender_id")
    private Long senderId;

    @Field("created_at")
    private LocalDateTime createdAt;
}
