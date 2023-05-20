package com.client;

import java.io.IOException;

public class ATMClient {
    public static void main(String[] args) {
        try {
            ClientSocketHandler clientSocketHandler = new ClientSocketHandler("localhost", 1234);
            clientSocketHandler.connectToServer();
            clientSocketHandler.sendRequest("withdraw 100");
            String response = clientSocketHandler.receiveResponse();
            System.out.println("Server response: " + response);
            clientSocketHandler.disconnectFromServer();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

