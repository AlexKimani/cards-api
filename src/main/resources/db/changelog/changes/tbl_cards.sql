-- Cards table
CREATE TABLE cards
(
    id          INT PRIMARY KEY AUTO_INCREMENT,
    name        VARCHAR(100)                          NOT NULL,
    description TEXT,
    color       VARCHAR(7)                                     DEFAULT NULL,
    status      ENUM ('To Do', 'In Progress', 'Done') NOT NULL DEFAULT 'To Do',
    created_by  INT,
    created_at  TIMESTAMP                                      DEFAULT NOW(),
    FOREIGN KEY (created_by) REFERENCES users (id)
);