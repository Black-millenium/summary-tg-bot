package com.romaskull.summarytgbot.repository;

import com.romaskull.summarytgbot.entity.ChatMessage;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface TelegramChatMessageRepository extends CrudRepository<ChatMessage, Long> {

    @Query("SELECT * FROM telegram_chat_messages WHERE chat_id = :chatId ORDER BY created_at DESC LIMIT :limit")
    List<ChatMessage> findLatestMessages(Long chatId, int limit);
}
