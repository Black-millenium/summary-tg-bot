CREATE TABLE telegram_chat_messages
(
    id          BIGINT PRIMARY KEY AUTO_INCREMENT,
    chat_id     NUMERIC   NOT NULL,
    message     TEXT,
    sender_name VARCHAR(255),
    sender_id   NUMERIC   NOT NULL,
    created_at  TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);
