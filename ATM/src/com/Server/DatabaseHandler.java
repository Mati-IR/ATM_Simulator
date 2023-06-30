package com.Server;

import com.MoneyInfoStorage.MoneyInfoStorage;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

/**
 * Handler for XAMPP MySQL database.
 */
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

    public final int OPERATION_WITHDRAW_PLN = 1;
    public final int OPERATION_DEPOSIT = 2;
    public final int OPERATION_WITHDRAW_EUR = 3;
    public final int OPERATION_BALANCE = 4;
    public final int OPERATION_PIN_CHANGE = 5;
    public final int OPERATION_TOP_UP_PHONE = 6;
    public final int OPERATION_HISTORY = 7;


    /**
     * Constructs a new DatabaseHandler instance.
     *
     * @param databaseAddress  the address of the database
     * @param databasePort     the port of the database
     * @param databaseName     the name of the database
     * @param databaseUsername the username for connecting to the database
     * @param databasePassword the password for connecting to the database
     */
    public DatabaseHandler(String databaseAddress, String databasePort, String databaseName, String databaseUsername, String databasePassword) {
        this.databaseAddress = databaseAddress;
        this.databasePort = databasePort;
        this.databaseName = databaseName;
        this.databaseUsername = databaseUsername;
        this.databasePassword = databasePassword;
    }

    /**
     * Connects to the database.
     */
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

    /**
     * Logs an operation to the database.
     *
     * @param userId    the ID of the user performing the operation
     * @param operation the type of operation
     * @param amount    the amount related to the operation
     */
    public void logToDatabase(String userId, int operation, String amount) {
        try {
            if (isConnected()) {
                String query = "INSERT INTO " + HISTORY_TABLE + " (" + USER_ACCOUNT_ID + ", Operation_Type, Amount) VALUES (?, ?, ?)";
                PreparedStatement statement = connection.prepareStatement(query);
                statement.setString(1, userId);
                statement.setString(2, Integer.toString(operation));
                statement.setString(3, amount);
                statement.executeUpdate();
            } else {
                System.err.println("Not connected to the database.");
            }
        } catch (SQLException e) {
            System.err.println("Failed to log operation to database: " + operation);
            e.printStackTrace();
        }
    }

    /**
     * Disconnects from the database.
     */
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

    /**
     * Checks if the connection to the database is active.
     *
     * @return {@code true} if connected, {@code false} otherwise
     */
    public boolean isConnected() {
        try {
            return connection != null && !connection.isClosed();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * Executes a database query.
     *
     * @param query the SQL query to execute
     * @return the ResultSet containing the query result
     */
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

    /**
     * Retrieves the PIN for a user from the database.
     *
     * @param userId the ID of the user
     * @return the PIN of the user, or {@code null} if not found
     */
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

    /**
     * Retrieves the balance for a user from the database.
     *
     * @param userId        the ID of the user
     * @param logOperation  flag indicating whether to log the operation
     * @return the balance of the user
     */
    public int getBalanceForUser(String userId, boolean logOperation) {
        String balance = null;
        try {
            if (isConnected()) {
                String query = "SELECT Balance FROM " + USERS_TABLE + " WHERE " + USER_ACCOUNT_ID + " = " + userId;
                PreparedStatement statement = connection.prepareStatement(query);
                ResultSet resultSet = statement.executeQuery();
                if (resultSet.next()) {
                    balance = resultSet.getString("balance");
                }
                if (logOperation) {
                    logToDatabase(userId, OPERATION_BALANCE, balance);
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

    /**
     * Changes the PIN for a user in the database.
     *
     * @param userId the ID of the user
     * @param newPin the new PIN to set
     * @return {@code true} if the PIN change is successful, {@code false} otherwise
     */
    public boolean changePinForUser(String userId, String newPin) {
        try {
            if (isConnected()) {
                String query = "UPDATE " + USERS_TABLE + " SET Pin = " + newPin + " WHERE " + USER_ACCOUNT_ID + " = " + userId;
                PreparedStatement statement = connection.prepareStatement(query);
                // if update executes successfully, return true
                if (1 == statement.executeUpdate()) {
                    logToDatabase(userId, OPERATION_PIN_CHANGE, null);
                    return true;
                }
                logToDatabase(userId, OPERATION_PIN_CHANGE, newPin);
            } else {
                System.err.println("Not connected to the database.");
            }
        } catch (SQLException e) {
            System.err.println("Failed to change PIN for user: " + userId);
            e.printStackTrace();
        }
        return false;
    }

    /**
     * Changes the balance for a user in the database.
     *
     * @param userId    the ID of the user
     * @param newBalance the new balance to set
     * @param amount     the amount related to the operation
     * @param operation  the type of operation
     * @return {@code true} if the balance change is successful, {@code false} otherwise
     */
    public boolean changeBalanceForUser(String userId, String newBalance, int amount, int operation) {
        try {
            if (isConnected()) {
                String query = "UPDATE " + USERS_TABLE + " SET Balance = " + newBalance + " WHERE " + USER_ACCOUNT_ID + " = " + userId;
                PreparedStatement statement = connection.prepareStatement(query);

                // if update executes successfully, return true
                if (1 == statement.executeUpdate()) {
                    logToDatabase(userId, operation, Integer.toString(amount));
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

    /**
     * Retrieves the history for a user from the database.
     *
     * @param userId the ID of the user
     * @return the history of the user as a string
     */
    public String getHistoryForUser(String userId) {
        String history = "";
        try {
            if (isConnected()) {
                String query = "SELECT * FROM " + HISTORY_TABLE + " WHERE " + USER_ACCOUNT_ID + " = " + userId;
                PreparedStatement statement = connection.prepareStatement(query);
                ResultSet resultSet = statement.executeQuery();
                logToDatabase(userId, OPERATION_HISTORY, null);
                int recordsSaved = 0;

                List<String> historyList = new ArrayList<>();


                while (resultSet.next()) {
                    recordsSaved++;
                    historyList.add(resultSet.getString("ID") + " " + resultSet.getString("Date") + " " + resultSet.getString("Operation_Type") + " " + resultSet.getString("Amount") + ";");
                }

                // add last five records to history string
                for (int i = historyList.size() - 1; i >= 0 && i >= historyList.size() - 5; i--) {
                    history += historyList.get(i);
                }
            } else {
                System.err.println("Not connected to the database.");
            }
        } catch (SQLException e) {
            System.err.println("Failed to get History for user: " + userId);
            e.printStackTrace();
        }
        System.out.println(history);
        return history;
    }
}
