package com.lior.util.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Stack;

public class ConnectionPool {
    private static final String SQL_URL = "jdbc:mysql://localhost:3306/coupon_system?useSSL=false&serverTimezone=UTC";
    private static final String SQL_USER = "root";
    private static final String SQL_PASS = "123456";
    private static final int NUMBER_OF_CONNECTIONS = 20;
    private static ConnectionPool instance = null;
    private final Stack<Connection> connections = new Stack<>();

    public ConnectionPool() throws SQLException {
        System.out.println("Created new connection pool");
        openAllConnections();
    }

    public static ConnectionPool getInstance() throws SQLException {
        if (instance == null) {
            synchronized (ConnectionPool.class) {
                if (instance == null) {
                    instance = new ConnectionPool();
                }
            }
        }
        return instance;
    }

    private void openAllConnections() throws SQLException  {
        for (int counter = 0; counter < NUMBER_OF_CONNECTIONS; counter++) {
            final Connection connection = DriverManager.getConnection(SQL_URL, SQL_USER, SQL_PASS);
            connections.push(connection);
        }
    }

    public void closeAllConnections() throws InterruptedException {
        synchronized (connections) {
            while (connections.size() < NUMBER_OF_CONNECTIONS){
                connections.wait();
            }
            connections.removeAllElements();
        }
    }

    //Getting a specific connection from the stack for any desirable operation
    public Connection getConnection() throws InterruptedException {
        synchronized (connections) {
            while (connections.isEmpty()) {
                connections.wait();
            }
            return connections.pop();
        }
    }

    //Returning one of the connections back to the stack
    public void returnConnection(final Connection connection) {
        synchronized (connections) {
            if (connection == null) {
                return;
            }
            connections.push(connection);
            connections.notify();
        }
    }
}