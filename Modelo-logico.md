# Modelo Logico Relacional

**EDITORAS**(`id`, nome, telefone)

- `id` PK
- `nome` UNIQUE NOT NULL

**CATEGORIAS**(`id`, nome)

- `id` PK
- `nome` UNIQUE NOT NULL

**AUTORES**(`id`, nome, nacionalidade)

- `id` PK
- `nome` NOT NULL
- UNIQUE (`nome`, `nacionalidade`)

**LIVROS**(`id`, titulo, isbn, ano_publicacao, quantidade_total, editora_id, categoria_id)

- `id` PK
- `isbn` UNIQUE NOT NULL
- `editora_id` FK -> EDITORAS(`id`)
- `categoria_id` FK -> CATEGORIAS(`id`)
- CHECK `quantidade_total >= 0`

**LIVRO_AUTOR**(`livro_id`, `autor_id`)

- PK composta (`livro_id`, `autor_id`)
- `livro_id` FK -> LIVROS(`id`)
- `autor_id` FK -> AUTORES(`id`)

**USUARIOS**(`id`, nome, email, telefone, ativo)

- `id` PK
- `email` UNIQUE NOT NULL
- `ativo` NOT NULL

**EMPRESTIMOS**(`id`, livro_id, usuario_id, data_emprestimo, data_prevista_devolucao, data_devolucao)

- `id` PK
- `livro_id` FK -> LIVROS(`id`)
- `usuario_id` FK -> USUARIOS(`id`)
- CHECK `data_prevista_devolucao >= data_emprestimo`

## Views

- `vw_livros_disponiveis`: exibe quantidade total e disponivel por livro.
- `vw_emprestimos_abertos`: exibe emprestimos ainda nao devolvidos e situacao.
