package com.Client.Peripherials;

import java.util.HashMap;
import java.util.Map;

/**
 * The KeyboardHandler class is responsible for handling input from a keyboard device.
 */
public class KeyboardHandler {
    /**
     * Enumeration of keyboard keys.
     */
    public enum KeyboardKeys {
        KEY_1, KEY_2, KEY_3, KEY_4, KEY_5, KEY_6, KEY_7, KEY_8, KEY_9, KEY_0, KEY_CANCEL, KEY_CLEAR, KEY_ENTER
    }

    /**
     * Enumeration of keyboard states.
     */
    public enum KeyboardState {
        OK, CANCEL, CLEAR, ENTER, ERROR
    }

    // key to number mapping
    private static final Map<String, String> keysMappings = new HashMap<>();
    static {
        keysMappings.put("KEY_1", "1");
        keysMappings.put("KEY_2", "2");
        keysMappings.put("KEY_3", "3");
        keysMappings.put("KEY_4", "4");
        keysMappings.put("KEY_5", "5");
        keysMappings.put("KEY_6", "6");
        keysMappings.put("KEY_7", "7");
        keysMappings.put("KEY_8", "8");
        keysMappings.put("KEY_9", "9");
        keysMappings.put("KEY_0", "0");
        keysMappings.put("KEY_CANCEL", "CANCEL");
        keysMappings.put("KEY_CLEAR", "CLEAR");
        keysMappings.put("KEY_ENTER", "ENTER");
    }

    private KeyboardState keyboardState = KeyboardState.OK;
    private String input = "";

    /**
     * Handles the input from the keyboard device.
     *
     * @param key The keyboard key that was pressed.
     */
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
                input += keysMappings.get(key.toString());
                break;
            default:
                keyboardState = KeyboardState.ERROR;
                break;
        }
    }

    /**
     * Retrieves the input received from the keyboard.
     *
     * @return The input string.
     */
    public String getInput() {
        return input;
    }

    /**
     * Retrieves the current state of the keyboard.
     *
     * @return The keyboard state.
     */
    public KeyboardState getKeyboardState() {
        return keyboardState;
    }

    private void clearInput() {
        input = "";
    }

    private void clearState() {
        keyboardState = KeyboardState.OK;
    }

    /**
     * Clears the input and resets the keyboard state.
     */
    public void clear() {
        clearInput();
        clearState();
    }
}
