package lab.features.auth.models;

import lab.core.db.DatabaseConnection;
import lab.features.auth.queries.AuthQueries;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class RegisterModel {
    public boolean register(String firstName, String lastName, String username, String role, String password) {
        try (Connection connection = DatabaseConnection.getConnection()) {
            AuthSchema.ensureUsersTableExists(connection);
            connection.setAutoCommit(false);

            try (PreparedStatement statement = connection.prepareStatement(AuthQueries.REGISTER_USER)) {
                statement.setString(1, firstName);
                statement.setString(2, lastName);
                statement.setString(3, username);
                statement.setString(4, role);
                statement.setString(5, password);

                int insertedRows = statement.executeUpdate();
                if (insertedRows == 1) {
                    UserCredentialsFile.append(username, password);
                    connection.commit();
                    return true;
                }

                connection.rollback();
                return false;
            } catch (SQLException | IOException e) {
                connection.rollback();
                e.printStackTrace();
                return false;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}
