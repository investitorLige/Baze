package lab.features.auth.models;

import lab.core.db.DatabaseSchema;

import java.sql.Connection;
import java.sql.SQLException;

final class AuthSchema {
    private AuthSchema() {
    }

    static void ensureResearchersTableExists(Connection connection) throws SQLException {
        DatabaseSchema.ensureProjectSchemaExists(connection);
    }
}
