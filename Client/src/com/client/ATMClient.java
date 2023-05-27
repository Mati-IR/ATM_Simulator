package com.client;

public class ATMClient {
    private enum ClientState {
        NOT_CONNECTED, CONNECTED, AUTHENTICATED
    }

    private ClientState         clientState = ClientState.NOT_CONNECTED;
    private int                 clientID = 0;
    private ClientSocketHandler clientSocketHandler;

    public ATMClient(int clientID, String serverAddress, int serverPort) {
        this.clientID = clientID;
        clientSocketHandler = new ClientSocketHandler(serverAddress, serverPort);

        if(null == clientSocketHandler){
            System.out.println("Could not connect to server");
            this.clientState = ClientState.NOT_CONNECTED;
            return;
        }
        clientSocketHandler.connectToServer();
        if(clientSocketHandler.isConnected()){
            this.clientState = ClientState.CONNECTED;
        }

    }

    // destructor
    protected void finalize() throws Throwable {
        try {
            clientSocketHandler.disconnectFromServer();
        } finally {
            super.finalize();
        }
        System.out.println("ATMClient no. " + this.clientID + " object destroyed.");
    }
}

