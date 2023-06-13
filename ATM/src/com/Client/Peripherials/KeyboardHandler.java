package com.Client.Peripherials;

public class KeyboardHandler {
    public enum KeyboardKeys {
        KEY_1, KEY_2, KEY_3, KEY_4, KEY_5, KEY_6, KEY_7, KEY_8, KEY_9, KEY_0, KEY_CANCEL, KEY_CLEAR, KEY_ENTER
    }
    public enum KeyboardState {
        OK, CANCEL, CLEAR, ENTER, ERROR
    }

    private KeyboardState keyboardState = KeyboardState.OK;
    private String input = "";

    public void handleKeyboardInput(KeyboardKeys key) {
        switch (key){
            case KEY_CANCEL:
                input = "";
                keyboardState = KeyboardState.CANCEL;
                break;
            case KEY_CLEAR:
                input = "";
                keyboardState = KeyboardState.CLEAR;
                break;
            case KEY_ENTER:
                keyboardState = KeyboardState.ENTER;
                break;
            case KEY_0:
            case KEY_1:
            case KEY_2:
            case KEY_3:
            case KEY_4:
            case KEY_5:
            case KEY_6:
            case KEY_7:
            case KEY_8:
            case KEY_9:
                keyboardState = KeyboardState.OK;
                input += key.toString();
                break;
            default:
                keyboardState = KeyboardState.ERROR;
                break;
        }
    }

    public String getInput() {
        return input;
    }

    public KeyboardState getKeyboardState() {
        return keyboardState;
    }

    private void clearInput() {
        input = "";
    }

    private void clearState() {
        keyboardState = KeyboardState.OK;
    }

    public void clear() {
        clearInput();
        clearState();
    }
}
