ALTER TABLE users ADD COLUMN password VARCHAR(255) NOT NULL DEFAULT 'temp_password';

ALTER TABLE users ALTER COLUMN password DROP DEFAULT;

INSERT INTO users (login, url, password) VALUES ('admin', 'https://github.com/admin', '$2a$10$8laB3Ta4e42rMJi2BqYhhO9C0HetrCOm6BiSYwO.VXp1hjuVonKbm');

INSERT INTO roles (nome) VALUES ('ADMIN') ON DUPLICATE KEY UPDATE nome=nome;
INSERT INTO roles (nome) VALUES ('USER') ON DUPLICATE KEY UPDATE nome=nome;

INSERT INTO user_roles (user_id, role_id)
SELECT u.id, r.id
FROM users u, roles r
WHERE u.login = 'admin' AND r.name = 'ADMIN'
ON DUPLICATE KEY UPDATE user_id = u.id;