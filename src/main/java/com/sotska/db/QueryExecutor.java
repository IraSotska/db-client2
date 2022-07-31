package com.sotska.db;

import com.sotska.entity.QueryCommand;
import com.sotska.entity.ReportData;
import org.apache.commons.lang.StringUtils;

import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.sotska.entity.QueryCommand.*;

public class QueryExecutor implements AutoCloseable {

    private final Connection connection;

    public QueryExecutor(Connection connection) {
        this.connection = connection;
    }

    public ReportData execute(String query) {
        var command = Arrays.stream(QueryCommand.values())
                .filter(queryCommand -> StringUtils.containsIgnoreCase(query, queryCommand.name())).findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Can't recognize query command for query: " + query));
        try {
            if (SELECT.equals(command)) {
                try (var resultSet = connection.createStatement().executeQuery(query)) {
                    return createReportData(resultSet);
                }
            }
            return new ReportData(connection.createStatement().executeUpdate(query), true);
        } catch (SQLException e) {
            throw new RuntimeException("Can't execute query: " + query, e);
        }
    }

    ReportData createReportData(ResultSet resultSet) throws SQLException {
        var resultSetMetaData = resultSet.getMetaData();

        return new ReportData(getColumnLabels(resultSetMetaData),
                getTableValues(resultSet, resultSetMetaData.getColumnCount()), false);
    }

    String[] getColumnLabels(ResultSetMetaData resultSetMetaData) throws SQLException {
        var columnCount = resultSetMetaData.getColumnCount();
        var labels = new String[columnCount];

        for (int i = 0; i < columnCount; i++) {
            labels[i] = resultSetMetaData.getColumnLabel(i + 1);
        }
        return labels;
    }

    Object[][] getTableValues(ResultSet resultSet, int columnCount) throws SQLException {
        List<Object[]> data = new ArrayList<>();
        while (resultSet.next()) {
            var arr = new Object[columnCount];
            for (int i = 0; i < columnCount; i++) {
                var value = resultSet.getObject(i + 1);
                arr[i] = value;
            }
            data.add(arr);
        }

        var result = new Object[data.size()][columnCount];
        for (int i = 0; i < data.size(); i++) {
            System.arraycopy(data.get(i), 0, result[i], 0, columnCount);
        }
        return result;
    }

    @Override
    public void close() throws Exception {
        connection.close();
    }
}
