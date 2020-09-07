package org.example.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class Database {
    private static Connection connection;

    public static Connection getConnection() {
        if (connection == null) {
            try {
                // db parameters
                String filePath = System.getProperty("user.home")+System.getProperty("file.separator")+"user-management.db";
                String url = "jdbc:sqlite:" + filePath;
                // create a connection to the database

                Class.forName("org.sqlite.JDBC");
                connection = DriverManager.getConnection(url);
                System.out.println("Connection to SQLite has been established.");

                createTable();

            } catch (SQLException | ClassNotFoundException e) {
                System.out.println(e.getMessage());
            }
        }
        return connection;
    }

    public static void createTable() throws SQLException {
        Statement stmt = connection.createStatement();
        String statement = "CREATE TABLE IF NOT EXISTS customers " +
                "(_id INTEGER PRIMARY KEY NOT NULL , " +
                "status VARCHAR(255), " +
                "name VARCHAR(255), " +
                "image_url VARCHAR(255), " +
                "class VARCHAR(255), " +
                "age INTEGER, " +
                "phone_number VARCHAR(255), " +
                "location VARCHAR(255), " +
                "stage VARCHAR(255), " +
                "result VARCHAR(255), " +
                "level_price INTEGER, " +
                "discount INTEGER, " +
                "cost INTEGER, " +
                "comment VARCHAR(255))";
        stmt.executeUpdate(statement);

        statement = "CREATE TABLE IF NOT EXISTS users " +
                "(_id INTEGER PRIMARY KEY NOT NULL , " +
                "user_name VARCHAR(255), " +
                "password VARCHAR(255), " +
                "is_logged_in BOOLEAN ) ";
        stmt.execute(statement);
        stmt.close();
    }
}