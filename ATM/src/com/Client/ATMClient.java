package com.Client;

import com.ClientRequestUtil.ClientRequestUtil;
import com.Client.GUI.MainController;
import com.Client.Peripherials.PeripherialsHandler;
import com.MoneyInfoStorage.MoneyInfoStorage;

/**
 * The ATMClient class represents a client for the ATM system.
 * It handles communication with the server, manages client requests, and interacts with peripherals.
 */
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

    /**
     * Returns the singleton instance of the ATMClient class.
     * If the instance does not exist, it is created with default parameters.
     *
     * @return The instance of the ATMClient.
     */
    public static ATMClient getInstance() {
        if (instance == null) {
            instance = new ATMClient(1, "localhost", 1234);
        }
        return instance;
    }

    /**
     * Returns the singleton instance of the ATMClient class.
     * If the instance does not exist, it is created with the specified parameters.
     *
     * @param clientID      The client ID.
     * @param serverAddress The server address.
     * @param serverPort    The server port.
     * @return The instance of the ATMClient.
     */
    public static ATMClient getInstance(int clientID, String serverAddress, int serverPort) {
        if (instance == null) {
            instance = new ATMClient(clientID, serverAddress, serverPort);
        }
        return instance;
    }

    /**
     * Constructs a new ATMClient instance with the specified parameters.
     * It initializes the client socket handler, client request util, and peripherals handler.
     * It also checks the server connection status.
     *
     * @param clientID      The client ID.
     * @param serverAddress The server address.
     * @param serverPort    The server port.
     */
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

    /**
     * Sets the card number for the client request.
     *
     * @param cardNumber The card number.
     */
    public void setCardNumber(String cardNumber) {
        clientRequestUtil.setCardNumber(cardNumber);
    }

    /**
     * Sets the PIN for the client request.
     *
     * @param pin The PIN.
     */
    public void setPin(String pin) {
        clientRequestUtil.setPin(pin);
    }

    /**
     * Sets the phone number for the client request.
     *
     * @param phoneNumber The phone number.
     */
    public void setPhoneNumber(String phoneNumber) {
        clientRequestUtil.setPhoneNumber(phoneNumber);
    }

    /**
     * Sets the request type for the client request.
     *
     * @param request The request type.
     */
    public void setRequest(String request) {
        clientRequestUtil.setRequest(request);
    }

    /**
     * Sets the money information for the client request.
     *
     * @param moneyInfo The money information.
     */
    public void setMoneyInfo(MoneyInfoStorage moneyInfo) {
        clientRequestUtil.setMoneyInfo(moneyInfo);
    }

    /**
     * Gets the amount of money from the client request.
     *
     * @return The amount of money.
     */
    public int getMoneyAmount() {
        return clientRequestUtil.getAmount();
    }

    /**
     * Gets the transaction history from the client request.
     *
     * @return The transaction history.
     */
    public String getHistory() {
        return clientRequestUtil.getHistory();
    }

    /**
     * Sets the controller for the peripherals handler.
     *
     * @param controller The main controller.
     */
    public void setController(MainController controller) {
        peripherialsHandler.setController(controller);
    }

    /**
     * Sends the client request to the server and receives the response.
     * It processes the received message, performs actions, or updates the UI based on the response.
     */
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

