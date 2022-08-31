package com.sotska.db;

import com.sotska.entity.QueryCommand;
import com.sotska.entity.ReportData;
import org.apache.commons.lang.StringUtils;

import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.sotska.entity.QueryCommand.*;

public class QueryExecutor {

    private final Connection connection;

    public QueryExecutor(Connection connection) {
        this.connection = connection;
    }

    public ReportData execute(String query) {
        try {
            if (SELECT.equals(getCommandFromQuery(query))) {
                try (var resultSet = connection.createStatement().executeQuery(query)) {
                    return createReportData(resultSet);
                }
            }
            try (var statement = connection.createStatement()) {
                return new ReportData(statement.executeUpdate(query), true);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Can't execute query: " + query, e);
        }
    }

    QueryCommand getCommandFromQuery(String query) {
        return Arrays.stream(QueryCommand.values())
                .filter(queryCommand -> StringUtils.containsIgnoreCase(query, queryCommand.name())).findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Can't recognize query command for query: " + query));
    }

    ReportData createReportData(ResultSet resultSet) throws SQLException {
        var resultSetMetaData = resultSet.getMetaData();
        var columnCount = resultSetMetaData.getColumnCount();
        return new ReportData(getColumnLabels(resultSetMetaData, columnCount),
                getTableValues(resultSet, columnCount), false);
    }

    String[] getColumnLabels(ResultSetMetaData resultSetMetaData, int columnCount) throws SQLException {
        var labels = new String[columnCount];

        for (int columnIndex = 0; columnIndex < columnCount; columnIndex++) {
            labels[columnIndex] = resultSetMetaData.getColumnLabel(columnIndex + 1);
        }
        return labels;
    }

    Object[][] getTableValues(ResultSet resultSet, int columnCount) throws SQLException {
        List<Object[]> data = new ArrayList<>();
        while (resultSet.next()) {
            var arr = new Object[columnCount];
            for (int columnIndex = 0; columnIndex < columnCount; columnIndex++) {
                var value = resultSet.getObject(columnIndex + 1);
                arr[columnIndex] = value;
            }
            data.add(arr);
        }

        var result = new Object[data.size()][columnCount];
        for (int i = 0; i < data.size(); i++) {
            System.arraycopy(data.get(i), 0, result[i], 0, columnCount);
        }
        return result;
    }
}
