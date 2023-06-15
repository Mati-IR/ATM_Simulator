package com.Client.GUI;

import com.Client.ATMClient;
import com.Client.Peripherials.PeripherialsHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;

import com.Client.Peripherials.KeyboardHandler.KeyboardKeys;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class MainController {
    PeripherialsHandler peripherialsHandler = PeripherialsHandler.getInstance();
    String cardNumber = "";

    @FXML
    private TextField cardnumber;

    @FXML
    private Button creditCard;

    @FXML
    private Button okcardnumber;

    @FXML
    private void handleCardNumberFromUser() {
        //save the card number
        cardNumber = cardnumber.textProperty().get();
        //check if the card number is at least one digit, and only digit
        if (cardNumber.length() > 0 && cardNumber.matches("[0-9]+")) {
            // OK, close this window
            Stage stage = (Stage) okcardnumber.getScene().getWindow();
            stage.close();
        } else {
            //not ok, show error message
            cardnumber.textProperty().set("Invalid card number");
        }

    }

    @FXML
    private void handleCreditCardPressed() {
        peripherialsHandler.handleCardReaderInput(cardNumber);
    }

    public void clearCardNumber() {
        cardNumber = "";
    }

    public void setCreditCardVisible(boolean visible) {
        creditCard.setVisible(visible);
    }

    @FXML
    private void handlePinButton1() {
        peripherialsHandler.handleKeyboardInput(KeyboardKeys.KEY_1);
    }

    @FXML
    private void handlePinButton2() {
        peripherialsHandler.handleKeyboardInput(KeyboardKeys.KEY_2);
    }

    @FXML
    private void handlePinButton3() {
        peripherialsHandler.handleKeyboardInput(KeyboardKeys.KEY_3);
    }

    @FXML
    private void handlePinButton4() {
        peripherialsHandler.handleKeyboardInput(KeyboardKeys.KEY_4);
    }

    @FXML
    private void handlePinButton5() {
        peripherialsHandler.handleKeyboardInput(KeyboardKeys.KEY_5);
    }

    @FXML
    private void handlePinButton6() {
        peripherialsHandler.handleKeyboardInput(KeyboardKeys.KEY_6);
    }

    @FXML
    private void handlePinButton7() {
        peripherialsHandler.handleKeyboardInput(KeyboardKeys.KEY_7);
    }

    @FXML
    private void handlePinButton8() {
        peripherialsHandler.handleKeyboardInput(KeyboardKeys.KEY_8);
    }

    @FXML
    private void handlePinButton9() {
        peripherialsHandler.handleKeyboardInput(KeyboardKeys.KEY_9);
    }

    @FXML
    private void handlePinButton0() {
        peripherialsHandler.handleKeyboardInput(KeyboardKeys.KEY_0);
    }

    @FXML
    private void handlePinButtonCancel() {
        peripherialsHandler.handleKeyboardInput(KeyboardKeys.KEY_CANCEL);
    }

    @FXML
    private void handlePinButtonClear() {
        peripherialsHandler.handleKeyboardInput(KeyboardKeys.KEY_CLEAR);
    }

    @FXML
    private void handlePinButtonEnter() {
        peripherialsHandler.handleKeyboardInput(KeyboardKeys.KEY_ENTER);
    }
}
