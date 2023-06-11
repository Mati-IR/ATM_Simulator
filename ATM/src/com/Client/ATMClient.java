package com.Client;

import ClientRequestUtil.ClientRequestUtil;
import com.Client.GUI.AtmGui;

public class ATMClient {
    private enum ClientState {
        NOT_CONNECTED, CONNECTED, AUTHENTICATED
    }

    private ClientSocketHandler clientSocketHandler;
    private ClientRequestUtil   clientRequestUtil;
    private AtmGui              atmGui;
    private ClientState         clientState = ClientState.NOT_CONNECTED;
    private int                 clientID = 0;
    private int                 clientPort = 0;
    private String              clientAddress = "";

    public ATMClient(int clientID, String serverAddress, int serverPort) {
        this.clientID = clientID;
        this.clientAddress = serverAddress;
        this.clientPort = serverPort;
        clientSocketHandler = new ClientSocketHandler(serverAddress, serverPort);
        clientRequestUtil = new ClientRequestUtil();
        atmGui = new AtmGui();
        Thread guiThread = new Thread(new Runnable() {
            @Override
            public void run() {
                atmGui.main(new String[0]);
            }
        });

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
        String request = "Authenticate";

        clientRequestUtil.setRequest(request);
        clientRequestUtil.setPin(userInput);
        clientRequestUtil.setUserNumber(userNumber);
        String clientRequest = clientRequestUtil.encodeRequest();

        if (clientRequest != "Error"){
            clientSocketHandler.sendRequest(clientRequest);
        } else {
            System.out.println("Error: Invalid request");
        }
    }

    public int run() {
        atmGui.main(new String[0]);
        clientRequestUtil.setRequest("history");
        clientSocketHandler.sendRequest(clientRequestUtil.encodeRequest());

        while (true) {
            secureConnection();
            if (ClientState.CONNECTED != this.clientState){
                System.out.println("Connection lost. Reconnecting...");
            } else {
                clientSocketHandler.sendRequest(clientRequestUtil.encodeRequest());
            }

            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private boolean secureConnection(){
        if (null == clientSocketHandler) {
            this.reconnect();
            return this.clientState == ClientState.CONNECTED;
        }
        if (false == clientSocketHandler.isConnected()) {
            this.reconnect();
            return this.clientState == ClientState.CONNECTED;
        }
        return this.clientState == ClientState.CONNECTED;
    }
    private void reconnect(){
        {
            System.out.println("Connection lost. Reconnecting...");
            if (clientSocketHandler == null) {
                clientSocketHandler = new ClientSocketHandler(this.clientAddress, this.clientPort);
                if (clientSocketHandler == null) {
                    //System.out.println("Could not connect to server");
                    this.clientState = ClientState.NOT_CONNECTED;
                    return;
                }
            }
            clientSocketHandler.connectToServer();
            if (clientSocketHandler.isConnected()) {
                //System.out.println("Reconnected.");
                this.clientState = ClientState.CONNECTED;
            } else {
                //System.out.println("Could not reconnect.");
                this.clientState = ClientState.NOT_CONNECTED;
            }
        }
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

