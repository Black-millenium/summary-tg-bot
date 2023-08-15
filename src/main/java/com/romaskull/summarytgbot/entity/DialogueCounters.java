package com.romaskull.summarytgbot.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Document("dialogue_counters")
public class DialogueCounters {

    @Id
    @Indexed
    private Long senderId;

    @Field("sender_name")
    private String senderName;

    @Field("dialogue_number")
    private Long dialogueNumber;
}
