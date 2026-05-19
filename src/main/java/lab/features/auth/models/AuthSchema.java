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

    static void ensureResearchersTableExists(Connection connection) throws SQLException {
        try (Statement statement = connection.createStatement()) {
            statement.executeUpdate(AuthQueries.CREATE_RESEARCHERS_TABLE);
        }

        ensureColumnExists(connection, "ime", "VARCHAR(100) NOT NULL DEFAULT ''");
        ensureColumnExists(connection, "prezime", "VARCHAR(100) NOT NULL DEFAULT ''");
        ensureColumnExists(connection, "datum_rodjenja", "DATE NOT NULL DEFAULT '1970-01-01'");
        ensureColumnExists(connection, "email", "VARCHAR(150) NOT NULL DEFAULT ''");
        ensureColumnExists(connection, "tel", "VARCHAR(50) NULL");
        ensureColumnExists(connection, "korisnicko_ime", "VARCHAR(100) NOT NULL DEFAULT ''");
        ensureColumnExists(connection, "password", "VARCHAR(100) NOT NULL DEFAULT ''");
        ensureColumnExists(connection, "titula", "VARCHAR(100) NULL");
        ensureColumnExists(connection, "specijalizacija", "VARCHAR(255) NULL");
        ensureColumnExists(connection, "godine_iskustva", "INT NULL");
        ensureColumnExists(connection, "institucija", "VARCHAR(255) NULL");

        allowNullForOptionalColumns(connection);
    }

    private static void ensureColumnExists(Connection connection, String columnName, String definition) throws SQLException {
        DatabaseMetaData metaData = connection.getMetaData();

        try (ResultSet columns = metaData.getColumns(null, null, "ISTRAZIVAC", columnName)) {
            if (columns.next()) {
                return;
            }
        }

        try (Statement statement = connection.createStatement()) {
            statement.executeUpdate("ALTER TABLE ISTRAZIVAC ADD COLUMN " + columnName + " " + definition);
        }
    }

    private static void allowNullForOptionalColumns(Connection connection) throws SQLException {
        try (Statement statement = connection.createStatement()) {
            statement.executeUpdate("ALTER TABLE ISTRAZIVAC MODIFY COLUMN tel VARCHAR(50) NULL");
            statement.executeUpdate("ALTER TABLE ISTRAZIVAC MODIFY COLUMN titula VARCHAR(100) NULL");
            statement.executeUpdate("ALTER TABLE ISTRAZIVAC MODIFY COLUMN specijalizacija VARCHAR(255) NULL");
            statement.executeUpdate("ALTER TABLE ISTRAZIVAC MODIFY COLUMN godine_iskustva INT NULL");
            statement.executeUpdate("ALTER TABLE ISTRAZIVAC MODIFY COLUMN institucija VARCHAR(255) NULL");
        }
    }
}
