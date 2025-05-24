INSERT INTO roles (name, code)
VALUES ('Пользователь', 'ROLE_USER'),
       ('Администратор', 'ROLE_ADMIN')
ON CONFLICT (code) DO NOTHING;