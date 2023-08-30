-- Create privileges table
CREATE TABLE IF NOT EXISTS privileges
(
    id             INT PRIMARY KEY AUTO_INCREMENT,
    privilege_name VARCHAR(50) NOT NULL
);