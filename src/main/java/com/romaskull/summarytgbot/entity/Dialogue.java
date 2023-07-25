package com.romaskull.summarytgbot.entity;

import com.romaskull.summarytgbot.dto.GptRole;
import lombok.Data;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.index.CompoundIndexes;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.time.LocalDateTime;

@Data
@CompoundIndexes({
        @CompoundIndex(name = "dialogues", def = "{'chat_id': 1, 'dialogue_number': -1, 'created_at': -1}")
})
@Document("telegram_dialogues")
public class Dialogue {

    @Field("chat_id")
    private Long chatId;

    @Field("sender_id")
    private Long senderId;

    @Field("dialogue_number")
    private Long dialogueNumber;

    @Field("sender_name")
    private String senderName;

    @Field("message_id")
    private Long messageId;

    @Field("message")
    private String message;

    @Field("created_at")
    private LocalDateTime createdAt;

    @Field("role")
    private GptRole gptRole;
}
