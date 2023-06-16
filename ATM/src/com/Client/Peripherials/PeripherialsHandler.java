package com.Client.Peripherials;

import com.Client.ATMClient;
import com.Client.GUI.MainController;
import com.Client.Peripherials.KeyboardHandler.*;
import com.ClientRequestUtil.ClientRequestUtil;

import java.util.Objects;

public class PeripherialsHandler {
    public enum AtmState {
        HELLO, INPUT_PIN, AGAIN_PIN, AUTHENTICATION_ONGOING, OPERATION_CHOICE,
        WITHDRAW_PLN, WITHDRAW_EUR, DEPOSIT, TOP_UP_PHONE, BALANCE,
        OPERATION_PRINT, PIN_CHANGE, HOW_MANY_CASH, CHOICE_RECEIPT,
        CASH_INPUT_W, GIVE_TELE, TELE_AMOUNT, EXIT
    }
    private AtmState atmState = AtmState.HELLO;
    private ATMClient atmClient;
    private static PeripherialsHandler instance;
    private KeyboardHandler keyboardHandler = new KeyboardHandler();
    private CardReaderHandler cardReaderHandler = new CardReaderHandler();
    private MainController controller;
    private ClientRequestUtil clientRequestUtil = new ClientRequestUtil();

    private boolean firstRun = true;
    private boolean requestActive = false;

    public static PeripherialsHandler getInstance() {
        if (instance == null) {
            synchronized (PeripherialsHandler.class) {
                if (instance == null) {
                    instance = new PeripherialsHandler();
                }
            }
        }
        return instance;
    }

    public void setController(MainController controller) {
        this.controller = controller;
    }

    public void setMessage(String message) {
        if (message != null) {
            this.clientRequestUtil.decodeRequest(message);
            System.out.println("Message: " + message);
        }
    }

    /****** Keyboard ******/
    public void handleKeyboardInput(KeyboardKeys key) {
        keyboardHandler.handleKeyboardInput(key);
        run();
    }

    public String getKeyboardInput() {
        return keyboardHandler.getInput();
    }

    public KeyboardState getKeyboardState() {
        return keyboardHandler.getKeyboardState();
    }

    public void clearKeyboard() {
        keyboardHandler.clear();
    }

    /****** Card Reader ******/
    public void handleCardReaderInput(String cardNumber) {
        cardReaderHandler.handleCardReaderInput(cardNumber);
        run();
    }

    public String getCardNumber() {
        return cardReaderHandler.getCardNumber();
    }

    public void clearCardReader() {
        cardReaderHandler.clear();
    }

    /****** Side buttons ******/
    public void handleSideButton(int buttonNumber,AtmState atmState ) {
        sideButtonHandler.handleSideButton(buttonNumber,atmState);
        run();
    }



    /* STATE MACHINE */

    private boolean evaluateCommonEvents() {
        if (Objects.equals(keyboardHandler.getKeyboardState(), KeyboardState.CLEAR)) {
            keyboardHandler.clear();
            return true;
        }
        if (Objects.equals(keyboardHandler.getKeyboardState(), KeyboardState.CANCEL)) {
            keyboardHandler.clear();
            cardReaderHandler.clear();
            atmState = AtmState.HELLO;
            return true;
        }
        return false;
    }

    private void evaluateStateChange() {
        if (true == evaluateCommonEvents()) {
            return;
        }

        switch (atmState){
            case HELLO -> {
                if (!Objects.equals(cardReaderHandler.getCardNumber(), "")) {
                    atmState = AtmState.INPUT_PIN;
                    atmClient.setCardNumber(cardReaderHandler.getCardNumber());
                }
            }
            case INPUT_PIN -> {
                if (Objects.equals(keyboardHandler.getKeyboardState(), KeyboardState.ENTER)) {
                    atmState = AtmState.AUTHENTICATION_ONGOING;
                }
            }
            case AUTHENTICATION_ONGOING -> {
                if (clientRequestUtil.getIsRequestValid() && clientRequestUtil.getSelectedRequest().equalsIgnoreCase("success")) {
                    atmState = AtmState.OPERATION_CHOICE;
                } else if (clientRequestUtil.getIsRequestValid() && clientRequestUtil.getSelectedRequest().equalsIgnoreCase("failure")) {
                    atmState = AtmState.AGAIN_PIN;
                    System.out.println("Authentication failed");
                }
            }


        }
    }

    private void runState() {
        switch (atmState){
            case HELLO -> {
                controller.handleAtmState(atmState);
            }
            case INPUT_PIN -> {
                controller.handleAtmState(atmState);
                controller.setPinStars(keyboardHandler.getInput().length());
                atmClient.setRequest("authenticate");
                atmClient.setCardNumber(cardReaderHandler.getCardNumber());
                if (Objects.equals(keyboardHandler.getKeyboardState(), KeyboardState.ENTER)) {
                    atmClient.setPin(keyboardHandler.getInput());
                    atmClient.sendRequest();
                }
            }
            case AUTHENTICATION_ONGOING -> {
                atmClient.setPin(keyboardHandler.getInput());
                controller.handleAtmState(atmState);
                if (false == requestActive) {
                    atmClient.sendRequest();
                    requestActive = true;
                }
            }
            case OPERATION_CHOICE -> {
                controller.handleAtmState(atmState);
            }
        }
    }

    public void run() {
        if (firstRun) {
            firstRun = false;
            atmClient = ATMClient.getInstance();
        }
        evaluateStateChange();
        runState();
    }
}
