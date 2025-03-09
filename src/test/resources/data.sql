INSERT INTO roles (name)
VALUES ('ROLE_ADMIN'),
       ('ROLE_JOURNALIST'),
       ('ROLE_SUBSCRIBER');

-- Создаем тестовых пользователей с разными ролями
INSERT INTO users (username, password, name, surname, "parentName", "roleId")
SELECT 'user' || i,
       'password' || i,
       'Name' || i,
       'Surname' || i,
       'Parent' || i,
       (SELECT id FROM roles ORDER BY RANDOM() LIMIT 1) -- Случайная роль
FROM generate_series(1, 5) AS i;

-- вручную делаем админа с предопределенным для тестирования uuid
UPDATE users SET id = '5cce18df-81a1-46b5-943e-f691dd59d806' WHERE username = 'user1';
UPDATE users SET "roleId" = (SELECT id FROM roles WHERE name = 'ROLE_ADMIN') WHERE id = '5cce18df-81a1-46b5-943e-f691dd59d806';

-- Создаем 20 новостей, каждая привязана к случайному пользователю
INSERT INTO news (title, text, "insertedById", "updatedById")
SELECT 'News Title ' || i,
       'News Content ' || i,
       (SELECT id FROM users ORDER BY RANDOM() LIMIT 1), -- Случайный пользователь
       (SELECT id FROM users ORDER BY RANDOM() LIMIT 1)  -- Случайный редактор
FROM generate_series(1, 20) AS i;

-- Создаем 10 комментариев к каждой новости от случайных пользователей
INSERT INTO comments (text, "insertedById", "idNews")
SELECT 'Comment ' || i || ' on News ' || n.id,
       (SELECT id FROM users ORDER BY RANDOM() LIMIT 1), -- Случайный пользователь
       n.id
FROM news AS n,
     generate_series(1, 10) AS i;