-- Create a table to store user information
CREATE TABLE users
(
    id       INT PRIMARY KEY AUTO_INCREMENT,
    email    VARCHAR(100) NOT NULL,
    password VARCHAR(100) NOT NULL
);