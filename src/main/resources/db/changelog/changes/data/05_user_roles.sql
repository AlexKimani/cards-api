-- Assign roles to users
INSERT INTO user_roles (user_id, role_id)
VALUES (1, 1), -- user1 is an Admin
       (2, 2); -- user2 is an Member