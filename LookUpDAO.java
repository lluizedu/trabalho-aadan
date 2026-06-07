package br.ucb.biblioteca.dao;

import br.ucb.biblioteca.model.Item;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class LookupDAO {
    public List<Item> listarEditoras() throws SQLException {
        return listar("SELECT id, nome FROM editoras ORDER BY nome");
    }

    public List<Item> listarCategorias() throws SQLException {
        return listar("SELECT id, nome FROM categorias ORDER BY nome");
    }

    public List<Item> listarAutores() throws SQLException {
        return listar("SELECT id, nome FROM autores ORDER BY nome");
    }

    private List<Item> listar(String sql) throws SQLException {
        List<Item> itens = new ArrayList<Item>();
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                itens.add(new Item(rs.getInt("id"), rs.getString("nome")));
            }
        }
        return itens;
    }
}
