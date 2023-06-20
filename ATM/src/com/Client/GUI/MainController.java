package com.Client.GUI;

import com.Client.Peripherials.PeripherialsHandler;
import com.Client.Peripherials.PeripherialsHandler.AtmState;

import javafx.fxml.FXML;
import javafx.scene.control.Button;

import com.Client.Peripherials.KeyboardHandler.KeyboardKeys;
import javafx.scene.control.Label;
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
    private AnchorPane selectAmount;

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
    private AnchorPane howMuchCashIn;

    @FXML
    private AnchorPane howMuchCashOut;

    @FXML
    private AnchorPane errorin;

    @FXML
    private AnchorPane cashinwait;

    @FXML
    private AnchorPane insuccess;

    @FXML
    private AnchorPane wait;

    @FXML
    private AnchorPane insufficientFunds;

    @FXML
    private Label pinlabel1;

    @FXML
    private Label withdrawAmount;

    @FXML
    private Label accountBalance;

    @FXML
    private Label cashput;

    @FXML
    private Label depositedCash;

    @FXML
    private Button cash;

    @FXML
    private Button receipt;



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

    public void setCashVisible(boolean visible) {
        cash.setVisible(visible);
    }

    public void setReceiptVisible(boolean visible) {
        receipt.setVisible(visible);
    }

    public void setAccountbalance(String balance) {
        accountBalance.setText(balance);
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

    @FXML
    private void handleSB1() {
        peripherialsHandler.handleSideButton(1);
    }

    @FXML
    private void handleSB2() {
        peripherialsHandler.handleSideButton(2);
    }

    @FXML
    private void handleSB3() {
        peripherialsHandler.handleSideButton(3);
    }

    @FXML
    private void handleSB4() {
        peripherialsHandler.handleSideButton(4);
    }

    @FXML
    private void handleSB5() {
        peripherialsHandler.handleSideButton(5);
    }

    @FXML
    private void handleSB6() {
        if (AtmState.INPUT_PIN == atmState || AtmState.WITHDRAW_OTHER_AMOUNT == atmState) {
            handlePinButtonEnter();
        }
        peripherialsHandler.handleSideButton(6);
    }

    @FXML
    private void handleSB7() {
        peripherialsHandler.handleSideButton(7);
    }

    @FXML
    private void handleSB8() {
        peripherialsHandler.handleSideButton(8);
    }

    @FXML
    private void handleCashButton() {
        peripherialsHandler.handleCashButton();
    }

    @FXML
    private void handleReceiptButton() {
        setReceiptVisible(false);
    }


    public void setPinStars(int stars) {
        String starsString = "";
        for (int i = 0; i < stars; i++) {
            starsString += "*";
        }
        pinlabel1.setText(starsString);
    }

    public void setCashOutAmount(String amount) {
        withdrawAmount.setText(amount);
    }

    public void setDepostitAmount(String amount) {
        cashput.setText(amount);
    }

    public void setDepositedCash(String amount) {
        depositedCash.setText(amount);
    }

    private void setScreenAnchorPaneVisible(AnchorPane anchorPane, boolean visible) {
        anchorPane.setVisible(visible);
        //set other anchor panes invisible
        if (visible) {
            for (AnchorPane pane : new AnchorPane[]{hello, givepin, againpin, blockcard, operationchoice, selectAmount, cashinputw, errorinputcash, choicereceipt, accountbalance, waitforcash, changepin, pinsuccess, operationprint, givetele, teleamount, telesuccess, howMuchCashIn, errorin, cashinwait, insuccess, wait, insufficientFunds, howMuchCashOut}) {
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
            /*case AUTHENTICATION_FAILED -> {
                setScreenAnchorPaneVisible(blockcard, true);
            }*/
            case OPERATION_CHOICE -> {
                setScreenAnchorPaneVisible(operationchoice, true);
            }
            /*case WITHDRAW -> {
                setScreenAnchorPaneVisible(howmanycash, true);
            }*/



        }
    }

    public void handleAtmState(AtmState atmState) {
        System.out.println("Atm state changed to " + atmState);
        switch (atmState){
            case HELLO -> {
                setScreenAnchorPaneVisible(hello, true);
                setCreditCardVisible(true);
                setReceiptVisible(false);
                setCashVisible(false);
                //clearCardNumber();
            }
            case INPUT_PIN -> {
                setScreenAnchorPaneVisible(givepin, true);
                setCreditCardVisible(false);
                setReceiptVisible(false);
                setCashVisible(false);
            }
            case AUTHENTICATION_ONGOING -> {
                setScreenAnchorPaneVisible(wait, true);
            }
            case OPERATION_CHOICE -> {
                setScreenAnchorPaneVisible(operationchoice, true);
            }
            case WITHDRAW_AMOUNT_CHOICE -> {
                setScreenAnchorPaneVisible(selectAmount, true);
            }
            case CHOOSE_RECEIPT_WITHDRAWAL -> {
                setScreenAnchorPaneVisible(choicereceipt, true);
            }
            case NO_RECEIPT_WITHDRAWAL -> {
                setScreenAnchorPaneVisible(waitforcash, true);
                setCreditCardVisible(true);
                setReceiptVisible(false);
            }
            case PRINT_RECEIPT_WITHDRAWAL -> {
                setScreenAnchorPaneVisible(waitforcash, true);
                setCreditCardVisible(true);
            }
            case INSUFFICIENT_FUNDS -> {
                setScreenAnchorPaneVisible(insufficientFunds, true);
                setCreditCardVisible(true);
            }
            case WITHDRAW_OTHER_AMOUNT -> {
                setScreenAnchorPaneVisible(howMuchCashOut, true);
            }
            case BALANCE -> {
                setScreenAnchorPaneVisible(accountbalance, true);
            }
            case DEPOSIT_AMOUNT_CHOICE -> {
                setScreenAnchorPaneVisible(howMuchCashIn, true);
            }
            case CHOOSE_RECEIPT_DEPOSIT -> {
                setScreenAnchorPaneVisible(choicereceipt, true);
            }
            case NO_RECEIPT_DEPOSIT -> {
                setScreenAnchorPaneVisible(insuccess, true);
                setCreditCardVisible(true);
                setReceiptVisible(false);
            }
            case PRINT_RECEIPT_DEPOSIT -> {
                setScreenAnchorPaneVisible(insuccess, true);
                setCreditCardVisible(true);
                setReceiptVisible(true);
            }
        }
    }
}
