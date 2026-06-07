package br.ucb.biblioteca.dao;

import br.ucb.biblioteca.model.Emprestimo;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class EmprestimoDAO {
    public void registrar(Emprestimo emprestimo) throws SQLException {
        String sql = "INSERT INTO emprestimos (livro_id, usuario_id, data_emprestimo, data_prevista_devolucao) VALUES (?, ?, ?, ?)";
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, emprestimo.getLivroId());
            stmt.setInt(2, emprestimo.getUsuarioId());
            stmt.setDate(3, Date.valueOf(emprestimo.getDataEmprestimo()));
            stmt.setDate(4, Date.valueOf(emprestimo.getDataPrevistaDevolucao()));
            stmt.executeUpdate();
        }
    }

    public void devolver(int id) throws SQLException {
        String sql = "UPDATE emprestimos SET data_devolucao = CURRENT_DATE WHERE id = ? AND data_devolucao IS NULL";
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        }
    }

    public void excluir(int id) throws SQLException {
        String sql = "DELETE FROM emprestimos WHERE id = ?";
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        }
    }

    public List<Emprestimo> listarTodos() throws SQLException {
        String sql = "SELECT e.id, e.livro_id, e.usuario_id, l.titulo AS livro_titulo, u.nome AS usuario_nome, " +
                "e.data_emprestimo, e.data_prevista_devolucao, e.data_devolucao " +
                "FROM emprestimos e JOIN livros l ON l.id = e.livro_id JOIN usuarios u ON u.id = e.usuario_id " +
                "ORDER BY e.id DESC";
        List<Emprestimo> emprestimos = new ArrayList<Emprestimo>();
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                Emprestimo emprestimo = new Emprestimo();
                emprestimo.setId(rs.getInt("id"));
                emprestimo.setLivroId(rs.getInt("livro_id"));
                emprestimo.setUsuarioId(rs.getInt("usuario_id"));
                emprestimo.setLivroTitulo(rs.getString("livro_titulo"));
                emprestimo.setUsuarioNome(rs.getString("usuario_nome"));
                emprestimo.setDataEmprestimo(rs.getDate("data_emprestimo").toLocalDate());
                emprestimo.setDataPrevistaDevolucao(rs.getDate("data_prevista_devolucao").toLocalDate());
                Date devolucao = rs.getDate("data_devolucao");
                emprestimo.setDataDevolucao(devolucao == null ? null : devolucao.toLocalDate());
                emprestimos.add(emprestimo);
            }
        }
        return emprestimos;
    }

    public List<String[]> listarAbertosRelatorio() throws SQLException {
        String sql = "SELECT id, livro, usuario, data_emprestimo, data_prevista_devolucao, situacao FROM vw_emprestimos_abertos ORDER BY data_prevista_devolucao";
        List<String[]> linhas = new ArrayList<String[]>();
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                linhas.add(new String[] {
                        String.valueOf(rs.getInt("id")),
                        rs.getString("livro"),
                        rs.getString("usuario"),
                        String.valueOf(rs.getDate("data_emprestimo")),
                        String.valueOf(rs.getDate("data_prevista_devolucao")),
                        rs.getString("situacao")
                });
            }
        }
        return linhas;
    }
}
