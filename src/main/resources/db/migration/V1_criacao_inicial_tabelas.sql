CREATE TABLE IF NOT EXISTS liberacao (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    nome VARCHAR(255) NOT NULL,
    senha VARCHAR(255) NOT NULL
);

INSERT INTO liberacao (nome, senha) VALUES
('admin', '$10$bFphits6/mcWc3jim8wwvOL0sitvooGbiT4H.b/LBX5zKaDC4JO6.');
