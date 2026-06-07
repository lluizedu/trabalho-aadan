package br.ucb.biblioteca.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectionFactory {
    private static final String DEFAULT_URL = "jdbc:mysql://localhost:3306/biblioteca_crud?useSSL=false&serverTimezone=America/Sao_Paulo";
    private static final String DEFAULT_USER = "root";
    private static final String DEFAULT_PASSWORD = "";

    public static Connection getConnection() throws SQLException {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            throw new SQLException("Driver MySQL nao encontrado. Adicione o MySQL Connector/J na pasta lib.", e);
        }

        String url = getEnvOrDefault("BIBLIOTECA_DB_URL", DEFAULT_URL);
        String user = getEnvOrDefault("BIBLIOTECA_DB_USER", DEFAULT_USER);
        String password = getEnvOrDefault("BIBLIOTECA_DB_PASSWORD", DEFAULT_PASSWORD);
        return DriverManager.getConnection(url, user, password);
    }

    private static String getEnvOrDefault(String name, String defaultValue) {
        String value = System.getenv(name);
        return value == null || value.trim().isEmpty() ? defaultValue : value;
    }
}
