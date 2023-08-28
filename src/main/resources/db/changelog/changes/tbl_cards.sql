-- Cards table
CREATE TABLE cards
(
    id          INT PRIMARY KEY AUTO_INCREMENT,
    name        VARCHAR(100)                          NOT NULL,
    description TEXT,
    color       VARCHAR(7)                                     DEFAULT NULL,
    status      ENUM ('TO_DO', 'IN_PROGRESS', 'DONE') NOT NULL DEFAULT 'TO_DO',
    date_created DATE   NOT NULL                               DEFAULT (CURRENT_DATE),
    created_by  INT,
    created_at  TIMESTAMP                                      DEFAULT NOW(),
    FOREIGN KEY (created_by) REFERENCES users (id)
);