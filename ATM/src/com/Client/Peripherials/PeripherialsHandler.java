package com.Client.Peripherials;

import com.Client.Peripherials.KeyboardHandler.*;

public class PeripherialsHandler {
    private static PeripherialsHandler instance = null;
    private KeyboardHandler keyboardHandler = new KeyboardHandler();
    private CardReaderHandler cardReaderHandler = new CardReaderHandler();


    public static PeripherialsHandler getInstance() {
        //singleton
        if (instance == null) {
            instance = new PeripherialsHandler();
        }
        return instance;
    }

    /****** Keyboard ******/
    public void handleKeyboardInput(KeyboardKeys key) {
        keyboardHandler.handleKeyboardInput(key);
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
    }

    public String getCardNumber() {
        return cardReaderHandler.getCardNumber();
    }

    public void clearCardReader() {
        cardReaderHandler.clear();
    }

    public void run() {

    }
}
