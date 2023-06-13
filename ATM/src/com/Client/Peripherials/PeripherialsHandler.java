package com.Client.Peripherials;

import com.Client.Peripherials.KeyboardHandler.*;

public class PeripherialsHandler {
    private KeyboardHandler keyboardHandler = new KeyboardHandler();
    private CardReaderHandler cardReaderHandler = new CardReaderHandler();



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




}
