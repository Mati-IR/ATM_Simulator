/**
 The MainController class is responsible for managing the GUI of the ATM application.
 It handles user interactions, updates the display, and communicates with the PeripheralsHandler.
 */

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




/**
 The MainController class controls the main window of the ATM application.
 */

public class MainController {
    private AtmState atmState = AtmState.HELLO;
    private static MainController instance = null;
    private PeripherialsHandler peripherialsHandler = PeripherialsHandler.getInstance();
    private static String cardNumber = "";
    private String history = "";



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
    private Label telecash;

    @FXML
    private Label telenum;

    @FXML
    private Label newpin;

    @FXML
    private Label historyOperation1;

    @FXML
    private Label historyOperation2;

    @FXML
    private Label historyOperation3;

    @FXML
    private Label historyOperation4;

    @FXML
    private Label historyOperation5;

    @FXML
    private Button cash;

    @FXML
    private Button receipt;


    /**
     * Handles the action when the user enters the card number.
     * Saves the card number and validates it.
     * Closes the current window if the card number is valid.
     */
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
        if (AtmState.INPUT_PIN == atmState || AtmState.WITHDRAW_OTHER_AMOUNT == atmState
         || AtmState.AGAIN_PIN == atmState || AtmState.INCORRECT_AMOUNT == atmState
         || AtmState.DEPOSIT_INCORRECT_AMOUNT == atmState) {
            handlePinButtonEnter();
            return;
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

    /**
     * Sets the amount for cash withdrawal on the GUI.
     *
     * @param amount The amount to be displayed for cash withdrawal.
     */
    public void setCashOutAmount(String amount) {
        withdrawAmount.setText(amount);
    }

    /**
     * Sets the amount for cash deposit on the GUI.
     *
     * @param amount The amount to be displayed for cash deposit.
     */
    public void setDepostitAmount(String amount) {
        cashput.setText(amount);
    }

    /**
     * Sets the deposited cash amount on the GUI.
     *
     * @param amount The amount of cash that has been deposited.
     */
    public void setDepositedCash(String amount) {
        depositedCash.setText(amount);
    }

    /**
     * Sets the phone number on the GUI for phone top-up.
     *
     * @param number The phone number to be displayed.
     */
    public void setPhoneNumber(String number) {
        telenum.setText(number);
    }

    /**
     * Sets the amount for phone top-up on the GUI.
     *
     * @param amount The amount to be displayed for phone top-up.
     */
    public void setPhoneAmount(String amount) {
        telecash.setText(amount);
    }

    /**
     * Sets the new PIN on the GUI for PIN change.
     *
     * @param pin The new PIN to be displayed.
     */
    public void setNewPin(String pin) {
        newpin.setText(pin);
    }

    /**
     * Sets the transaction history on the GUI.
     *
     * @param history The transaction history to be displayed.
     */
    public void setHistory(String history) {
        this.history = history;
    }

    /**
     * Sets the visibility of the specified anchor pane on the GUI.
     *
     * @param anchorPane The anchor pane to be shown or hidden.
     * @param visible    {@code true} to make the anchor pane visible, {@code false} to hide it.
     */
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

    /**
     * Extracts the relevant information from a history record and formats it.
     *
     * @param history The array containing the history record information.
     * @return The formatted history record string.
     */
    private String extractHistory(String history[]) {
        String operation = "";
        switch (history[3]) {
            case "1" -> operation = "Wypłata Zł";
            case "2" -> operation = "Wpłata";
            case "3" -> operation = "Wypłata EUR";
            case "4" -> operation = "Stan konta";
            case "5" -> operation = "Zmiana PIN";
            case "6" -> operation = "Doładowanie telefonu";
            case "7" -> operation = "Sprawdzenie historii";
        }

        String date = history[1];
        String time = history[2];

        return operation + " " + date; // + " " + time;
    }

    /**
     * Handles the display of the transaction history on the GUI.
     * The history is retrieved from the 'history' variable.
     */
    private void handleHistory() {
        // each history record is seperated by a semicolon
        String[] historyRecords = history.split(";");
        System.out.println(historyRecords.toString());
        System.out.println(historyRecords.length);
        for (int i = 0; i < historyRecords.length; i++) {
            switch (i) {
                case 0 -> {
                    if (historyRecords[i].equals("")) {
                        historyOperation1.setVisible(false);
                        break;
                    } else {
                        historyOperation1.setVisible(true);
                    }
                    historyOperation1.setText(extractHistory(historyRecords[i].split(" ")));
                }
                case 1 -> {
                    if (historyRecords[i].equals("")) {
                        historyOperation2.setVisible(false);
                        break;
                    } else {
                        historyOperation2.setVisible(true);
                    }
                    historyOperation2.setText(extractHistory(historyRecords[i].split(" ")));
                }
                case 2 -> {
                    if (historyRecords[i].equals("")) {
                        historyOperation3.setVisible(false);
                        break;
                    } else {
                        historyOperation3.setVisible(true);
                    }
                    historyOperation3.setText(extractHistory(historyRecords[i].split(" ")));
                }
                case 3 -> {
                    if (historyRecords[i].equals("")) {
                        historyOperation4.setVisible(false);
                        break;
                    } else {
                        historyOperation4.setVisible(true);
                    }
                    historyOperation4.setText(extractHistory(historyRecords[i].split(" ")));
                }
                case 4 -> {
                    if (historyRecords[i].equals("")) {
                        historyOperation5.setVisible(false);
                        break;
                    } else {
                        historyOperation5.setVisible(true);
                    }
                    historyOperation5.setText(extractHistory(historyRecords[i].split(" ")));
                }
            }
        }

    }

    /**
     * Handles the change in ATM state and updates the GUI accordingly.
     *
     * @param atmState The new ATM state.
     */
    public void handleAtmState(AtmState atmState) {
        System.out.println("Atm state changed to " + atmState);
        this.atmState = atmState;
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
            case AGAIN_PIN -> {
                setScreenAnchorPaneVisible(againpin, true);
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
            case DEPOSIT_INCORRECT_AMOUNT -> {
                setScreenAnchorPaneVisible(errorinputcash, true);
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
            case TOP_UP_PHONE_NUMBER -> {
                setScreenAnchorPaneVisible(givetele, true);
            }
            case TOP_UP_AMOUNT -> {
                setScreenAnchorPaneVisible(teleamount, true);
            }
            case TOP_UP_ONGOING -> {
                setScreenAnchorPaneVisible(wait, true);
            }
            case TOP_UP_SUCCESS -> {
                setScreenAnchorPaneVisible(telesuccess, true);
                setCreditCardVisible(true);
            }
            case PIN_CHANGE -> {
                setScreenAnchorPaneVisible(changepin, true);
            }
            case PIN_CHANGE_SUCCESS -> {
                setScreenAnchorPaneVisible(pinsuccess, true);
                setCreditCardVisible(true);
            }
            case INCORRECT_AMOUNT -> {
                setScreenAnchorPaneVisible(errorinputcash, true);
            }
            case PRINT_HISTORY_ONGOING -> {
                setScreenAnchorPaneVisible(operationprint, true);
                handleHistory();
            }
        }
    }
}
