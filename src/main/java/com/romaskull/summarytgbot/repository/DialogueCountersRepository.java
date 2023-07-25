package com.romaskull.summarytgbot.repository;

import com.romaskull.summarytgbot.entity.DialogueCounters;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface DialogueCountersRepository extends MongoRepository<DialogueCounters, String> {

    Optional<DialogueCounters> findBySenderId(Long senderId);
}
