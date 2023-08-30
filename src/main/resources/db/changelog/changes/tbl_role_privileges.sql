-- Create a mapping table to associate roles with privileges
CREATE TABLE IF NOT EXISTS role_privileges
(
    role_id      INT,
    privilege_id INT,
    FOREIGN KEY (role_id) REFERENCES roles (id),
    FOREIGN KEY (privilege_id) REFERENCES privileges (id),
    PRIMARY KEY (role_id, privilege_id)
);