package com.Client;

import com.ClientRequestUtil.ClientRequestUtil;
import com.Client.GUI.MainController;
import com.Client.Peripherials.PeripherialsHandler;
import com.MoneyInfoStorage.MoneyInfoStorage;

public class ATMClient {
    private enum ClientState {
        NOT_CONNECTED, CONNECTED,
    }

    // instance
    private static ATMClient instance = null;

    private ClientSocketHandler clientSocketHandler;
    private ClientRequestUtil   clientRequestUtil;
    private PeripherialsHandler peripherialsHandler;
    private ClientState         clientState = ClientState.NOT_CONNECTED;
    private int                 clientID = 0;
    private int                 clientPort = 0;
    private String              clientAddress = "";

    public static ATMClient getInstance() {
        if (instance == null) {
            instance = new ATMClient(1, "localhost", 1234);
        }
        return instance;
    }

    private ATMClient(int clientID, String serverAddress, int serverPort) {
        this.clientID = clientID;
        this.clientAddress = serverAddress;
        this.clientPort = serverPort;
        clientSocketHandler = new ClientSocketHandler(serverAddress, serverPort);
        clientRequestUtil = new ClientRequestUtil();
        peripherialsHandler = PeripherialsHandler.getInstance();

        if(null == clientSocketHandler){
            System.out.println("Could not connect to server");
            this.clientState = ClientState.NOT_CONNECTED;
            return;
        }
        clientSocketHandler.connectToServer();
        if(clientSocketHandler.isConnected()){
            this.clientState = ClientState.CONNECTED;
            this.clientSocketHandler.disconnectFromServer();
        }
    }

    public void setCardNumber(String cardNumber) {
        clientRequestUtil.setCardNumber(cardNumber);
    }

    public void setPin(String pin) {
        clientRequestUtil.setPin(pin);
    }

    public void setPhoneNumber(String phoneNumber) {
        clientRequestUtil.setPhoneNumber(phoneNumber);
    }

    public void setRequest(String request) {
        clientRequestUtil.setRequest(request);
    }

    public void setMoneyInfo(MoneyInfoStorage moneyInfo) {
        clientRequestUtil.setMoneyInfo(moneyInfo);
    }

    public int getMoneyAmount() {
        return clientRequestUtil.getAmount();
    }

    public String getHistory() {
        return clientRequestUtil.getHistory();
    }

    public void setController(MainController controller) {
        peripherialsHandler.setController(controller);
    }

    public void sendRequest() {
        String clientRequest = clientRequestUtil.encodeRequest();
        if (clientRequest != "Error"){
            String response = clientSocketHandler.sendRequestAndReceiveResponse(clientRequest);
            if (response != null) {
                // Process the received message, perform actions, or update UI
                System.out.println("Received message from server: " + response);
                peripherialsHandler.setMessage(response);
                peripherialsHandler.run();
            } else {
                // Handle disconnection or error
                System.out.println("Lost connection to the server");
            }
        } else {
            System.out.println("Error: Invalid request");
        }
    }
}

