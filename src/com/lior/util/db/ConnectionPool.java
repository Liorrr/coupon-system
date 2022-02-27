package com.lior.util.db;

import com.lior.util.db.DbConfig;
import com.lior.util.logging.Logger;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Calendar;
import java.util.Stack;
import java.util.logging.Logger;

public class ConnectionPool {
    //public static final String SQL_URL = "";
    //public static final String SQL_USER = "root";
    //public static final String SQL_PASS = "123456";
    private static final int NUMBER_OF_CONNECTIONS = 5;
    private static ConnectionPool instance = null;
    private final Stack<Connection> connections = new Stack<>();

    private static final Logger logger = Logger.getLogger(String.valueOf(ConnectionPool.class));

    private ConnectionPool() throws SQLException {
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

    private void openAllConnections() throws SQLException {
        for (int counter = 0; counter < NUMBER_OF_CONNECTIONS; counter++) {
            final Connection connection = DriverManager.getConnection(DbConfig.sqlUrl, DbConfig.sqlUser, DbConfig.sqlPassword);
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

    public Connection getConnection() throws InterruptedException {
        synchronized (connections) {
            final long start = Calendar.getInstance().getTimeInMillis();
            if (connections.isEmpty()) {
                logger.debug(Thread.currentThread().getName() + " is waiting for an available connection");
            }
            while (connections.isEmpty()) {
                connections.wait();
            }
            final long end = Calendar.getInstance().getTimeInMillis();
            final long duration = end - start;
            logger.debug(Thread.currentThread().getName() + " found available connection after " + duration + " ms");
            return connections.pop();
        }
    }

    public void returnConnection(final Connection connection) {
        synchronized (connections) {
            if (connection == null) {
                logger.warn("Attempt to return null connection terminated");
                return;
            }
            connections.push(connection);
            logger.debug(Thread.currentThread().getName() + " is returning it's connection, now there are " + connections.size());
            connections.notifyAll();
        }
    }
}