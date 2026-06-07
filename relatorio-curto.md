# Relatorio Curto - Gestao de Biblioteca

## Objetivo

Desenvolver um sistema CRUD para gestao de biblioteca, permitindo controlar livros, usuarios e emprestimos com integridade referencial no banco de dados e interface grafica em Java Swing.

## Escopo

O sistema contempla:

- cadastro, consulta, edicao e exclusao de livros;
- cadastro, consulta, edicao e exclusao de usuarios;
- registro de emprestimos;
- registro de devolucoes;
- relatorio de emprestimos em aberto.

## Entidades

- **Editoras**: empresas responsaveis pela publicacao dos livros.
- **Categorias**: classificacao dos livros por area.
- **Autores**: escritores dos livros.
- **Livros**: acervo da biblioteca.
- **Livro_Autor**: tabela associativa que resolve o relacionamento N:N entre livros e autores.
- **Usuarios**: pessoas que podem pegar livros emprestados.
- **Emprestimos**: registros de retirada e devolucao dos livros.

## Regras de negocio

1. O livro nao pode ter quantidade total negativa.
2. O email do usuario deve ser unico.
3. A data prevista de devolucao nao pode ser anterior a data do emprestimo.
4. A aplicacao nao permite registrar emprestimo quando nao ha exemplares disponiveis.

## Arquitetura

O projeto foi separado em camadas:

- `model`: classes de dominio.
- `dao`: acesso ao banco com JDBC, Connection e PreparedStatement.
- `service`: validacoes e regras de negocio.
- `ui`: telas Java Swing.

## Consulta/relatorio

A view `vw_emprestimos_abertos` lista emprestimos sem devolucao registrada e informa a situacao do prazo: `EM DIA` ou `ATRASADO`.
