package com.romaskull.summarytgbot.entity;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBRangeKey;
import lombok.Data;

@Data
public class PrimaryKey {

    @DynamoDBHashKey(attributeName = "chat_id")
    private Long chatId;

    @DynamoDBRangeKey(attributeName = "message_id")
    private Long messageId;
}
