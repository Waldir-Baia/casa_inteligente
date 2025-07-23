ALTER TABLE users ADD COLUMN password VARCHAR(255) NOT NULL DEFAULT 'temp_password';

ALTER TABLE users ALTER COLUMN password DROP DEFAULT;

INSERT INTO users (login, url, password) VALUES ('admin', 'https://github.com/admin', '$2a$10$FpGkU.2rM0vFmX2Zq2n2Oe.1wQ7M.5wQ7M.5wQ7M.5wQ7M.5wQ7M.5'); -- <-- SUBSTITUA ESTE HASH

INSERT INTO roles (name) VALUES ('ADMIN') ON DUPLICATE KEY UPDATE name=name;
INSERT INTO roles (name) VALUES ('USER') ON DUPLICATE KEY UPDATE name=name;

INSERT INTO user_roles (user_id, role_id)
SELECT u.id, r.id
FROM users u, roles r
WHERE u.login = 'admin' AND r.name = 'ADMIN'
ON DUPLICATE KEY UPDATE user_id = u.id;