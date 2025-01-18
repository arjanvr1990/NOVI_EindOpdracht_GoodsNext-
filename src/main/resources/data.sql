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

-- Voeg shops toe
INSERT INTO contact_info (email, city, postal_code, address, phone_number) VALUES
                                                                               ('shop1@example.com', 'Utrecht', '1234AA', 'Shop Street 1', '0611111111'),
                                                                               ('shop2@example.com', 'Den Haag', '5678BB', 'Shop Street 2', '0622222222'),
                                                                               ('shop3@example.com', 'Eindhoven', '9876CC', 'Shop Street 3', '0633333333'),
                                                                               ('shop4@example.com', 'Maastricht', '5432DD', 'Shop Street 4', '0644444444'),
                                                                               ('shop5@example.com', 'Groningen', '6789EE', 'Shop Street 5', '0655555555');

INSERT INTO shops (shop_name, contact_info_id, photo_upload_id) VALUES
                                                                    ('Tech Store', 3, NULL),
                                                                    ('Fashion Outlet', 4, NULL),
                                                                    ('Book Haven', 5, NULL),
                                                                    ('Gadget Galaxy', 6, NULL),
                                                                    ('Home Comforts', 7, NULL);

INSERT INTO photo_uploads (file_name, file_type, file_size) VALUES
                                                                ('logo1.png', 'image/png', 12345),
                                                                ('logo2.jpg', 'image/jpeg', 54321);
