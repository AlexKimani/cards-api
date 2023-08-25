-- Card access table (Members' access to cards they created)
CREATE TABLE card_access
(
    user_id INT,
    card_id INT,
    FOREIGN KEY (user_id) REFERENCES users (id),
    FOREIGN KEY (card_id) REFERENCES cards (id),
    PRIMARY KEY (user_id, card_id)
);