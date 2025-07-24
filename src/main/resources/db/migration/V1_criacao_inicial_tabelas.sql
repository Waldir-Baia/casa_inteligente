-- criação tabela Liberacao
CREATE TABLE liberacao (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    nome VARCHAR(255) NOT NULL,
    senha VARCHAR(255) NOT NULL
);

-- criação tabela
CREATE TABLE roles (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    nome VARCHAR(255) NOT NULL UNIQUE
);

-- criação tabela User
CREATE TABLE user (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    login VARCHAR(255) NOT NULL UNIQUE,
    url VARCHAR(255) NOT NULL
);

-- criação tabela user_roles
CREATE TABLE user_roles (
    user_id BIGINT NOT NULL,
    role_id BIGINT NOT NULL,
    PRIMARY KEY (user_id, role_id),
    FOREIGN KEY (user_id) REFERENCES user(id) ON DELETE NO ACTION ON UPDATE NO ACTION,
    FOREIGN KEY (role_id) REFERENCES roles(id) ON DELETE NO ACTION ON UPDATE NO ACTION
);

-- Insert inicial
INSERT INTO liberacao (nome, senha) VALUES
('admin', '$10$bFphits6/mcWc3jim8wwvOL0sitvooGbiT4H.b/LBX5zKaDC4JO6.');