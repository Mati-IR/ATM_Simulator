package com.Client.Peripherials;

import com.Client.ATMClient;
import com.Client.GUI.MainController;
import com.Client.Peripherials.KeyboardHandler.*;
import com.Client.Peripherials.SideButtonHandler.SideButtonState;
import com.ClientRequestUtil.ClientRequestUtil;
import com.MoneyInfoStorage.MoneyInfoStorage;

import java.util.Objects;
//import static com.Client.Peripherials.SideButtonHandler.SideButtonState.WITHDRAW_PLN;

public class PeripherialsHandler {
    public enum AtmState {
        HELLO, INPUT_PIN, AGAIN_PIN, AUTHENTICATION_ONGOING, OPERATION_CHOICE,
        WITHDRAW_PLN, WITHDRAW_EUR, INSUFFICIENT_FUNDS, DEPOSIT_AMOUNT_CHOICE, DEPOSIT_OTHER_AMOUNT, DEPOSIT_INCORRECT_AMOUNT, BALANCE_REQUEST_ONGOING, BALANCE, WITHDRAW_OTHER_AMOUNT, INCORRECT_AMOUNT,
        PRINT_HISTORY_ONGOING, PIN_CHANGE, PIN_CHANGE_SUCCESS, WITHDRAW_AMOUNT_CHOICE, CHOOSE_RECEIPT_WITHDRAWAL, CHOOSE_RECEIPT_DEPOSIT, PRINT_RECEIPT_WITHDRAWAL, NO_RECEIPT_WITHDRAWAL, PRINT_RECEIPT_DEPOSIT, NO_RECEIPT_DEPOSIT, PRINT_CASH,
        TOP_UP_PHONE_NUMBER, TOP_UP_ONGOING, TOP_UP_AMOUNT, TOP_UP_SUCCESS, EXIT
    }
    private AtmState atmState = AtmState.HELLO;
    private AtmState previousState = AtmState.HELLO;

    private ATMClient atmClient;
    private MoneyInfoStorage moneyInfoStorage = new MoneyInfoStorage();
    private static PeripherialsHandler instance;
    private KeyboardHandler keyboardHandler = new KeyboardHandler();
    private CardReaderHandler cardReaderHandler = new CardReaderHandler();
    private SideButtonHandler sideButtonHandler = new SideButtonHandler();
    private MainController controller;
    private ClientRequestUtil clientRequestUtil = new ClientRequestUtil();

    private boolean firstRun = true;
    private boolean requestActive = false;
    private boolean cashPressed = false;
    private boolean cardPressed = false;

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

    public void setMessage(String message) {
        if (message != null) {
            this.clientRequestUtil.decodeRequest(message);
            //System.out.println("Message: " + message);
        }
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
        cardPressed = true;
        cardReaderHandler.handleCardReaderInput(cardNumber);
        run();
    }

    public String getCardNumber() {
        return cardReaderHandler.getCardNumber();
    }

    public void clearCardReader() {
        cardReaderHandler.clear();
    }

    /****** Side buttons ******/
    public void handleSideButton(int buttonNumber) {
        sideButtonHandler.handleSideButton(buttonNumber, atmState);
        run();
    }

    /****** Cash ******/
    public void handleCashButton() {
        cashPressed = true;
        run();
    }


    /* STATE MACHINE */
    private boolean isSideButtonStateAnAmountChoice(SideButtonState state) {
        switch (state) {
            case CHOICE_50, CHOICE_100, CHOICE_150, CHOICE_200, CHOICE_300, CHOICE_400, CHOICE_500 -> {
                return true;
            }
            default -> {
                return false;
            }
        }
    }

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
                    previousState = atmState;
                    atmState = AtmState.INPUT_PIN;
                    atmClient.setCardNumber(cardReaderHandler.getCardNumber());
                }
            }
            case INPUT_PIN -> {
                if (Objects.equals(keyboardHandler.getKeyboardState(), KeyboardState.ENTER)) {
                    previousState = atmState;
                    atmState = AtmState.AUTHENTICATION_ONGOING;
                }
            }
            case AGAIN_PIN -> {
                if (Objects.equals(keyboardHandler.getKeyboardState(), KeyboardState.ENTER)) {
                    previousState = atmState;
                    atmState = AtmState.INPUT_PIN;
                    requestActive = false;
                }
            }
            case AUTHENTICATION_ONGOING -> {
                if (clientRequestUtil.getIsRequestValid() && clientRequestUtil.getSelectedRequest().equalsIgnoreCase("success")) {
                    previousState = atmState;
                    atmState = AtmState.OPERATION_CHOICE;
                } else if (clientRequestUtil.getIsRequestValid() && clientRequestUtil.getSelectedRequest().equalsIgnoreCase("failure")) {
                    previousState = atmState;
                    atmState = AtmState.AGAIN_PIN;
                    System.out.println("Authentication failed");
                }
            }
            case OPERATION_CHOICE -> {
                requestActive = false;
                cardPressed = false;
                SideButtonState sideButtonState = sideButtonHandler.getSideButtonState();
                switch (sideButtonState) {
                    case WITHDRAW_PLN -> {
                        this.moneyInfoStorage.setCurrency(MoneyInfoStorage.Currency.PLN);
                        previousState = atmState;
                        atmState = AtmState.WITHDRAW_AMOUNT_CHOICE;
                    }
                    case WITHDRAW_EUR -> {
                        this.moneyInfoStorage.setCurrency(MoneyInfoStorage.Currency.EUR);
                        previousState = atmState;
                        atmState = AtmState.WITHDRAW_AMOUNT_CHOICE;
                    }
                    case DEPOSIT -> {
                        previousState = atmState;
                        previousState = atmState;
                        atmState = AtmState.DEPOSIT_AMOUNT_CHOICE;
                    }
                    case BALANCE -> {
                        previousState = atmState;
                        keyboardHandler.clear();
                        atmState = AtmState.BALANCE_REQUEST_ONGOING;
                    }
                    case OPERATION_PRINT -> {
                        previousState = atmState;
                        requestActive = false;
                        atmState = AtmState.PRINT_HISTORY_ONGOING;
                    }
                    case PIN_CHANGE -> {
                        previousState = atmState;
                        atmState = AtmState.PIN_CHANGE;
                    }
                    case TOP_UP_PHONE -> {
                        previousState = atmState;
                        atmState = AtmState.TOP_UP_PHONE_NUMBER;
                    }
                }
            }
            case WITHDRAW_AMOUNT_CHOICE -> {
                if(SideButtonState.OTHER == sideButtonHandler.getSideButtonState()) {
                    previousState = atmState;
                    keyboardHandler.clear();
                    atmState = AtmState.WITHDRAW_OTHER_AMOUNT;
                    return;
                }
                if ("success".equals(clientRequestUtil.getSelectedRequest())) {
                    previousState = atmState;
                    atmState = AtmState.CHOOSE_RECEIPT_WITHDRAWAL;
                    requestActive = false;
                } else if ("failure".equals(clientRequestUtil.getSelectedRequest())) {
                    previousState = atmState;
                    atmState = AtmState.INSUFFICIENT_FUNDS;
                    requestActive = false;
                }
            }
            case WITHDRAW_OTHER_AMOUNT -> {
                if (Objects.equals(keyboardHandler.getKeyboardState(), KeyboardState.ENTER)) {
                    if (Integer.parseInt(keyboardHandler.getInput()) % 10 != 0 /* Input not divisible by 10 */
                    ||  keyboardHandler.getInput().matches("^0.*")       /* Input starts with 0 */
                    || keyboardHandler.getInput().equals("")) {                /* Input is empty */
                        previousState = atmState;
                        atmState = AtmState.INCORRECT_AMOUNT;
                        keyboardHandler.clear();
                    } else {
                        previousState = atmState;
                        atmState = AtmState.CHOOSE_RECEIPT_WITHDRAWAL;
                    }
                }
            }
            case INCORRECT_AMOUNT -> {
                if (Objects.equals(keyboardHandler.getKeyboardState(), KeyboardState.ENTER)) {
                    previousState = atmState;
                    keyboardHandler.clear();
                    atmState = AtmState.WITHDRAW_OTHER_AMOUNT;
                }
            }
            case DEPOSIT_AMOUNT_CHOICE -> {
                if(keyboardHandler.getKeyboardState() == KeyboardState.ENTER) {
                try {
                    if (Integer.parseInt(keyboardHandler.getInput()) % 10 != 0 /* Input not divisible by 10 */
                            || keyboardHandler.getInput().matches("^0.*")       /* Input starts with 0 */
                            || keyboardHandler.getInput().equals("")) {                /* Input is empty */
                        previousState = atmState;
                        atmState = AtmState.DEPOSIT_INCORRECT_AMOUNT;
                        keyboardHandler.clear();
                        return;
                    }
                } catch (RuntimeException e) {
                    previousState = atmState;
                    atmState = AtmState.DEPOSIT_INCORRECT_AMOUNT;
                    keyboardHandler.clear();
                    return;
                }
                }
                if ("success".equals(clientRequestUtil.getSelectedRequest())) {
                    previousState = atmState;
                    atmState = AtmState.CHOOSE_RECEIPT_DEPOSIT;
                    requestActive = false;
                } else if ("failure".equals(clientRequestUtil.getSelectedRequest())) {
                    previousState = atmState;
                    atmState = AtmState.DEPOSIT_INCORRECT_AMOUNT;
                    requestActive = false;
                }
            }
            case DEPOSIT_INCORRECT_AMOUNT -> {
                if (Objects.equals(keyboardHandler.getKeyboardState(), KeyboardState.ENTER)) {
                    previousState = atmState;
                    keyboardHandler.clear();
                    atmState = AtmState.DEPOSIT_AMOUNT_CHOICE;
                }
            }
            case CHOOSE_RECEIPT_WITHDRAWAL, CHOOSE_RECEIPT_DEPOSIT -> {
                if (previousState == AtmState.WITHDRAW_AMOUNT_CHOICE || previousState == AtmState.WITHDRAW_OTHER_AMOUNT) {
                    if (Objects.equals(sideButtonHandler.getSideButtonState(), SideButtonState.YES)) {
                        previousState = atmState;
                        atmState = AtmState.PRINT_RECEIPT_WITHDRAWAL;
                        break;
                    } else if (Objects.equals(sideButtonHandler.getSideButtonState(), SideButtonState.NO)) {
                        previousState = atmState;
                        atmState = AtmState.NO_RECEIPT_WITHDRAWAL;
                        break;
                    }
                } else if (previousState == AtmState.DEPOSIT_AMOUNT_CHOICE || previousState == AtmState.DEPOSIT_OTHER_AMOUNT) {
                    if (Objects.equals(sideButtonHandler.getSideButtonState(), SideButtonState.YES)) {
                        previousState = atmState;
                        atmState = AtmState.PRINT_RECEIPT_DEPOSIT;
                        break;
                    } else if (Objects.equals(sideButtonHandler.getSideButtonState(), SideButtonState.NO)) {
                        previousState = atmState;
                        atmState = AtmState.NO_RECEIPT_DEPOSIT;
                        break;
                    }
                }
                if (Objects.equals(sideButtonHandler.getSideButtonState(), SideButtonState.YES)) {
                    previousState = atmState;
                    atmState = AtmState.PRINT_RECEIPT_DEPOSIT;
                } else if (Objects.equals(sideButtonHandler.getSideButtonState(), SideButtonState.NO)) {
                    previousState = atmState;
                    atmState = AtmState.NO_RECEIPT_DEPOSIT;
                }
            }
            case NO_RECEIPT_WITHDRAWAL, PRINT_RECEIPT_WITHDRAWAL -> {
                if (true == cardPressed && true == cashPressed) {
                    previousState = atmState;
                    keyboardHandler.clear();
                    atmState = AtmState.INPUT_PIN;
                    cashPressed = false;
                    cardPressed = false;
                }

            }
            case INSUFFICIENT_FUNDS, PRINT_RECEIPT_DEPOSIT, NO_RECEIPT_DEPOSIT -> {
                if (true == cardPressed) {
                    previousState = atmState;
                    atmState = AtmState.HELLO;
                }
                cardPressed = false;
            }
            case BALANCE_REQUEST_ONGOING -> {
                if ("balance".equals(clientRequestUtil.getSelectedRequest()) || "failure".equals(clientRequestUtil.getSelectedRequest()) || "success".equals(clientRequestUtil.getSelectedRequest())) {
                    previousState = atmState;
                    atmState = AtmState.BALANCE;
                    requestActive = false;
                }
            }
            case BALANCE -> {
                if (keyboardHandler.getKeyboardState() != KeyboardState.OK || keyboardHandler.getInput() != "") {
                    atmState = AtmState.HELLO;
                }
            }
            case TOP_UP_PHONE_NUMBER -> {
                if ((KeyboardState.ENTER == keyboardHandler.getKeyboardState() || SideButtonState.OK == sideButtonHandler.getSideButtonState())
                    && keyboardHandler.getInput() != "" && false == requestActive) {
                    atmClient.setPhoneNumber(keyboardHandler.getInput());
                    keyboardHandler.clear();
                    sideButtonHandler.clear();
                    atmState = AtmState.TOP_UP_AMOUNT;
                }
            }
            case TOP_UP_AMOUNT -> {
                if ((SideButtonState.OK == sideButtonHandler.getSideButtonState() || KeyboardState.ENTER == keyboardHandler.getKeyboardState()) && false == requestActive) {
                    atmState = AtmState.TOP_UP_ONGOING;
                    requestActive = false;
                }
            }
            case TOP_UP_ONGOING -> {
                if ("success".equals(clientRequestUtil.getSelectedRequest())) {
                    previousState = atmState;
                    atmState = AtmState.TOP_UP_SUCCESS;
                    requestActive = false;
                } else if ("failure".equals(clientRequestUtil.getSelectedRequest())) {
                    previousState = atmState;
                    atmState = AtmState.INSUFFICIENT_FUNDS;
                    requestActive = false;
                }
            }
            case TOP_UP_SUCCESS -> {
                if (true == cardPressed) {
                    previousState = atmState;
                    atmState = AtmState.HELLO;
                }
                cardPressed = false;
            }
            case PIN_CHANGE -> {
                controller.handleAtmState(atmState);
                if ("success".equals(clientRequestUtil.getSelectedRequest())) {
                    previousState = atmState;
                    atmState = AtmState.PIN_CHANGE_SUCCESS;
                    requestActive = false;
                } else if ("failure".equals(clientRequestUtil.getSelectedRequest())) {
                    previousState = atmState;
                    atmState = AtmState.AGAIN_PIN;
                    requestActive = false;
                }
            }
            case PIN_CHANGE_SUCCESS -> {
                if (true == cardPressed) {
                    keyboardHandler.clear();
                    previousState = atmState;
                    atmState = AtmState.HELLO;
                }
                cardPressed = false;
            }
            case PRINT_HISTORY_ONGOING -> {

            }
        }


    }


    private void runState() {
        switch (atmState){
            case HELLO -> {
                controller.handleAtmState(atmState);
                requestActive = false;
                cardPressed = false;
                cashPressed = false;
                keyboardHandler.clear();
            }
            case INPUT_PIN -> {
                controller.handleAtmState(atmState);
                controller.setPinStars(keyboardHandler.getInput().length());
                atmClient.setRequest("authenticate");
                atmClient.setCardNumber(cardReaderHandler.getCardNumber());
            }
            case AGAIN_PIN -> {
                controller.handleAtmState(atmState);
                keyboardHandler.clear();
                requestActive = false;
            }
            case AUTHENTICATION_ONGOING -> {
                atmClient.setPin(keyboardHandler.getInput());
                controller.handleAtmState(atmState);
                if (false == requestActive) {
                    atmClient.sendRequest();
                    requestActive = true;
                }
            }
            case OPERATION_CHOICE -> {
                requestActive = false;
                controller.handleAtmState(atmState);
                clientRequestUtil.clear();
                keyboardHandler.clear();
            }
            case WITHDRAW_PLN -> {
                moneyInfoStorage.setCurrency(MoneyInfoStorage.Currency.PLN);
                controller.handleAtmState(atmState);
                atmClient.setRequest("withdraw");
                if (true == isSideButtonStateAnAmountChoice(sideButtonHandler.getSideButtonState()) && false == requestActive) {
                    moneyInfoStorage.setWholeUnits(Long.parseLong(sideButtonHandler.getAmount()));
                    atmClient.setMoneyInfo(moneyInfoStorage);
                    atmClient.sendRequest();
                    requestActive = true;
                }
            }
            case WITHDRAW_EUR -> {
                moneyInfoStorage.setCurrency(MoneyInfoStorage.Currency.EUR);
                controller.handleAtmState(atmState);
                atmClient.setRequest("withdraw");
                if (true == isSideButtonStateAnAmountChoice(sideButtonHandler.getSideButtonState()) && false == requestActive) {
                    moneyInfoStorage.setWholeUnits(Long.parseLong(sideButtonHandler.getAmount()));
                    atmClient.setMoneyInfo(moneyInfoStorage);
                    atmClient.sendRequest();
                    requestActive = true;
                }
            }
            case WITHDRAW_OTHER_AMOUNT -> {
                controller.handleAtmState(atmState);
                atmClient.setRequest("withdraw");
                if (KeyboardState.ENTER == keyboardHandler.getKeyboardState()
                && false == requestActive) {
                    moneyInfoStorage.setWholeUnits(Long.parseLong(sideButtonHandler.getAmount()));
                    atmClient.setMoneyInfo(moneyInfoStorage);
                    atmClient.sendRequest();
                    requestActive = true;
                } else {
                    controller.setCashOutAmount(keyboardHandler.getInput());
                }
            }
            case INCORRECT_AMOUNT -> {
                keyboardHandler.clear();
                controller.handleAtmState(atmState);
            }
            case CHOOSE_RECEIPT_WITHDRAWAL -> {
                requestActive = false;
                controller.handleAtmState(atmState);
            }
            case INSUFFICIENT_FUNDS -> {
                requestActive = false;
                controller.handleAtmState(atmState);
            }
            case WITHDRAW_AMOUNT_CHOICE -> {
                controller.handleAtmState(atmState);
                if (true == isSideButtonStateAnAmountChoice(sideButtonHandler.getSideButtonState()) && false == requestActive) {
                    moneyInfoStorage.setWholeUnits(Long.parseLong(sideButtonHandler.getAmount()));
                    atmClient.setMoneyInfo(moneyInfoStorage);
                    atmClient.setRequest("withdraw");
                    atmClient.sendRequest();
                    requestActive = true;
                }
            }
            case DEPOSIT_AMOUNT_CHOICE -> {
                controller.handleAtmState(atmState);
                controller.setDepostitAmount(keyboardHandler.getInput());
                if (KeyboardState.ENTER == keyboardHandler.getKeyboardState() && false == requestActive) {
                    moneyInfoStorage.setWholeUnits(Long.parseLong(keyboardHandler.getInput()));
                    atmClient.setMoneyInfo(moneyInfoStorage);
                    atmClient.setRequest("deposit");
                    atmClient.sendRequest();
                    requestActive = true;
                }
            }
            case DEPOSIT_INCORRECT_AMOUNT -> {
                controller.handleAtmState(atmState);
                keyboardHandler.clear();
            }
            case CHOOSE_RECEIPT_DEPOSIT -> {
                requestActive = false;
                controller.handleAtmState(atmState);
            }
            case NO_RECEIPT_DEPOSIT, PRINT_RECEIPT_DEPOSIT -> {
                controller.setDepositedCash(keyboardHandler.getInput());
                controller.handleAtmState(atmState);
                requestActive = false;
            }
            case NO_RECEIPT_WITHDRAWAL -> {
                controller.handleAtmState(atmState);
                requestActive = false;
                if (true == cardPressed) {
                    controller.setReceiptVisible(false);
                    controller.setCashVisible(true);
                }
                if (true == cashPressed) {
                    controller.setCashVisible(false);
                }
            }
            case PRINT_RECEIPT_WITHDRAWAL -> {
                controller.handleAtmState(atmState);
                requestActive = false;
                if (true == cardPressed) {
                    controller.setCreditCardVisible(false);
                    controller.setReceiptVisible(true);
                    controller.setCashVisible(true);
                }
                cashPressed = false;
            }
            case BALANCE_REQUEST_ONGOING -> {
                atmClient.setRequest("balance");
                if (false == requestActive) {
                    atmClient.sendRequest();
                    requestActive = true;
                }
            }
            case BALANCE -> {
                controller.handleAtmState(atmState);
                controller.setAccountbalance(Integer.toString(clientRequestUtil.getAmount()));
                requestActive = false;
            }
            case TOP_UP_PHONE_NUMBER -> {
                controller.handleAtmState(atmState);
                controller.setPhoneNumber(keyboardHandler.getInput());
                atmClient.setRequest("topup");
            }
            case TOP_UP_AMOUNT -> {
                controller.handleAtmState(atmState);
                controller.setPhoneAmount(keyboardHandler.getInput());
            }
            case TOP_UP_ONGOING -> {
                controller.handleAtmState(atmState);
                if ((SideButtonState.OK == sideButtonHandler.getSideButtonState() || KeyboardState.ENTER == keyboardHandler.getKeyboardState()) && false == requestActive) {
                    moneyInfoStorage.setWholeUnits(Long.parseLong(keyboardHandler.getInput()));
                    atmClient.setMoneyInfo(moneyInfoStorage);
                    atmClient.sendRequest();
                    requestActive = true;
                }
            }
            case TOP_UP_SUCCESS -> {
                controller.handleAtmState(atmState);
                requestActive = false;
            }
            case PIN_CHANGE -> {
                controller.setNewPin(keyboardHandler.getInput());
                controller.handleAtmState(atmState);
                atmClient.setRequest("changepin");
                if ((SideButtonState.OK == sideButtonHandler.getSideButtonState()
                || KeyboardState.ENTER == keyboardHandler.getKeyboardState())
                && false == requestActive) {
                    atmClient.setPin(keyboardHandler.getInput());
                    atmClient.sendRequest();
                    requestActive = true;
                }
            }
            case PIN_CHANGE_SUCCESS -> {
                controller.handleAtmState(atmState);
                requestActive = false;
            }
            case PRINT_HISTORY_ONGOING -> {
                if (false == requestActive) {
                    requestActive = true;
                    atmClient.setRequest("history");
                    atmClient.sendRequest();
                    controller.setHistory(clientRequestUtil.getHistory());
                }
                controller.handleAtmState(atmState);
            }
        }
        /* It is forbidden to write any instructions after this line in this method */
    }

    public void run() {
        if (firstRun) {
            firstRun = false;
            atmClient = ATMClient.getInstance();
        }
        evaluateStateChange();
        runState();
        /* It is forbidden to write any instructions after this line in this method */
    }
}
