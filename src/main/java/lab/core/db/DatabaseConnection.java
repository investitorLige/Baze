package lab.core.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseConnection {
    private static final String SERVER_URL = "jdbc:mysql://localhost:3306/";
    private static final String DATABASE_NAME = "psihologija_lab";
    private static final String URL = SERVER_URL + DATABASE_NAME;
    private static final String USERNAME = "root";
    private static final String PASSWORD = "root";
    private static boolean databaseChecked;

    private DatabaseConnection() {
    }

    public static Connection getConnection() throws SQLException {
        ensureDatabaseExists();
        return DriverManager.getConnection(URL, USERNAME, PASSWORD);
    }

    private static synchronized void ensureDatabaseExists() throws SQLException {
        if (databaseChecked) {
            return;
        }

        try (Connection connection = DriverManager.getConnection(SERVER_URL, USERNAME, PASSWORD);
             Statement statement = connection.createStatement()) {
            statement.executeUpdate("""
                    CREATE DATABASE IF NOT EXISTS psihologija_lab
                        CHARACTER SET utf8mb4
                        COLLATE utf8mb4_unicode_ci
                    """);
        }

        databaseChecked = true;
    }
}
