package com.romaskull.summarytgbot.entity;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBRangeKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTypeConverted;
import com.romaskull.summarytgbot.converter.LocalDateConverter;
import lombok.AccessLevel;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;

import java.time.LocalDateTime;

@Data
@DynamoDBTable(tableName = "telegram_chat_messages")
public class ChatMessage {

    @Id
    @Setter(AccessLevel.NONE)
    @Getter(AccessLevel.NONE)
    private PrimaryKey key;

    @DynamoDBAttribute(attributeName = "message")
    private String message;

    @DynamoDBAttribute(attributeName = "sender_name")
    private String senderName;

    @DynamoDBAttribute(attributeName = "sender_id")
    private Long senderId;

    @DynamoDBTypeConverted(converter = LocalDateConverter.class)
    private LocalDateTime createdAt;

    @DynamoDBHashKey(attributeName = "chat_id")
    public Long getChatId() {
        return key.getChatId();
    }

    public void setChatId(Long chatId) {
        if (key == null) {
            key = new PrimaryKey();
        }

        key.setChatId(chatId);
    }

    @DynamoDBRangeKey(attributeName = "message_id")
    public Long getMessageId() {
        return key.getMessageId();
    }

    public void setMessageId(Long messageId) {
        if (key == null) {
            key = new PrimaryKey();
        }

        key.setMessageId(messageId);
    }


}
