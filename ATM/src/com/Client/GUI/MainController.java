package com.Client.GUI;

import com.Client.ATMClient;
import javafx.fxml.FXML;
import javafx.scene.control.Button;

import com.Client.Peripherials.KeyboardHandler.KeyboardKeys;

public class MainController {
    ATMClient atmClient = ATMClient.getInstance();

    @FXML
    private void handlePinButton1() {
        atmClient.handleKeyboardInput(KeyboardKeys.KEY_1);
    }

    @FXML
    private void handlePinButton2() {
        atmClient.handleKeyboardInput(KeyboardKeys.KEY_2);
    }

    @FXML
    private void handlePinButton3() {
        atmClient.handleKeyboardInput(KeyboardKeys.KEY_3);
    }

    @FXML
    private void handlePinButton4() {
        atmClient.handleKeyboardInput(KeyboardKeys.KEY_4);
    }

    @FXML
    private void handlePinButton5() {
        atmClient.handleKeyboardInput(KeyboardKeys.KEY_5);
    }

    @FXML
    private void handlePinButton6() {
        atmClient.handleKeyboardInput(KeyboardKeys.KEY_6);
    }

    @FXML
    private void handlePinButton7() {
        atmClient.handleKeyboardInput(KeyboardKeys.KEY_7);
    }

    @FXML
    private void handlePinButton8() {
        atmClient.handleKeyboardInput(KeyboardKeys.KEY_8);
    }

    @FXML
    private void handlePinButton9() {
        atmClient.handleKeyboardInput(KeyboardKeys.KEY_9);
    }

    @FXML
    private void handlePinButton0() {
        atmClient.handleKeyboardInput(KeyboardKeys.KEY_0);
    }

    @FXML
    private void handlePinButtonCancel() {
        atmClient.handleKeyboardInput(KeyboardKeys.KEY_CANCEL);
    }

    @FXML
    private void handlePinButtonClear() {
        atmClient.handleKeyboardInput(KeyboardKeys.KEY_CLEAR);
    }

    @FXML
    private void handlePinButtonEnter() {
        atmClient.handleKeyboardInput(KeyboardKeys.KEY_ENTER);
    }
}
