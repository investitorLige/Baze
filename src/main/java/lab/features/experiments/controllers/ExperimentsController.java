package lab.features.experiments.controllers;

import lab.core.db.DatabaseConnection;
import lab.core.db.DatabaseSchema;
import lab.core.session.UserSession;
import lab.features.experiments.queries.ExperimentQueries;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class ExperimentsController {
    public List<Object[]> findLaboratories() {
        return queryRows(ExperimentQueries.FIND_LABORATORIES);
    }

    public List<Object[]> findExperiments() {
        return queryRows(ExperimentQueries.FIND_EXPERIMENTS);
    }

    public List<Object[]> findExecutions() {
        return queryRows(ExperimentQueries.FIND_EXECUTIONS);
    }

    public List<Object[]> findExecutionsByLab(int labId) {
        return queryRows(ExperimentQueries.FIND_EXECUTIONS_BY_LAB, labId);
    }

    public List<Object[]> findExecutionsByExperiment(int experimentId) {
        return queryRows(ExperimentQueries.FIND_EXECUTIONS_BY_EXPERIMENT, experimentId);
    }

    public List<Object[]> findExecutionsByFilters(Integer labId, Integer experimentId) {
        return queryRows(ExperimentQueries.FIND_EXECUTIONS_BY_FILTERS, labId, labId, experimentId, experimentId);
    }

    public List<Object[]> findSessionsByExecution(int executionId) {
        return queryRows(ExperimentQueries.FIND_SESSIONS_BY_EXECUTION, executionId);
    }

    public List<Object[]> findTheoryByExperiment(int experimentId) {
        return queryRows(ExperimentQueries.FIND_THEORY_BY_EXPERIMENT, experimentId);
    }

    public List<Object[]> findProtocolByExperiment(int experimentId) {
        return queryRows(ExperimentQueries.FIND_PROTOCOL_BY_EXPERIMENT, experimentId);
    }

    public List<Object[]> findInventoryByLab(int labId) {
        return queryRows(ExperimentQueries.FIND_INVENTORY_BY_LAB, labId);
    }

    public List<Object[]> findToolsByLab(int labId) {
        return queryRows(ExperimentQueries.FIND_TOOLS_BY_LAB, labId);
    }

    public List<Object[]> findQuestionnairesByExperiment(int experimentId) {
        return queryRows(ExperimentQueries.FIND_QUESTIONNAIRES_BY_EXPERIMENT, experimentId);
    }

    public List<Object[]> findRequiredResourcesByExperiment(int experimentId) {
        return queryRows(ExperimentQueries.FIND_REQUIRED_RESOURCES_BY_EXPERIMENT, experimentId);
    }

    public List<Object[]> findRequiredToolsByExperiment(int experimentId) {
        return queryRows(ExperimentQueries.FIND_REQUIRED_TOOLS_BY_EXPERIMENT, experimentId);
    }

    public List<Object[]> findApprovalsByExperiment(int experimentId) {
        return queryRows(ExperimentQueries.FIND_APPROVALS_BY_EXPERIMENT, experimentId);
    }

    public List<Object[]> findDesignersByExperiment(int experimentId) {
        return queryRows(ExperimentQueries.FIND_DESIGNERS_BY_EXPERIMENT, experimentId);
    }

    public List<Object[]> findTeamByExecution(int executionId) {
        return queryRows(ExperimentQueries.FIND_TEAM_BY_EXECUTION, executionId);
    }

    public List<Object[]> findParticipationBySession(int sessionId) {
        return queryRows(ExperimentQueries.FIND_PARTICIPATION_BY_SESSION, sessionId);
    }

    public List<Object[]> findResultsBySession(int sessionId) {
        return queryRows(ExperimentQueries.FIND_RESULTS_BY_SESSION, sessionId);
    }

    public List<Object[]> findUsedResourcesBySession(int sessionId) {
        return queryRows(ExperimentQueries.FIND_USED_RESOURCES_BY_SESSION, sessionId);
    }

    public List<Object[]> findUsedToolsBySession(int sessionId) {
        return queryRows(ExperimentQueries.FIND_USED_TOOLS_BY_SESSION, sessionId);
    }

    public boolean updateExecutionStatus(int executionId, String status) {
        int researcherId = UserSession.getCurrentResearcherId();

        try (Connection connection = DatabaseConnection.getConnection()) {
            DatabaseSchema.ensureProjectSchemaExists(connection);

            try (PreparedStatement statement = connection.prepareStatement(ExperimentQueries.UPDATE_EXECUTION_STATUS)) {
                statement.setString(1, status);
                statement.setInt(2, executionId);
                statement.setInt(3, researcherId);
                statement.setInt(4, researcherId);
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

            try (PreparedStatement statement = connection.prepareStatement(ExperimentQueries.DELETE_SESSION)) {
                statement.setInt(1, sessionId);
                return statement.executeUpdate() == 1;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public List<Object[]> getExecutionReport(int executionId) {
        return queryRows(ExperimentQueries.EXECUTION_REPORT, executionId);
    }

    private boolean canDeleteSession(Connection connection, int sessionId, int researcherId) throws SQLException {
        try (PreparedStatement statement = connection.prepareStatement(ExperimentQueries.CAN_DELETE_SESSION)) {
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

            if (parameters.length == 0) {
                try (Statement statement = connection.createStatement();
                     ResultSet resultSet = statement.executeQuery(sql)) {
                    rows.addAll(readRows(resultSet));
                }
            } else {
                try (PreparedStatement statement = connection.prepareStatement(sql)) {
                    for (int i = 0; i < parameters.length; i++) {
                        statement.setObject(i + 1, parameters[i]);
                    }

                    try (ResultSet resultSet = statement.executeQuery()) {
                        rows.addAll(readRows(resultSet));
                    }
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
