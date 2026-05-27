package lab.features.sessions.controllers;

import lab.core.db.DatabaseConnection;
import lab.core.db.DatabaseSchema;
import lab.core.session.UserSession;
import lab.features.sessions.queries.SessionQueries;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class SessionsController {
    public List<Object[]> findSessionsByExecution(int executionId) {
        return queryRows(SessionQueries.FIND_SESSIONS_BY_EXECUTION, executionId);
    }

    public List<Object[]> findParticipationBySession(int sessionId) {
        return queryRows(SessionQueries.FIND_PARTICIPATION_BY_SESSION, sessionId);
    }

    public List<Object[]> findResultsBySession(int sessionId) {
        return queryRows(SessionQueries.FIND_RESULTS_BY_SESSION, sessionId);
    }

    public List<Object[]> findUsedResourcesBySession(int sessionId) {
        return queryRows(SessionQueries.FIND_USED_RESOURCES_BY_SESSION, sessionId);
    }

    public List<Object[]> findUsedToolsBySession(int sessionId) {
        return queryRows(SessionQueries.FIND_USED_TOOLS_BY_SESSION, sessionId);
    }

    public boolean updateSessionStatus(int sessionId, String status) {
        int researcherId = UserSession.getCurrentResearcherId();

        try (Connection connection = DatabaseConnection.getConnection()) {
            DatabaseSchema.ensureProjectSchemaExists(connection);

            try (PreparedStatement statement = connection.prepareStatement(SessionQueries.UPDATE_SESSION_STATUS)) {
                statement.setString(1, status);
                statement.setInt(2, sessionId);
                statement.setInt(3, researcherId);
                return statement.executeUpdate() == 1;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean deleteSession(int sessionId) {
        int researcherId = UserSession.getCurrentResearcherId();

        try (Connection connection = DatabaseConnection.getConnection()) {
            DatabaseSchema.ensureProjectSchemaExists(connection);

            if (!canDeleteSession(connection, sessionId, researcherId)) {
                return false;
            }

            try (PreparedStatement statement = connection.prepareStatement(SessionQueries.DELETE_SESSION)) {
                statement.setInt(1, sessionId);
                return statement.executeUpdate() == 1;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    private boolean canDeleteSession(Connection connection, int sessionId, int researcherId) throws SQLException {
        try (PreparedStatement statement = connection.prepareStatement(SessionQueries.CAN_DELETE_SESSION)) {
            statement.setInt(1, sessionId);
            statement.setInt(2, researcherId);

            try (ResultSet resultSet = statement.executeQuery()) {
                return resultSet.next() && resultSet.getInt("dozvoljeno") > 0;
            }
        }
    }

    private List<Object[]> queryRows(String sql, Object... parameters) {
        List<Object[]> rows = new ArrayList<>();

        try (Connection connection = DatabaseConnection.getConnection()) {
            DatabaseSchema.ensureProjectSchemaExists(connection);

            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                for (int i = 0; i < parameters.length; i++) {
                    statement.setObject(i + 1, parameters[i]);
                }

                try (ResultSet resultSet = statement.executeQuery()) {
                    rows.addAll(readRows(resultSet));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return rows;
    }

    private List<Object[]> readRows(ResultSet resultSet) throws SQLException {
        List<Object[]> rows = new ArrayList<>();
        ResultSetMetaData metaData = resultSet.getMetaData();
        int columnCount = metaData.getColumnCount();

        while (resultSet.next()) {
            Object[] row = new Object[columnCount];
            for (int i = 0; i < columnCount; i++) {
                row[i] = resultSet.getObject(i + 1);
            }
            rows.add(row);
        }

        return rows;
    }
}
