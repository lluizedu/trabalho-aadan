DROP DATABASE IF EXISTS biblioteca_crud;
CREATE DATABASE biblioteca_crud CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE biblioteca_crud;

CREATE TABLE editoras (
    id INT AUTO_INCREMENT PRIMARY KEY,
    nome VARCHAR(120) NOT NULL UNIQUE,
    telefone VARCHAR(20)
);

CREATE TABLE categorias (
    id INT AUTO_INCREMENT PRIMARY KEY,
    nome VARCHAR(80) NOT NULL UNIQUE
);

CREATE TABLE autores (
    id INT AUTO_INCREMENT PRIMARY KEY,
    nome VARCHAR(120) NOT NULL,
    nacionalidade VARCHAR(80),
    UNIQUE (nome, nacionalidade)
);

CREATE TABLE livros (
    id INT AUTO_INCREMENT PRIMARY KEY,
    titulo VARCHAR(160) NOT NULL,
    isbn VARCHAR(20) NOT NULL UNIQUE,
    ano_publicacao INT,
    quantidade_total INT NOT NULL DEFAULT 1,
    editora_id INT NOT NULL,
    categoria_id INT NOT NULL,
    CONSTRAINT chk_livro_ano CHECK (ano_publicacao IS NULL OR ano_publicacao BETWEEN 1400 AND 2100),
    CONSTRAINT chk_livro_quantidade CHECK (quantidade_total >= 0),
    CONSTRAINT fk_livro_editora FOREIGN KEY (editora_id) REFERENCES editoras(id),
    CONSTRAINT fk_livro_categoria FOREIGN KEY (categoria_id) REFERENCES categorias(id)
);

CREATE TABLE livro_autor (
    livro_id INT NOT NULL,
    autor_id INT NOT NULL,
    PRIMARY KEY (livro_id, autor_id),
    CONSTRAINT fk_livro_autor_livro FOREIGN KEY (livro_id) REFERENCES livros(id) ON DELETE CASCADE,
    CONSTRAINT fk_livro_autor_autor FOREIGN KEY (autor_id) REFERENCES autores(id)
);

CREATE TABLE usuarios (
    id INT AUTO_INCREMENT PRIMARY KEY,
    nome VARCHAR(120) NOT NULL,
    email VARCHAR(140) NOT NULL UNIQUE,
    telefone VARCHAR(20),
    ativo BOOLEAN NOT NULL DEFAULT TRUE
);

CREATE TABLE emprestimos (
    id INT AUTO_INCREMENT PRIMARY KEY,
    livro_id INT NOT NULL,
    usuario_id INT NOT NULL,
    data_emprestimo DATE NOT NULL,
    data_prevista_devolucao DATE NOT NULL,
    data_devolucao DATE,
    CONSTRAINT chk_datas_emprestimo CHECK (data_prevista_devolucao >= data_emprestimo),
    CONSTRAINT chk_data_devolucao CHECK (data_devolucao IS NULL OR data_devolucao >= data_emprestimo),
    CONSTRAINT fk_emprestimo_livro FOREIGN KEY (livro_id) REFERENCES livros(id),
    CONSTRAINT fk_emprestimo_usuario FOREIGN KEY (usuario_id) REFERENCES usuarios(id)
);

CREATE VIEW vw_livros_disponiveis AS
SELECT
    l.id,
    l.titulo,
    l.isbn,
    l.quantidade_total,
    l.quantidade_total - COUNT(e.id) AS quantidade_disponivel
FROM livros l
LEFT JOIN emprestimos e ON e.livro_id = l.id AND e.data_devolucao IS NULL
GROUP BY l.id, l.titulo, l.isbn, l.quantidade_total;

CREATE VIEW vw_emprestimos_abertos AS
SELECT
    e.id,
    l.titulo AS livro,
    u.nome AS usuario,
    e.data_emprestimo,
    e.data_prevista_devolucao,
    CASE
        WHEN e.data_prevista_devolucao < CURRENT_DATE THEN 'ATRASADO'
        ELSE 'EM DIA'
    END AS situacao
FROM emprestimos e
JOIN livros l ON l.id = e.livro_id
JOIN usuarios u ON u.id = e.usuario_id
WHERE e.data_devolucao IS NULL;

INSERT INTO editoras (nome, telefone) VALUES
('Companhia das Letras', '6133331111'),
('Novatec', '6133332222'),
('Alta Books', '6133333333');

INSERT INTO categorias (nome) VALUES
('Romance'),
('Tecnologia'),
('Banco de Dados'),
('Programacao');

INSERT INTO autores (nome, nacionalidade) VALUES
('Machado de Assis', 'Brasileira'),
('Robert C. Martin', 'Americana'),
('Abraham Silberschatz', 'Americana'),
('Carlos Heitor Cony', 'Brasileira');

INSERT INTO livros (titulo, isbn, ano_publicacao, quantidade_total, editora_id, categoria_id) VALUES
('Dom Casmurro', '9788535910663', 1899, 3, 1, 1),
('Codigo Limpo', '9788576082675', 2008, 2, 2, 4),
('Sistema de Banco de Dados', '9788535251425', 2019, 2, 3, 3);

INSERT INTO livro_autor (livro_id, autor_id) VALUES
(1, 1),
(2, 2),
(3, 3);

INSERT INTO usuarios (nome, email, telefone, ativo) VALUES
('Ana Souza', 'ana.souza@email.com', '61999990001', TRUE),
('Bruno Lima', 'bruno.lima@email.com', '61999990002', TRUE),
('Carla Mendes', 'carla.mendes@email.com', '61999990003', TRUE);

INSERT INTO emprestimos (livro_id, usuario_id, data_emprestimo, data_prevista_devolucao) VALUES
(1, 1, CURRENT_DATE, DATE_ADD(CURRENT_DATE, INTERVAL 7 DAY));
