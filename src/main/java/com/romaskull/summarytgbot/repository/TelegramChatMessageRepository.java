package com.romaskull.summarytgbot.repository;

import com.romaskull.summarytgbot.entity.ChatMessage;
import com.romaskull.summarytgbot.entity.PrimaryKey;
import org.socialsignin.spring.data.dynamodb.repository.EnableScan;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;

@EnableScan
public interface TelegramChatMessageRepository extends PagingAndSortingRepository<ChatMessage, PrimaryKey> {


    List<ChatMessage> findByChatIdOrderByMessageIdDesc(Long chatId, Pageable pageable);
}
