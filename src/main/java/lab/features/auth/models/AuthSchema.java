package lab.features.auth.models;

import lab.features.auth.queries.AuthQueries;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

final class AuthSchema {
    private AuthSchema() {
    }

    static void ensureUsersTableExists(Connection connection) throws SQLException {
        try (Statement statement = connection.createStatement()) {
            statement.executeUpdate(AuthQueries.CREATE_USERS_TABLE);
        }

        ensureColumnExists(connection, "first_name", "VARCHAR(100) NOT NULL DEFAULT ''");
        ensureColumnExists(connection, "last_name", "VARCHAR(100) NOT NULL DEFAULT ''");
        ensureColumnExists(connection, "user_role", "VARCHAR(50) NOT NULL DEFAULT 'External user'");
    }

    private static void ensureColumnExists(Connection connection, String columnName, String definition) throws SQLException {
        DatabaseMetaData metaData = connection.getMetaData();

        try (ResultSet columns = metaData.getColumns(null, null, "users", columnName)) {
            if (columns.next()) {
                return;
            }
        }

        try (Statement statement = connection.createStatement()) {
            statement.executeUpdate("ALTER TABLE users ADD COLUMN " + columnName + " " + definition);
        }
    }
}
