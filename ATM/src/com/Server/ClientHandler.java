package com.Server;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

import com.MoneyInfoStorage.MoneyInfoStorage;
import com.ClientRequestUtil.ClientRequestUtil;

class ClientHandler implements Runnable {
    boolean userIsAuthenticated = true;
    private Socket clientSocket;
    private ClientRequestUtil clientRequestUtil;
    private DatabaseHandler databaseHandler;
    MoneyInfoStorage moneyInfoStorage;

    private String history = "";

    //private MoneyInfoStorage moneyInfoStorage;

    public ClientHandler(Socket clientSocket, DatabaseHandler databaseHandler) {
        clientRequestUtil = new ClientRequestUtil();
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
        if (true == result) {
            clientRequestUtil.setRequest("success");
        } else {
            clientRequestUtil.setRequest("failure");
        }
        return result;
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

    private boolean balanceHandler(String userNumber) {
        if (false == clientRequestUtil.getIsRequestValid()){
            return false;
        }
        if (userIsAuthenticated) {
            final int groszInPLN = 100;
            int accountBalance = databaseHandler.getBalanceForUser(userNumber);
            MoneyInfoStorage zloteASkromne = new MoneyInfoStorage(MoneyInfoStorage.Currency.PLN, accountBalance / groszInPLN, accountBalance % groszInPLN);
            clientRequestUtil.setMoneyInfo(zloteASkromne);
            return true;
        }else {
            return false;
        }
    }

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

    private boolean historyHandler(String userNumber) {
        if (false == clientRequestUtil.getIsRequestValid()) {
            return false;
        }
        if (userIsAuthenticated) {
            history = databaseHandler.getHistoryForUser(userNumber);
            System.out.println(history);
            return history != null;
        }
        return false;
    }

    private boolean topupHandler(String userNumber, int amount, MoneyInfoStorage.Currency currency) {
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
