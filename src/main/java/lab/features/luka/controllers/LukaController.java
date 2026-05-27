package lab.features.luka.controllers;

import lab.core.db.DatabaseConnection;
import lab.core.db.DatabaseSchema;
import lab.features.luka.queries.LukaQueries;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class LukaController {
    public List<Object[]> getResearcherLoadReport() {
        List<Object[]> rows = new ArrayList<>();

        try (Connection connection = DatabaseConnection.getConnection()) {
            DatabaseSchema.ensureProjectSchemaExists(connection);

            try (Statement statement = connection.createStatement();
                 ResultSet resultSet = statement.executeQuery(LukaQueries.RESEARCHER_LOAD_REPORT)) {
                rows.addAll(readRows(resultSet));
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
