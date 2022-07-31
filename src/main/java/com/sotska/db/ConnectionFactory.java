package com.sotska.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectionFactory implements AutoCloseable {

    private final Connection connection;

    public ConnectionFactory(String url, String user, String password) {
        try {
            connection = DriverManager.getConnection(url, user, password);
        } catch (SQLException e) {
            throw new RuntimeException("Can't create connection with url: " + url + " password: " + password + " user: " + user, e);
        }
    }

    @Override
    public void close() throws Exception {
        connection.close();
    }

    public Connection getConnection() {
        return connection;
    }
}
