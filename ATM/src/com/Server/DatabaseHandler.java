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

    private final String USERS_TABLE = "account_info";
    private final String USER_ACCOUNT_ID = "Account_Id";

    private final String HISTORY_TABLE = "operation_info";


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

    public String getPinForUser(String userId) {
        String pin = null;
        try {
            if (isConnected()) {
                String query = "SELECT Pin FROM " + USERS_TABLE + " WHERE " + USER_ACCOUNT_ID + " = " + userId;
                PreparedStatement statement = connection.prepareStatement(query);
                ResultSet resultSet = statement.executeQuery();
                if (resultSet.next()) {
                    pin = resultSet.getString("pin");
                }
            } else {
                System.err.println("Not connected to the database.");
            }
        } catch (SQLException e) {
            System.err.println("Failed to get PIN for user: " + userId);
            e.printStackTrace();
        }
        return pin;
    }

    public int getBalanceForUser(String userId) {
        String balance = null;
        try {
            if (isConnected()) {
                String query = "SELECT Balance FROM " + USERS_TABLE + " WHERE " + USER_ACCOUNT_ID + " = " + userId;
                PreparedStatement statement = connection.prepareStatement(query);
                ResultSet resultSet = statement.executeQuery();
                if (resultSet.next()) {
                    balance = resultSet.getString("balance");
                }
            } else {
                System.err.println("Not connected to the database.");
            }
        } catch (SQLException e) {
            System.err.println("Failed to get Balance for user: " + userId);
            e.printStackTrace();
        }
        return Integer.parseInt(balance);
    }

    public boolean changePinForUser(String userId, String newPin) {
        try {
            if (isConnected()) {
                String query = "UPDATE " + USERS_TABLE + " SET Pin = " + newPin + " WHERE " + USER_ACCOUNT_ID + " = " + userId;
                PreparedStatement statement = connection.prepareStatement(query);
                // if update executes successfully, return true
                if (1 == statement.executeUpdate()) {
                    return true;
                }
            } else {
                System.err.println("Not connected to the database.");
            }
        } catch (SQLException e) {
            System.err.println("Failed to change PIN for user: " + userId);
            e.printStackTrace();
        }
        return false;
    }

    public boolean changeBalanceForUser(String userId, String newBalance) {
        try {
            if (isConnected()) {
                String query = "UPDATE " + USERS_TABLE + " SET Balance = " + newBalance + " WHERE " + USER_ACCOUNT_ID + " = " + userId;
                PreparedStatement statement = connection.prepareStatement(query);

                // if update executes successfully, return true
                if (1 == statement.executeUpdate()) {
                    return true;
                }
            } else {
                System.err.println("Not connected to the database.");
            }
        } catch (SQLException e) {
            System.err.println("Failed to change Balance for user: " + userId);
            e.printStackTrace();
        }
        return false;
    }

    public String getHistoryForUser(String userId) {
        String history = "";
        try {
            if (isConnected()) {
                String query = "SELECT * FROM " + HISTORY_TABLE + " WHERE " + USER_ACCOUNT_ID + " = " + userId;
                PreparedStatement statement = connection.prepareStatement(query);
                System.out.println("executing query");
                ResultSet resultSet = statement.executeQuery();
                System.out.println("executed query");
                while (resultSet.next()) {
                    System.out.println("start");
                    history += resultSet.getString("ID") + " " + resultSet.getString("Date") + " " + resultSet.getString("Operation_Type") + " " + resultSet.getString("Amount");
                    System.out.println("end");
                }
            } else {
                System.err.println("Not connected to the database.");
            }
        } catch (SQLException e) {
            System.err.println("Failed to get History for user: " + userId);
            e.printStackTrace();
        }
        return history;
    }
}
