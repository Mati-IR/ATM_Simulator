package com.Server;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Locale;
import java.util.Objects;

import com.MoneyInfoStorage.MoneyInfoStorage;
import com.ClientRequestUtil.ClientRequestUtil;

/**
 * The ClientHandler class represents a thread that handles client requests in the ATM server.
 * It receives client requests, processes them, and sends the appropriate response back to the client.
 */
class ClientHandler implements Runnable {
    boolean userIsAuthenticated = true;
    private Socket clientSocket;
    private ClientRequestUtil clientRequestUtil;
    private DatabaseHandler databaseHandler;
    MoneyInfoStorage moneyInfoStorage;

    private String history = "";

    /**
     * Constructs a new {@code ClientHandler} instance with the specified client socket and database handler.
     *
     * @param clientSocket the client socket connected to the client
     * @param databaseHandler the database handler used to interact with the database
     */
    public ClientHandler(Socket clientSocket, DatabaseHandler databaseHandler) {
        clientRequestUtil = new ClientRequestUtil();
        this.clientSocket = clientSocket;
        this.databaseHandler = databaseHandler;
        this.moneyInfoStorage = new MoneyInfoStorage();
    }

    /**
     * The run method represents the main execution logic of the client handler thread.
     * It reads the client request, processes it, and sends the response back to the client.
     */
    @Override
    public void run() {
        try {
            // Create input and output streams for client communication
            BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);

            // Read client request
            String request = in.readLine();
            System.out.println("Received request from client: " + request);

            // Process client request and send response
            processRequest(request);
            // Send response to client
            out.println(clientRequestUtil.encodeRequest());

            // Close streams and socket
            in.close();
            out.close();
            clientSocket.close();
            System.out.println("Client disconnected: " + clientSocket);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Processes the client request and performs the corresponding actions based on the selected request.
     *
     * @param request  the client request to process
     * @return {@code true} if the request was processed successfully, {@code false} otherwise
     */
    private boolean processRequest(String request) {
        /* Extract information from incoming message */
        clientRequestUtil.decodeRequest(request);
        String selectedRequest = clientRequestUtil.getSelectedRequest();

        /* Based on the operation type, perform the necessary actions */
        boolean result = false;
        switch (selectedRequest) {
            case "authenticate":
                result = authentificationHandler(clientRequestUtil.getCardNumber(), clientRequestUtil.getPin());
                break;
            case "withdraw":
                result = withdrawHandler(clientRequestUtil.getCardNumber(), clientRequestUtil.getAmount(), clientRequestUtil.getCurrency());
                break;
            case "deposit":
                result = depositHandler(clientRequestUtil.getCardNumber(), clientRequestUtil.getAmount(), clientRequestUtil.getCurrency());
                break;
            case "balance":
                result = balanceHandler(clientRequestUtil.getCardNumber());
                break;
            case "history":
                result = historyHandler(clientRequestUtil.getCardNumber());
                break;
            case "changepin":
                result = changePinHandler(clientRequestUtil.getCardNumber(), clientRequestUtil.getPin());
                break;
            case "topup":
                result = topupHandler(clientRequestUtil.getCardNumber(), clientRequestUtil.getAmount(), clientRequestUtil.getCurrency());
                break;


        }
        if (Objects.equals(clientRequestUtil.getSelectedRequest(), "balance")) {
            clientRequestUtil.setRequest("balance");
        } else if (clientRequestUtil.getSelectedRequest().toLowerCase().equals("history") || clientRequestUtil.getSelectedRequest().toLowerCase().equals("h")) {
            clientRequestUtil.setRequest("history");
        }
        else if (true == result) {
            clientRequestUtil.setRequest("success");
        } else {
            clientRequestUtil.setRequest("failure");
        }
        return result;
    }

    /**
     * Performs authentication for the user based on the provided user number and PIN.
     *
     * @param userNumber   the user number for authentication
     * @param userInputPin the PIN provided by the user
     * @return {@code true} if authentication is successful, {@code false} otherwise
     */
    private boolean authentificationHandler(String userNumber, String userInputPin) {
        if (false == clientRequestUtil.getIsRequestValid()){
            return false;
        }
        String pinFromDatabase = databaseHandler.getPinForUser(userNumber);
        if (pinFromDatabase == null) {
            return false;
        }
        if (pinFromDatabase.equals(userInputPin)) {
            userIsAuthenticated = true;
            return true;
        } else {
            userIsAuthenticated = false;
            return false;
        }
    }

    /**
     * Handles the withdrawal operation for the user based on the specified amount and currency.
     *
     * @param userNumber the user number for the withdrawal
     * @param amount     the amount to withdraw
     * @param currency   the currency of the withdrawal
     * @return {@code true} if the withdrawal is successful, {@code false} otherwise
     */
    private boolean withdrawHandler(String userNumber, int amount, MoneyInfoStorage.Currency currency) {
        if (false == clientRequestUtil.getIsRequestValid()){
            return false;
        }
        if (userIsAuthenticated) {
            int accountBalance = databaseHandler.getBalanceForUser(userNumber, false);
            int amountToWithdraw = amount;
            int operation = databaseHandler.OPERATION_WITHDRAW_PLN;

            if (currency == MoneyInfoStorage.Currency.EUR) { /* Only PLN withdrawals are allowed for now */
                amountToWithdraw = amountToWithdraw * moneyInfoStorage.getExchangeRate(MoneyInfoStorage.Currency.EUR);
                operation = databaseHandler.OPERATION_WITHDRAW_EUR;
            }
            if (accountBalance >= amountToWithdraw) {
                int newBalance = accountBalance - amountToWithdraw;
                return databaseHandler.changeBalanceForUser(userNumber, String.valueOf(newBalance), amountToWithdraw, operation);
            } else {
                return false;
            }
        }else {
            return false;
        }
    }

    /**
     * Handles the deposit operation for the user based on the specified amount and currency.
     *
     * @param userNumber the user number for the deposit
     * @param amount     the amount to deposit
     * @param currency   the currency of the deposit
     * @return {@code true} if the deposit is successful, {@code false} otherwise
     */
    private boolean depositHandler(String userNumber, int amount, MoneyInfoStorage.Currency currency) {
        if (false == clientRequestUtil.getIsRequestValid()){
            return false;
        }
        if (userIsAuthenticated) {
            int accountBalance = databaseHandler.getBalanceForUser(userNumber, false);
            int amountToDeposit = amount;
            if (currency == MoneyInfoStorage.Currency.EUR) { /* Only PLN deposits are allowed for now */
                amountToDeposit = amountToDeposit *  moneyInfoStorage.getExchangeRate(MoneyInfoStorage.Currency.EUR);
            }
            int newBalance = accountBalance + amountToDeposit;
            boolean result = databaseHandler.changeBalanceForUser(userNumber, String.valueOf(newBalance), amountToDeposit, databaseHandler.OPERATION_DEPOSIT);
            return result;
        }else {
            return false;
        }
    }

    /**
     * Retrieves the balance for the user and sets it in the client request utility.
     *
     * @param userNumber the user number to retrieve the balance
     * @return {@code true} if the balance retrieval is successful, {@code false} otherwise
     */
    private boolean balanceHandler(String userNumber) {
        if (false == clientRequestUtil.getIsRequestValid()){
            return false;
        }
        if (userIsAuthenticated) {
            final int groszInPLN = 100;
            int accountBalance = databaseHandler.getBalanceForUser(userNumber, true);
            clientRequestUtil.setAmount(Integer.toString(accountBalance));
            return true;
        }else {
            return false;
        }
    }

    /**
     * Handles the change PIN operation for the user.
     *
     * @param userNumber the user number for the PIN change
     * @param newPin     the new PIN to set
     * @return {@code true} if the PIN change is successful, {@code false} otherwise
     */
    private boolean changePinHandler(String userNumber, String newPin) {
        if (false == clientRequestUtil.getIsRequestValid()){
            return false;
        }
        if (userIsAuthenticated) {
            boolean result = databaseHandler.changePinForUser(userNumber, newPin);
            return result;
        }else {
            return false;
        }
    }

    /**
     * Retrieves the transaction history for the user and sets it in the client request utility.
     *
     * @param userNumber the user number to retrieve the history
     * @return {@code true} if the history retrieval is successful, {@code false} otherwise
     */
    private boolean historyHandler(String userNumber) {
        if (false == clientRequestUtil.getIsRequestValid()) {
            return false;
        }
        if (userIsAuthenticated) {
            this.history = databaseHandler.getHistoryForUser(userNumber);
            this.clientRequestUtil.setRequest("history");
            this.clientRequestUtil.setHistory(history);
            System.out.println(history);
            return history != null;
        }
        return false;
    }

    /**
     * Handles the top-up operation for the user's phone based on the specified amount and currency.
     *
     * @param userNumber the user number for the top-up
     * @param amount     the amount to top up
     * @param currency   the currency of the top-up
     * @return {@code true} if the top-up is successful, {@code false} otherwise
     */
    private boolean topupHandler(String userNumber, int amount, MoneyInfoStorage.Currency currency) {
        if (false == clientRequestUtil.getIsRequestValid()){
            return false;
        }
        if (userIsAuthenticated) {
            boolean result = false;
            int accountBalance = databaseHandler.getBalanceForUser(userNumber, false);
            int amountToDeposit = amount;
            int operation = databaseHandler.OPERATION_TOP_UP_PHONE;
            if (currency == MoneyInfoStorage.Currency.EUR) { /* Only PLN deposits are allowed for now */
                amountToDeposit = amountToDeposit * moneyInfoStorage.getExchangeRate(MoneyInfoStorage.Currency.EUR);
            }
            int newBalance = accountBalance - amountToDeposit;
            if (newBalance >= 0) {
                result = databaseHandler.changeBalanceForUser(userNumber, String.valueOf(newBalance), amountToDeposit, operation);
            } else {
                return false;
            }
            return result;
        }else {
            return false;
        }
    }


}
