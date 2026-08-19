package org.example;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Database {
    private static final String URL = "jdbc:mariadb://127.0.0.1:3306/visitorlog_db";
    private static final String USER = "root";
    private static final String PASSWORD = "2255";

    public static Connection connect() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }
}