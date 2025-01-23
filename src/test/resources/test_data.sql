
INSERT INTO users (username, password) VALUES
                                           ('admin', '$2a$12$wGGxeb9hbrMEIDomwSpHWeG6ZhGcZ7z3vULAXl6iWSSDPKlpwVzO6'), -- password: admin
                                           ('user', '$2a$12$TzVjQpybrq7qlBtXMTtFuO5a.P9RpoNV8VwZQZmoYQaixzYwWodIO'); -- password: user


INSERT INTO authorities (user_id, authority) VALUES
                                                 (1, 'ROLE_ADMIN'),
                                                 (1, 'ROLE_USER'),
                                                 (2, 'ROLE_USER');


INSERT INTO contact_info (email, city, postal_code, address, phone_number) VALUES
                                                                               ('admin@example.com', 'Amsterdam', '1234AB', 'Admin Street 1', '0612345678'),
                                                                               ('user@example.com', 'Rotterdam', '5678CD', 'User Street 2', '0698765432');


UPDATE users SET contact_info_id = 1 WHERE username = 'admin';
UPDATE users SET contact_info_id = 2 WHERE username = 'user';


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



INSERT INTO product (product_name, product_description, product_price, product_availability, product_img, shop_id) VALUES
                                                                                                                       ('Laptop', 'High-performance laptop', 999.99, true, 'laptop.png', 1),
                                                                                                                       ('Mouse', 'Wireless mouse', 29.99, true, 'mouse.png', 1),
                                                                                                                       ('Keyboard', 'Mechanical keyboard', 79.99, true, 'keyboard.png', 1),
                                                                                                                       ('Monitor', '4K Ultra HD Monitor', 299.99, true, 'monitor.png', 1),
                                                                                                                       ('Smartphone', 'Latest model smartphone', 699.99, true, 'smartphone.png', 1),

                                                                                                                       ('T-Shirt', 'Cotton T-shirt', 19.99, true, 'tshirt.png', 2),
                                                                                                                       ('Jeans', 'Blue denim jeans', 49.99, true, 'jeans.png', 2),
                                                                                                                       ('Jacket', 'Winter jacket', 99.99, true, 'jacket.png', 2),
                                                                                                                       ('Shoes', 'Running shoes', 89.99, true, 'shoes.png', 2),
                                                                                                                       ('Hat', 'Stylish hat', 24.99, true, 'hat.png', 2),

                                                                                                                       ('Novel', 'Bestselling novel', 14.99, true, 'novel.png', 3),
                                                                                                                       ('Magazine', 'Monthly magazine', 4.99, true, 'magazine.png', 3),
                                                                                                                       ('Textbook', 'Educational textbook', 59.99, true, 'textbook.png', 3),
                                                                                                                       ('Notebook', 'Pack of notebooks', 9.99, true, 'notebook.png', 3),
                                                                                                                       ('Comic Book', 'Popular comic book', 7.99, true, 'comicbook.png', 3),

                                                                                                                       ('Headphones', 'Noise-canceling headphones', 199.99, true, 'headphones.png', 4),
                                                                                                                       ('Smartwatch', 'Waterproof smartwatch', 149.99, true, 'smartwatch.png', 4),
                                                                                                                       ('Tablet', '10-inch tablet', 249.99, true, 'tablet.png', 4),
                                                                                                                       ('Camera', 'Digital camera', 499.99, true, 'camera.png', 4),
                                                                                                                       ('Drone', 'Compact drone', 399.99, true, 'drone.png', 4);
