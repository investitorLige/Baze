package lab.features.auth.models;

import lab.core.db.DatabaseConnection;
import lab.features.auth.queries.AuthQueries;
import lab.features.researchers.models.ResearcherModel;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class LoginModel {
    public ResearcherModel login(String username, String password) {
        try (Connection connection = DatabaseConnection.getConnection()) {
            AuthSchema.ensureResearchersTableExists(connection);

            try (PreparedStatement statement = connection.prepareStatement(AuthQueries.FIND_RESEARCHER_BY_USERNAME)) {
                statement.setString(1, username);

                try (ResultSet resultSet = statement.executeQuery()) {
                    if (!resultSet.next() || !UserCredentialsFile.contains(username, password)) {
                        return null;
                    }

                    return new ResearcherModel(
                            resultSet.getInt("id_istrazivac"),
                            resultSet.getString("ime"),
                            resultSet.getString("prezime"),
                            resultSet.getString("datum_rodjenja"),
                            resultSet.getString("email"),
                            resultSet.getString("tel"),
                            resultSet.getString("korisnicko_ime"),
                            resultSet.getString("titula"),
                            resultSet.getString("specijalizacija"),
                            resultSet.getObject("godine_iskustva", Integer.class),
                            resultSet.getString("institucija")
                    );
                }
            }
        } catch (SQLException | IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}
