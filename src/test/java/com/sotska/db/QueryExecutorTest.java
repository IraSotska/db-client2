package com.sotska.db;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.sql.*;

import static com.sotska.entity.QueryCommand.SELECT;
import static com.sotska.entity.QueryCommand.UPDATE;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class QueryExecutorTest {

    private Connection connection = mock(Connection.class);
    private QueryExecutor queryExecutor = new QueryExecutor(connection);

    @DisplayName("Should Execute Query If Select")
    @Test
    void shouldExecuteQueryIfSelect() throws SQLException {
        var query = "select * from test;";
        var resultSet = mock(ResultSet.class);
        var metadata = mock(ResultSetMetaData.class);
        var statement = mock(Statement.class);

        when(connection.createStatement()).thenReturn(statement);
        when(statement.executeQuery(query)).thenReturn(resultSet);
        when(resultSet.getMetaData()).thenReturn(metadata);
        when(metadata.getColumnCount()).thenReturn(2);
        when(metadata.getColumnLabel(1)).thenReturn("label1");
        when(metadata.getColumnLabel(2)).thenReturn("label2");
        when(resultSet.next()).thenReturn(true).thenReturn(false);
        when(resultSet.getObject(1)).thenReturn("val1");
        when(resultSet.getObject(2)).thenReturn("val2");

        var result = queryExecutor.execute(query);

        assertArrayEquals(new String[]{"label1", "label2"}, result.getColumnNames());
        assertArrayEquals(new Object[][]{new Object[]{"val1", "val2"}}, result.getData());

        verify(connection).createStatement();
        verify(statement).executeQuery(query);
        verify(resultSet).getMetaData();
        verify(metadata).getColumnCount();
        verify(metadata).getColumnLabel(1);
        verify(metadata).getColumnLabel(2);
        verify(resultSet, times(2)).next();
        verify(resultSet).getObject(1);
        verify(resultSet).getObject(2);

        verifyNoMoreInteractions(connection, statement, metadata);
    }

    @DisplayName("Should Execute Query If Update")
    @Test
    void shouldExecuteQueryIfUpdate() throws SQLException {
        var query = "Update test SET (name = 'new') WHERE id = 1;";
        var statement = mock(Statement.class);

        when(connection.createStatement()).thenReturn(statement);
        when(statement.executeUpdate(query)).thenReturn(1);

        var result = queryExecutor.execute(query);

        assertTrue(result.isUpdate());
        assertEquals(1, result.getUpdatedRows());

        verify(connection).createStatement();
        verify(statement).executeUpdate(query);

        verifyNoMoreInteractions(connection, statement);
    }

    @DisplayName("Should Get Columns Names")
    @Test
    void shouldGetColumnsNames() throws SQLException {
        var resultSetMetaData = mock(ResultSetMetaData.class);

        when(resultSetMetaData.getColumnLabel(1)).thenReturn("label1");
        when(resultSetMetaData.getColumnLabel(2)).thenReturn("label2");

        var result = queryExecutor.getColumnLabels(resultSetMetaData, 2);

        assertArrayEquals(new String[]{"label1", "label2"}, result);

        verify(resultSetMetaData).getColumnLabel(1);
        verify(resultSetMetaData).getColumnLabel(2);

        verifyNoMoreInteractions(resultSetMetaData);
    }

    @DisplayName("Should Get Table Values")
    @Test
    void shouldGetTableValues() throws SQLException {
        var resultSet = mock(ResultSet.class);

        when(resultSet.next()).thenReturn(true).thenReturn(false);
        when(resultSet.getObject(1)).thenReturn("val1");
        when(resultSet.getObject(2)).thenReturn("val2");

        var result = queryExecutor.getTableValues(resultSet, 2);

        assertArrayEquals(new Object[][]{new Object[]{"val1", "val2"}}, result);

        verify(resultSet, times(2)).next();
        verify(resultSet).getObject(1);
        verify(resultSet).getObject(2);

        verifyNoMoreInteractions(resultSet);
    }

    @DisplayName("Should Get Command From Query If Select")
    @Test
    void shouldGetCommandFromQueryIfSelect() {
        assertEquals(SELECT, queryExecutor.getCommandFromQuery("select * from test;"));
    }

    @DisplayName("Should Get Command From Query If Update")
    @Test
    void shouldGetCommandFromQueryIfUpdate() {
        assertEquals(UPDATE, queryExecutor.getCommandFromQuery("Update test SET (name = 'new') WHERE id = 1;"));
    }
}