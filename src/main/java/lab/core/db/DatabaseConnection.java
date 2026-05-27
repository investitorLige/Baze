package lab.core.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseConnection {
    private static final String DB_HOST = envOrDefault("DB_HOST", "localhost");
    private static final String DB_PORT = envOrDefault("DB_PORT", "3306");
    private static final String DATABASE_NAME = envOrDefault("DB_NAME", "psihologija_lab");
    private static final String SERVER_URL = "jdbc:mysql://" + DB_HOST + ":" + DB_PORT + "/";
    private static final String URL = SERVER_URL + DATABASE_NAME;
    private static final String USERNAME = envOrDefault("DB_USER", "root");
    private static final String PASSWORD = envOrDefault("DB_PASSWORD", "root");
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
            statement.executeUpdate(
                    "CREATE DATABASE IF NOT EXISTS " + DATABASE_NAME + " "
                            + "CHARACTER SET utf8mb4 "
                            + "COLLATE utf8mb4_unicode_ci"
            );
        }

        databaseChecked = true;
    }

    private static String envOrDefault(String name, String defaultValue) {
        String value = System.getenv(name);
        return value == null || value.isBlank() ? defaultValue : value;
    }
}
