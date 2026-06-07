package br.ucb.biblioteca.service;

import br.ucb.biblioteca.dao.EmprestimoDAO;
import br.ucb.biblioteca.dao.LivroDAO;
import br.ucb.biblioteca.model.Emprestimo;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

public class EmprestimoService {
    private final EmprestimoDAO emprestimoDAO = new EmprestimoDAO();
    private final LivroDAO livroDAO = new LivroDAO();

    public void registrar(Emprestimo emprestimo) throws SQLException {
        validar(emprestimo);
        if (livroDAO.buscarQuantidadeDisponivel(emprestimo.getLivroId()) <= 0) {
            throw new IllegalArgumentException("Nao ha exemplares disponiveis para este livro.");
        }
        emprestimoDAO.registrar(emprestimo);
    }

    public void devolver(int id) throws SQLException {
        emprestimoDAO.devolver(id);
    }

    public void excluir(int id) throws SQLException {
        emprestimoDAO.excluir(id);
    }

    public List<Emprestimo> listar() throws SQLException {
        return emprestimoDAO.listarTodos();
    }

    public List<String[]> listarAbertosRelatorio() throws SQLException {
        return emprestimoDAO.listarAbertosRelatorio();
    }

    private void validar(Emprestimo emprestimo) {
        if (emprestimo.getLivroId() <= 0 || emprestimo.getUsuarioId() <= 0) {
            throw new IllegalArgumentException("Selecione livro e usuario.");
        }
        LocalDate inicio = emprestimo.getDataEmprestimo();
        LocalDate prevista = emprestimo.getDataPrevistaDevolucao();
        if (inicio == null || prevista == null) {
            throw new IllegalArgumentException("Informe as datas do emprestimo.");
        }
        if (prevista.isBefore(inicio)) {
            throw new IllegalArgumentException("A devolucao prevista nao pode ser anterior ao emprestimo.");
        }
    }
}
