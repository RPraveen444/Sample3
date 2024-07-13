package org.example.connection;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class TestDatabaseConnection {
    private static final String URL = "jdbc:mysql://localhost:3306/testrevtaskmanager"; // Use a test database
    private static final String USERNAME = "root";
    private static final String PASSWORD = "praveen444";

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USERNAME, PASSWORD);
    }
}

