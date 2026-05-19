package lab.features.auth.models;

import lab.core.db.DatabaseConnection;
import lab.features.auth.queries.AuthQueries;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class LoginModel {
    public boolean login(String username, String password) {
        try (Connection connection = DatabaseConnection.getConnection()) {
            AuthSchema.ensureResearchersTableExists(connection);

            try (PreparedStatement statement = connection.prepareStatement(AuthQueries.LOGIN_RESEARCHER)) {
                statement.setString(1, username);
                statement.setString(2, password);

                try (ResultSet resultSet = statement.executeQuery()) {
                    return resultSet.next();
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}
