-- =============================================
-- V2__seed_initial_categories_and_products.sql
-- Seed initial categories and products
-- =============================================

-- ---------- Categories ----------
insert into category (id, name, description)
values (nextval('category_seq'), 'ELECTRONICS', 'Electronic devices and accessories'),
       (nextval('category_seq'), 'BOOKS', 'Books and educational material'),
       (nextval('category_seq'), 'CLOTHING', 'Men and women clothing'),
       (nextval('category_seq'), 'HOME', 'Home appliances and furniture'),
       (nextval('category_seq'), 'SPORTS', 'Sports equipment and accessories') on conflict do nothing;


-- ---------- ELECTRONICS ----------
insert into product (id, name, description, available_quantity, price, category_id)
values (nextval('product_seq'),
        'MacBook Pro 14"',
        'Apple M2 Pro, 16GB RAM, 512GB SSD',
        15,
        8999.99,
        (select id from category where name = 'ELECTRONICS')),
       (nextval('product_seq'),
        'Dell XPS 13',
        'Ultra portable laptop with Intel i7',
        20,
        6799.50,
        (select id from category where name = 'ELECTRONICS')),
       (nextval('product_seq'),
        'Samsung Galaxy S23',
        'Flagship smartphone with AMOLED display',
        40,
        4299.90,
        (select id from category where name = 'ELECTRONICS'));


-- ---------- BOOKS ----------
insert into product (id, name, description, available_quantity, price, category_id)
values (nextval('product_seq'),
        'Clean Architecture',
        'A guide to software structure and design',
        35,
        219.90,
        (select id from category where name = 'BOOKS')),
       (nextval('product_seq'),
        'Designing Data-Intensive Applications',
        'Modern systems design and scalability',
        25,
        289.00,
        (select id from category where name = 'BOOKS')),
       (nextval('product_seq'),
        'Spring Boot in Practice',
        'Production-ready Spring Boot applications',
        30,
        199.50,
        (select id from category where name = 'BOOKS'));


-- ---------- CLOTHING ----------
insert into product (id, name, description, available_quantity, price, category_id)
values (nextval('product_seq'),
        'Basic Cotton T-Shirt',
        'Unisex cotton t-shirt size M',
        150,
        39.90,
        (select id from category where name = 'CLOTHING')),
       (nextval('product_seq'),
        'Slim Fit Jeans',
        'Denim jeans for everyday wear',
        90,
        149.90,
        (select id from category where name = 'CLOTHING')),
       (nextval('product_seq'),
        'Winter Jacket',
        'Waterproof jacket with thermal insulation',
        45,
        349.00,
        (select id from category where name = 'CLOTHING'));


-- ---------- HOME ----------
insert into product (id, name, description, available_quantity, price, category_id)
values (nextval('product_seq'),
        'Ergonomic Office Chair',
        'Adjustable ergonomic chair',
        25,
        999.00,
        (select id from category where name = 'HOME')),
       (nextval('product_seq'),
        'Automatic Coffee Maker',
        'Programmable coffee maker 1.5L',
        18,
        649.90,
        (select id from category where name = 'HOME')),
       (nextval('product_seq'),
        'LED Floor Lamp',
        'Minimalist LED lamp with dimmer',
        40,
        189.90,
        (select id from category where name = 'HOME'));


-- ---------- SPORTS ----------
insert into product (id, name, description, available_quantity, price, category_id)
values (nextval('product_seq'),
        'Professional Football',
        'Official size and weight football',
        100,
        89.90,
        (select id from category where name = 'SPORTS')),
       (nextval('product_seq'),
        'Yoga Mat Pro',
        'Non-slip yoga mat high density',
        120,
        59.90,
        (select id from category where name = 'SPORTS')),
       (nextval('product_seq'),
        'Adjustable Dumbbells Set',
        '20kg adjustable dumbbells kit',
        35,
        499.00,
        (select id from category where name = 'SPORTS'));
