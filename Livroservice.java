package br.ucb.biblioteca.service;

import br.ucb.biblioteca.dao.LivroDAO;
import br.ucb.biblioteca.model.Livro;

import java.sql.SQLException;
import java.util.List;

public class LivroService {
    private final LivroDAO dao = new LivroDAO();

    public void salvar(Livro livro) throws SQLException {
        validar(livro);
        if (livro.getId() == 0) {
            dao.inserir(livro);
        } else {
            dao.atualizar(livro);
        }
    }

    public void excluir(int id) throws SQLException {
        dao.excluir(id);
    }

    public List<Livro> listar() throws SQLException {
        return dao.listarTodos();
    }

    private void validar(Livro livro) {
        if (livro.getTitulo() == null || livro.getTitulo().trim().isEmpty()) {
            throw new IllegalArgumentException("Informe o titulo do livro.");
        }
        if (livro.getIsbn() == null || livro.getIsbn().trim().isEmpty()) {
            throw new IllegalArgumentException("Informe o ISBN.");
        }
        if (livro.getQuantidadeTotal() < 0) {
            throw new IllegalArgumentException("A quantidade nao pode ser negativa.");
        }
        if (livro.getEditoraId() <= 0 || livro.getCategoriaId() <= 0 || livro.getAutorId() <= 0) {
            throw new IllegalArgumentException("Selecione editora, categoria e autor.");
        }
    }
}
