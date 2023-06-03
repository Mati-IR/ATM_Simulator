package com.Client;

import ClientRequestUtil.ClientRequestUtil;

public class ATMClient {
    private enum ClientState {
        NOT_CONNECTED, CONNECTED, AUTHENTICATED
    }

    private ClientState         clientState = ClientState.NOT_CONNECTED;
    private int                 clientID = 0;
    //private ClientSocketHandler clientSocketHandler;
    private ClientRequestUtil   clientRequestUtil;

    public ATMClient(int clientID, String serverAddress, int serverPort) {
        this.clientID = clientID;
        //clientSocketHandler = new ClientSocketHandler(serverAddress, serverPort);
        clientRequestUtil = new ClientRequestUtil();
/*
        if(null == clientSocketHandler){
            System.out.println("Could not connect to server");
            this.clientState = ClientState.NOT_CONNECTED;
            return;
        }
        clientSocketHandler.connectToServer();
        if(clientSocketHandler.isConnected()){
            this.clientState = ClientState.CONNECTED;
        }

        // Fake authentication request
        String userInput = "2137";
        String userNumber = "1";
        String action = "Authenticate";

        clientRequestUtil.setRequest(action);
        clientRequestUtil.setPin(userInput);
        String clientRequest = clientRequestUtil.encodeRequest();

        if (clientRequest != "Error"){
            clientSocketHandler.sendRequest(clientRequest);
        }*/

    }

    // destructor
    protected void finalize() throws Throwable {
        try {
            //clientSocketHandler.disconnectFromServer();
        } finally {
            super.finalize();
        }
        System.out.println("ATMClient no. " + this.clientID + " object destroyed.");
    }
}

