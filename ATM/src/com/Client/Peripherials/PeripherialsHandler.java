package com.Client.Peripherials;

import com.Client.ATMClient;
import com.Client.GUI.MainController;
import com.Client.Peripherials.KeyboardHandler.*;

import java.util.Objects;

public class PeripherialsHandler {
    public enum AtmState {
        HELLO, INPUT_PIN, AGAIN_PIN, AUTHENTICATION_ONGOING, AUTHENTICATION_FAILED, OPERATION_CHOICE, WITHDRAW, DEPOSIT, TOP_UP_PHONE, BALANCE, EXIT
    }
    private AtmState atmState = AtmState.HELLO;
    private ATMClient atmClient;
    private static PeripherialsHandler instance;
    private KeyboardHandler keyboardHandler = new KeyboardHandler();
    private CardReaderHandler cardReaderHandler = new CardReaderHandler();
    boolean firstRun = true;
    private MainController controller;

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
                    atmClient.setPin(keyboardHandler.getInput());
                    keyboardHandler.clear();
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
                atmClient.setRequest("authenticate");
                if (Objects.equals(keyboardHandler.getKeyboardState(), KeyboardState.ENTER)) {
                    atmClient.setPin(keyboardHandler.getInput());
                }
            }
            case AUTHENTICATION_ONGOING -> {
                keyboardHandler.clear();
                controller.handleAtmState(atmState);
                //atmClient.authenticate();
                //if (atmClient.isAuthenticated()) {
                //    atmState = AtmState.OPERATION_CHOICE;
                //} else {
                //    atmState = AtmState.AUTHENTICATION_FAILED;
                //}
            }
        }
    }

    public void run() {
        if (firstRun) {
            firstRun = false;
            atmClient = ATMClient.getInstance();
            return;
        }
        evaluateStateChange();
        runState();
    }
}
