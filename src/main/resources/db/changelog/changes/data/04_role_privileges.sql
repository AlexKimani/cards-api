-- Assign privileges to roles
INSERT INTO role_privileges (role_id, privilege_id)
VALUES (1, 1), -- Admin can Create  their card
       (1, 2), -- Admin can Edit their card
       (1, 3), -- Admin can Delete their card
       (1, 4), -- Admin can View their card
       (1, 5), -- Admin can View All cards
       (1, 6), -- Admin can Delete all cards
       (2, 2), -- Member can Edit their card
       (2, 4), -- Member can View their card
       (2, 1), -- Member can Create their card
       (2, 3); -- Member can delete their card