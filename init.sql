CREATE TABLE `products` (
    `id` int NOT NULL AUTO_INCREMENT COMMENT 'Primary Key',
    `name` varchar(45) DEFAULT NULL COMMENT 'Product Name',
    `price` decimal(10, 2) DEFAULT NULL COMMENT 'Price',
    `create_at` date DEFAULT NULL COMMENT 'Create Time',
    PRIMARY KEY (`id`)
) ENGINE = InnoDB AUTO_INCREMENT = 16 DEFAULT CHARSET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci;

INSERT INTO
    `products`
VALUES (
        1,
        'Panasonic',
        800.00,
        '2025-06-02'
    ),
    (
        2,
        'Sony',
        700.00,
        '2025-06-02'
    ),
    (
        3,
        'Apple',
        1000.00,
        '2025-06-02'
    ),
    (
        4,
        'Sony Notebook',
        1000.00,
        '2025-06-02'
    ),
    (
        5,
        'Hewlett Packard',
        500.00,
        '2025-06-02'
    ),
    (
        6,
        'Bianchi',
        600.00,
        '2025-06-02'
    ),
    (
        7,
        'Nike',
        100.00,
        '2025-06-02'
    ),
    (
        8,
        'Adidas',
        200.00,
        '2025-06-02'
    ),
    (
        9,
        'Reebok',
        300.00,
        '2025-06-02'
    ),
    (
        10,
        'Samsung',
        1250.00,
        '2025-06-05'
    ),
    (
        12,
        'Mesita del comedor',
        2500.95,
        NULL
    );

CREATE TABLE `roles` (
    `id` bigint NOT NULL AUTO_INCREMENT,
    `name` varchar(50) NOT NULL,
    PRIMARY KEY (`id`),
    UNIQUE KEY `name` (`name`)
) ENGINE = InnoDB AUTO_INCREMENT = 3 DEFAULT CHARSET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci;

INSERT INTO `roles` VALUES (2, 'ROLE_ADMIN'), (1, 'ROLE_USER');

CREATE TABLE `users` (
    `id` bigint NOT NULL AUTO_INCREMENT,
    `username` varchar(50) NOT NULL,
    `password` varchar(100) NOT NULL,
    `email` varchar(100) NOT NULL,
    `enabled` tinyint(1) NOT NULL DEFAULT '1',
    PRIMARY KEY (`id`),
    UNIQUE KEY `username` (`username`),
    UNIQUE KEY `email` (`email`)
) ENGINE = InnoDB AUTO_INCREMENT = 4 DEFAULT CHARSET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci;

INSERT INTO
    `users`
VALUES (
        1,
        'lawis',
        '$2a$10$4zjdbVsK2xXqd7Uqpiuluey8/t1bXnAygoPFJQeyvI3OkzeYfXezK',
        'correo@correo.com',
        1
    ),
    (
        3,
        'admin',
        '$2a$10$P8X62eFaqGKucoMho9Eu7eRvpOwQOk/uqIrZrZr5FgcBzw.H/NuZK',
        'admin@correo.com',
        1
    );

CREATE TABLE `users_roles` (
    `user_id` bigint NOT NULL,
    `role_id` bigint NOT NULL,
    UNIQUE KEY `uq_users_roles` (`user_id`, `role_id`),
    KEY `fk_roles` (`role_id`),
    CONSTRAINT `fk_roles` FOREIGN KEY (`role_id`) REFERENCES `roles` (`id`),
    CONSTRAINT `fk_users` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`)
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci;

INSERT INTO `users_roles` VALUES (1, 1), (3, 1), (3, 2);