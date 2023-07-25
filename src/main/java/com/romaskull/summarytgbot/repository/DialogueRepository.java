package com.romaskull.summarytgbot.repository;

import com.romaskull.summarytgbot.entity.Dialogue;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DialogueRepository extends MongoRepository<Dialogue, String> {

    List<Dialogue> findAllByChatIdAndDialogueNumber(Long chatId, Long dialogueNumber, Pageable pageable);
}
