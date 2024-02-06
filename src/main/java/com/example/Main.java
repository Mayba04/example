package com.example;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class Main {
  public static void main(String[] args) {
        String url = "jdbc:postgresql://localhost:5432/test1";
        String user = "postgres";
        String password = "123456";

        try {
            // Завантаження драйвера PostgreSQL
            Class.forName("org.postgresql.Driver");

            // Встановлення підключення до бази даних
            try (Connection connection = DriverManager.getConnection(url, user, password)) {
                createTable(connection);
                insertData(connection);
                displayTableContents(connection);
            }
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
    }

    private static void createTable(Connection connection) throws SQLException {
        String createTableSQL = "CREATE TABLE IF NOT EXISTS categories (id SERIAL PRIMARY KEY, name VARCHAR(255) UNIQUE)";
        try (Statement statement = connection.createStatement()) {
            statement.execute(createTableSQL);
        }
    }

    private static void insertData(Connection connection) throws SQLException {
        String insertDataSQL = "INSERT INTO categories (name) VALUES (?) ON CONFLICT (name) DO NOTHING";
        try (PreparedStatement preparedStatement = connection.prepareStatement(insertDataSQL)) {
            preparedStatement.setString(1, "Category 1");
            preparedStatement.addBatch();
    
            preparedStatement.setString(1, "Category 2");
            preparedStatement.addBatch();
    
            preparedStatement.setString(1, "Category 3");
            preparedStatement.addBatch();
    
            preparedStatement.executeBatch();
        }
    }

    private static void displayTableContents(Connection connection) throws SQLException {
        String selectDataSQL = "SELECT * FROM categories";
        try (Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(selectDataSQL)) {

            System.out.println("Table Contents:");
            System.out.println("ID\tName");

            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String name = resultSet.getString("name");
                System.out.println(id + "\t" + name);
            }
        }
    }
}