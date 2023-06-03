package com.Server;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

/* Handler for XAMPP MySQL database */
public class DatabaseHandler {
    private String databaseAddress;
    private String databasePort;
    private String databaseName;
    private String databaseUsername;
    private String databasePassword;
    private Connection connection;

    public DatabaseHandler(String databaseAddress, String databasePort, String databaseName, String databaseUsername, String databasePassword) {
        this.databaseAddress = databaseAddress;
        this.databasePort = databasePort;
        this.databaseName = databaseName;
        this.databaseUsername = databaseUsername;
        this.databasePassword = databasePassword;
    }

    public void connectToDatabase() {
        try {
            Class.forName("com.mysql.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            System.err.println("Failed to load MySQL JDBC driver.");
            e.printStackTrace();
            return;
        }
        String jdbcUrl = "jdbc:mysql://" + databaseAddress + ":" + databasePort + "/" + databaseName;
        try {
            connection = DriverManager.getConnection(jdbcUrl, databaseUsername, databasePassword);
            System.out.println("Connected to database: " + databaseName);
        } catch (SQLException e) {
            System.err.println("Failed to connect to database: " + databaseName);
            e.printStackTrace();
        }
    }

    public void disconnectFromDatabase() {
        if (connection != null) {
            try {
                connection.close();
                System.out.println("Disconnected from database: " + databaseName);
            } catch (SQLException e) {
                System.err.println("Failed to disconnect from database: " + databaseName);
                e.printStackTrace();
            }
        }
    }

    public boolean isConnected() {
        try {
            return connection != null && !connection.isClosed();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public ResultSet executeQuery(String query) {
        ResultSet resultSet = null;
        try {
            if (isConnected()) {
                PreparedStatement statement = connection.prepareStatement(query);
                resultSet = statement.executeQuery();
            } else {
                System.err.println("Not connected to the database.");
            }
        } catch (SQLException e) {
            System.err.println("Failed to execute query: " + query);
            e.printStackTrace();
        }
        return resultSet;
    }

}
