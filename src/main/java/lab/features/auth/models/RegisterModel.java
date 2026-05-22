package lab.features.auth.models;

import lab.core.db.DatabaseConnection;
import lab.features.auth.queries.AuthQueries;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Types;

public class RegisterModel {
    public boolean register(
            String ime,
            String prezime,
            String datumRodjenja,
            String email,
            String tel,
            String korisnickoIme,
            String titula,
            String specijalizacija,
            Integer godineIskustva,
            String institucija,
            String password
    ) {
        try (Connection connection = DatabaseConnection.getConnection()) {
            AuthSchema.ensureResearchersTableExists(connection);
            connection.setAutoCommit(false);

            try (PreparedStatement statement = connection.prepareStatement(AuthQueries.REGISTER_RESEARCHER)) {
                statement.setString(1, ime);
                statement.setString(2, prezime);
                statement.setString(3, datumRodjenja);
                statement.setString(4, email);
                statement.setString(5, tel);
                statement.setString(6, korisnickoIme);
                statement.setString(7, titula);
                statement.setString(8, specijalizacija);
                if (godineIskustva == null) {
                    statement.setNull(9, Types.INTEGER);
                } else {
                    statement.setInt(9, godineIskustva);
                }
                statement.setString(10, institucija);

                int insertedRows = statement.executeUpdate();
                if (insertedRows == 1) {
                    UserCredentialsFile.append(korisnickoIme, password);
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
