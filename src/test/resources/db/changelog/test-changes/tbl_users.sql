-- Create a table to store user information
CREATE TABLE users
(
    id       INT PRIMARY KEY AUTO_INCREMENT,
    email    VARCHAR(100) NOT NULL,
    password VARCHAR(255) NOT NULL,
    enabled  TINYINT(1) DEFAULT 1
);