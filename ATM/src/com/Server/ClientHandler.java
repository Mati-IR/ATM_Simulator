package com.Server;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

import MoneyInfoStorage.MoneyInfoStorage;

class ClientHandler implements Runnable {
    boolean userIsAuthenticated = false;
    private Socket clientSocket;
    private ClientRequestUtil.ClientRequestUtil clientRequestUtil;
    private DatabaseHandler databaseHandler;
    MoneyInfoStorage moneyInfoStorage;


    //private MoneyInfoStorage moneyInfoStorage;

    public ClientHandler(Socket clientSocket, DatabaseHandler databaseHandler) {
        clientRequestUtil = new ClientRequestUtil.ClientRequestUtil();
        this.clientSocket = clientSocket;
        this.databaseHandler = databaseHandler;
        this.moneyInfoStorage = new MoneyInfoStorage();
    }

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
            String response = processRequest(request);
            out.println(response);

            // Close streams and socket
            in.close();
            out.close();
            clientSocket.close();
            System.out.println("Client disconnected: " + clientSocket);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private String processRequest(String request) {
        // TODO: Implement the logic for processing client requests and generating a response
        // TODO: Parse the client request to identify the operation type and required parameters
        clientRequestUtil.decodeRequest(request);
        String selectedRequest = clientRequestUtil.getSelectedRequest();

        // TODO: Based on the operation type, perform the necessary actions
        boolean result = false;
        switch (selectedRequest) {
            case "authenticate":
                result = authentificationHandler(clientRequestUtil.getUserNumber(), clientRequestUtil.getPin());
                break;
            case "withdraw":
                result = withdrawHandler(clientRequestUtil.getUserNumber(), clientRequestUtil.getAmount(), clientRequestUtil.getCurrency());
                break;
            case "deposit":
                result = depositHandler(clientRequestUtil.getUserNumber(), clientRequestUtil.getAmount(), clientRequestUtil.getCurrency());
                break;
        }
        if (true == result) {
            System.out.println("Authentification successful");
        } else {
            System.out.println("Authentification failed");
        }

        // Example operations: withdrawing money, depositing money, checking balance, etc.

        // TODO: Update account balances, transaction history, and any relevant data structures

        // TODO: Generate the appropriate response based on the operation result or error messages

        return "response";
    }

    private boolean authentificationHandler(String userNumber, String userInputPin) {
        if (false == clientRequestUtil.getIsRequestValid()){
            return false;
        }
        String pinFromDatabase = databaseHandler.getPinForUser(userNumber);
        if (pinFromDatabase.equals(userInputPin)) {
            userIsAuthenticated = true;
            return true;
        } else {
            userIsAuthenticated = false;
            return false;
        }
    }

    private boolean withdrawHandler(String userNumber, int amount, MoneyInfoStorage.Currency currency) {
        if (false == clientRequestUtil.getIsRequestValid()){
            return false;
        }
        if (userIsAuthenticated) {
            int accountBalance = databaseHandler.getBalanceForUser(userNumber);
            int amountToWithdraw = amount;
            if (currency == MoneyInfoStorage.Currency.EUR) { /* Only PLN withdrawals are allowed for now */
                amountToWithdraw *= moneyInfoStorage.getExchangeRate();
            }
            if (accountBalance >= amountToWithdraw) {
                int newBalance = accountBalance - amountToWithdraw;
                boolean result = databaseHandler.changeBalanceForUser(userNumber, String.valueOf(newBalance));
                return result;
            } else {
                return false;
            }
        }else {
            return false;
        }
    }

    private boolean depositHandler(String userNumber, int amount, MoneyInfoStorage.Currency currency) {
        if (false == clientRequestUtil.getIsRequestValid()){
            return false;
        }
        if (userIsAuthenticated) {
            int accountBalance = databaseHandler.getBalanceForUser(userNumber);
            int amountToDeposit = amount;
            if (currency == MoneyInfoStorage.Currency.EUR) { /* Only PLN deposits are allowed for now */
                amountToDeposit *= moneyInfoStorage.getExchangeRate();
            }
            int newBalance = accountBalance + amountToDeposit;
            boolean result = databaseHandler.changeBalanceForUser(userNumber, String.valueOf(newBalance));
            return result;
        }else {
            return false;
        }
    }
}
