package br.ucb.biblioteca.service;

import br.ucb.biblioteca.dao.UsuarioDAO;
import br.ucb.biblioteca.model.Usuario;

import java.sql.SQLException;
import java.util.List;

public class UsuarioService {
    private final UsuarioDAO dao = new UsuarioDAO();

    public void salvar(Usuario usuario) throws SQLException {
        validar(usuario);
        if (usuario.getId() == 0) {
            dao.inserir(usuario);
        } else {
            dao.atualizar(usuario);
        }
    }

    public void excluir(int id) throws SQLException {
        dao.excluir(id);
    }

    public List<Usuario> listar() throws SQLException {
        return dao.listarTodos();
    }

    private void validar(Usuario usuario) {
        if (usuario.getNome() == null || usuario.getNome().trim().isEmpty()) {
            throw new IllegalArgumentException("Informe o nome do usuario.");
        }
        if (usuario.getEmail() == null || !usuario.getEmail().contains("@")) {
            throw new IllegalArgumentException("Informe um email valido.");
        }
    }
}
