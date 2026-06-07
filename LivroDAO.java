package br.ucb.biblioteca.dao;

import br.ucb.biblioteca.model.Livro;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

public class LivroDAO {
    public void inserir(Livro livro) throws SQLException {
        String sql = "INSERT INTO livros (titulo, isbn, ano_publicacao, quantidade_total, editora_id, categoria_id) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection conn = ConnectionFactory.getConnection()) {
            conn.setAutoCommit(false);
            try {
                try (PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
                    preencherStatement(livro, stmt);
                    stmt.executeUpdate();
                    try (ResultSet keys = stmt.getGeneratedKeys()) {
                        if (keys.next()) {
                            livro.setId(keys.getInt(1));
                        }
                    }
                }
                salvarAutor(conn, livro);
                conn.commit();
            } catch (SQLException e) {
                conn.rollback();
                throw e;
            }
        }
    }

    public void atualizar(Livro livro) throws SQLException {
        String sql = "UPDATE livros SET titulo = ?, isbn = ?, ano_publicacao = ?, quantidade_total = ?, editora_id = ?, categoria_id = ? WHERE id = ?";
        try (Connection conn = ConnectionFactory.getConnection()) {
            conn.setAutoCommit(false);
            try {
                try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                    preencherStatement(livro, stmt);
                    stmt.setInt(7, livro.getId());
                    stmt.executeUpdate();
                }
                excluirAutores(conn, livro.getId());
                salvarAutor(conn, livro);
                conn.commit();
            } catch (SQLException e) {
                conn.rollback();
                throw e;
            }
        }
    }

    private void salvarAutor(Connection conn, Livro livro) throws SQLException {
        if (livro.getAutorId() <= 0) {
            return;
        }
        String sql = "INSERT INTO livro_autor (livro_id, autor_id) VALUES (?, ?)";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, livro.getId());
            stmt.setInt(2, livro.getAutorId());
            stmt.executeUpdate();
        }
    }

    private void excluirAutores(Connection conn, int livroId) throws SQLException {
        String sql = "DELETE FROM livro_autor WHERE livro_id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, livroId);
            stmt.executeUpdate();
        }
    }

    public void excluir(int id) throws SQLException {
        String sql = "DELETE FROM livros WHERE id = ?";
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        }
    }

    public List<Livro> listarTodos() throws SQLException {
        String sql = "SELECT l.id, l.titulo, l.isbn, l.ano_publicacao, l.quantidade_total, l.editora_id, l.categoria_id, " +
                "MIN(a.id) AS autor_id, GROUP_CONCAT(a.nome ORDER BY a.nome SEPARATOR ', ') AS autores_nomes, " +
                "ed.nome AS editora_nome, c.nome AS categoria_nome, COALESCE(v.quantidade_disponivel, l.quantidade_total) AS quantidade_disponivel " +
                "FROM livros l " +
                "JOIN editoras ed ON ed.id = l.editora_id " +
                "JOIN categorias c ON c.id = l.categoria_id " +
                "LEFT JOIN livro_autor la ON la.livro_id = l.id " +
                "LEFT JOIN autores a ON a.id = la.autor_id " +
                "LEFT JOIN vw_livros_disponiveis v ON v.id = l.id " +
                "GROUP BY l.id, l.titulo, l.isbn, l.ano_publicacao, l.quantidade_total, l.editora_id, l.categoria_id, ed.nome, c.nome, v.quantidade_disponivel " +
                "ORDER BY l.titulo";
        List<Livro> livros = new ArrayList<Livro>();
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                livros.add(mapear(rs));
            }
        }
        return livros;
    }

    public int buscarQuantidadeDisponivel(int livroId) throws SQLException {
        String sql = "SELECT quantidade_disponivel FROM vw_livros_disponiveis WHERE id = ?";
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, livroId);
            try (ResultSet rs = stmt.executeQuery()) {
                return rs.next() ? rs.getInt("quantidade_disponivel") : 0;
            }
        }
    }

    private void preencherStatement(Livro livro, PreparedStatement stmt) throws SQLException {
        stmt.setString(1, livro.getTitulo());
        stmt.setString(2, livro.getIsbn());
        if (livro.getAnoPublicacao() == null) {
            stmt.setNull(3, Types.INTEGER);
        } else {
            stmt.setInt(3, livro.getAnoPublicacao());
        }
        stmt.setInt(4, livro.getQuantidadeTotal());
        stmt.setInt(5, livro.getEditoraId());
        stmt.setInt(6, livro.getCategoriaId());
    }

    private Livro mapear(ResultSet rs) throws SQLException {
        Livro livro = new Livro();
        livro.setId(rs.getInt("id"));
        livro.setTitulo(rs.getString("titulo"));
        livro.setIsbn(rs.getString("isbn"));
        int ano = rs.getInt("ano_publicacao");
        livro.setAnoPublicacao(rs.wasNull() ? null : ano);
        livro.setQuantidadeTotal(rs.getInt("quantidade_total"));
        livro.setEditoraId(rs.getInt("editora_id"));
        livro.setCategoriaId(rs.getInt("categoria_id"));
        livro.setAutorId(rs.getInt("autor_id"));
        livro.setEditoraNome(rs.getString("editora_nome"));
        livro.setCategoriaNome(rs.getString("categoria_nome"));
        livro.setAutoresNomes(rs.getString("autores_nomes"));
        livro.setQuantidadeDisponivel(rs.getInt("quantidade_disponivel"));
        return livro;
    }
}
