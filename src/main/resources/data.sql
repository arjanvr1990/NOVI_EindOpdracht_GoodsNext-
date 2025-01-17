-- Voeg gebruikers toe
INSERT INTO users (user_id, username, password) VALUES
    (1, 'admin', '$2a$12$wGGxeb9hbrMEIDomwSpHWeG6ZhGcZ7z3vULAXl6iWSSDPKlpwVzO6'); -- password: admin



-- Voeg autoriteiten toe
INSERT INTO authorities (user_id, authority) VALUES
                                                 (1, 'ROLE_ADMIN'),
                                                 (1, 'ROLE_USER');

