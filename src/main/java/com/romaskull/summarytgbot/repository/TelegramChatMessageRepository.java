package com.romaskull.summarytgbot.repository;

import com.romaskull.summarytgbot.entity.ChatMessage;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TelegramChatMessageRepository extends MongoRepository<ChatMessage, String> {

    List<ChatMessage> findByChatId(Long chatId, Pageable pageable);
}
