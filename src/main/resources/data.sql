-- -- Voeg gebruikers toe zonder expliciete user_id
-- INSERT INTO users (username, password) VALUES
--                                            ('admin', '$2a$12$wGGxeb9hbrMEIDomwSpHWeG6ZhGcZ7z3vULAXl6iWSSDPKlpwVzO6'), -- password: admin
--                                            ('user', '$2a$12$TzVjQpybrq7qlBtXMTtFuO5a.P9RpoNV8VwZQZmoYQaixzYwWodIO'); -- password: user
--
-- -- Voeg autoriteiten toe op basis van user_id
-- INSERT INTO authorities (user_id, authority) VALUES
--                                                  (1, 'ROLE_ADMIN'),
--                                                  (1, 'ROLE_USER'),
--                                                  (2, 'ROLE_USER');
--
-- Voeg gebruikers toe
INSERT INTO users (username, password) VALUES
                                           ('admin', '$2a$12$wGGxeb9hbrMEIDomwSpHWeG6ZhGcZ7z3vULAXl6iWSSDPKlpwVzO6'), -- password: admin
                                           ('user', '$2a$12$TzVjQpybrq7qlBtXMTtFuO5a.P9RpoNV8VwZQZmoYQaixzYwWodIO'); -- password: user

-- Voeg autoriteiten toe
INSERT INTO authorities (user_id, authority) VALUES
                                                 (1, 'ROLE_ADMIN'),
                                                 (1, 'ROLE_USER'),
                                                 (2, 'ROLE_USER');

-- Voeg ContactInfo toe
INSERT INTO contact_info (email, city, postal_code, address, phone_number) VALUES
                                                                               ('admin@example.com', 'Amsterdam', '1234AB', 'Admin Street 1', '0612345678'),
                                                                               ('user@example.com', 'Rotterdam', '5678CD', 'User Street 2', '0698765432');

-- Koppel ContactInfo aan gebruikers
UPDATE users SET contact_info_id = 1 WHERE username = 'admin';
UPDATE users SET contact_info_id = 2 WHERE username = 'user';
