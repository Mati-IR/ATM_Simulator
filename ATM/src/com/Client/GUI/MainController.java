package com.Client.GUI;

import com.Client.ATMClient;
import com.Client.Peripherials.PeripherialsHandler;
import com.Client.Peripherials.PeripherialsHandler.AtmState;

import javafx.fxml.FXML;
import javafx.scene.control.Button;

import com.Client.Peripherials.KeyboardHandler.KeyboardKeys;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public class MainController {
    private AtmState atmState = AtmState.HELLO;
    private static MainController instance = null;
    private PeripherialsHandler peripherialsHandler = PeripherialsHandler.getInstance();
    private static String cardNumber = "";

    @FXML
    private TextField cardnumber;

    @FXML
    private Button creditCard;

    @FXML
    private Button okcardnumber;

    @FXML
    private AnchorPane hello;

    @FXML
    private AnchorPane givepin;

    @FXML
    private AnchorPane againpin;

    @FXML
    private AnchorPane blockcard;

    @FXML
    private AnchorPane operationchoice;

    @FXML
    private AnchorPane howmanycash;

    @FXML
    private AnchorPane cashinputw;

    @FXML
    private AnchorPane errorinputcash;

    @FXML
    private AnchorPane choicereceipt;

    @FXML
    private AnchorPane accountbalance;

    @FXML
    private AnchorPane waitforcash;

    @FXML
    private AnchorPane changepin;

    @FXML
    private AnchorPane pinsuccess;

    @FXML
    private AnchorPane operationprint;

    @FXML
    private AnchorPane givetele;

    @FXML
    private AnchorPane teleamount;

    @FXML
    private AnchorPane telesuccess;

    @FXML
    private AnchorPane howmanycashin;

    @FXML
    private AnchorPane errorin;

    @FXML
    private AnchorPane cashinwait;

    @FXML
    private AnchorPane insuccess;

    @FXML
    private AnchorPane wait;

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

    public static void clearCardNumber() {
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

    private void setScreenAnchorPaneVisible(AnchorPane anchorPane, boolean visible) {
        anchorPane.setVisible(visible);
        //set other anchor panes invisible
        if (visible) {
            for (AnchorPane pane : new AnchorPane[]{hello, givepin, againpin, blockcard, operationchoice, howmanycash, cashinputw, errorinputcash, choicereceipt, accountbalance, waitforcash, changepin, pinsuccess, operationprint, givetele, teleamount, telesuccess, howmanycashin, errorin, cashinwait, insuccess, wait}) {
                if (pane != anchorPane) {
                    pane.setVisible(false);
                }
            }
        }
    }

    private void screenStateUpdate(AtmState atmState) {
        switch (atmState) {
            case HELLO -> {
                setScreenAnchorPaneVisible(hello, true);
            }
            case INPUT_PIN -> {
                setScreenAnchorPaneVisible(givepin, true);
            }
            case AGAIN_PIN -> {
                /* TODO: implement */
            }
            case AUTHENTICATION_ONGOING -> {
                setScreenAnchorPaneVisible(wait, true);
            }
            case AUTHENTICATION_FAILED -> {
                setScreenAnchorPaneVisible(blockcard, true);
            }
            case OPERATION_CHOICE -> {
                setScreenAnchorPaneVisible(operationchoice, true);
            }
            case WITHDRAW -> {
                setScreenAnchorPaneVisible(howmanycash, true);
            }



        }
    }

    public void handleAtmState(AtmState atmState) {
        System.out.println("Atm state changed to " + atmState);
        switch (atmState){
            case HELLO -> {
                setCreditCardVisible(true);
                clearCardNumber();
            }
            case INPUT_PIN -> {
                setScreenAnchorPaneVisible(givepin, true);
                setCreditCardVisible(false);
            }
        }
    }
}
