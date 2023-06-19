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
        WITHDRAW_PLN, WITHDRAW_EUR, INSUFFICIENT_FUNDS, DEPOSIT, TOP_UP_PHONE, BALANCE_REQUEST_ONGOING, BALANCE, WITHDRAW_OTHER_AMOUNT,
        OPERATION_PRINT, PIN_CHANGE, AMOUNT_CHOICE, CHOOSE_RECEIPT, PRINT_RECEIPT, NO_RECEIPT, PRINT_CASH,
        CASH_INPUT_W, GIVE_TELE, TELE_AMOUNT, EXIT
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
                    atmState = AtmState.INPUT_PIN;
                    atmClient.setCardNumber(cardReaderHandler.getCardNumber());
                }
            }
            case INPUT_PIN -> {
                if (Objects.equals(keyboardHandler.getKeyboardState(), KeyboardState.ENTER)) {
                    atmState = AtmState.AUTHENTICATION_ONGOING;
                }
            }
            case AUTHENTICATION_ONGOING -> {
                if (clientRequestUtil.getIsRequestValid() && clientRequestUtil.getSelectedRequest().equalsIgnoreCase("success")) {
                    atmState = AtmState.OPERATION_CHOICE;
                } else if (clientRequestUtil.getIsRequestValid() && clientRequestUtil.getSelectedRequest().equalsIgnoreCase("failure")) {
                    atmState = AtmState.AGAIN_PIN;
                    System.out.println("Authentication failed");
                }
            }
            case OPERATION_CHOICE -> {
                requestActive = false;
                cardPressed = false;
                SideButtonState sideButtonState = sideButtonHandler.getSideButtonState();
                switch (sideButtonState) {
                    case WITHDRAW_PLN, WITHDRAW_EUR, DEPOSIT, TOP_UP_PHONE -> {
                        previousState = atmState;
                        atmState = AtmState.AMOUNT_CHOICE;
                    }
                    case BALANCE -> {
                        keyboardHandler.clear();
                        atmState = AtmState.BALANCE_REQUEST_ONGOING;
                    }
                    case OPERATION_PRINT -> {
                        atmState = AtmState.OPERATION_PRINT;
                    }
                    case PIN_CHANGE -> {
                        atmState = AtmState.PIN_CHANGE;
                    }
                }

            }
            case AMOUNT_CHOICE -> {
                if(SideButtonState.OTHER == sideButtonHandler.getSideButtonState()) {
                    atmState = AtmState.WITHDRAW_OTHER_AMOUNT;
                    return;
                }
                if ("success".equals(clientRequestUtil.getSelectedRequest())) {
                    atmState = AtmState.CHOOSE_RECEIPT;
                    requestActive = false;
                } else if ("failure".equals(clientRequestUtil.getSelectedRequest())) {
                    atmState = AtmState.INSUFFICIENT_FUNDS;
                    requestActive = false;
                }
            }
            case CHOOSE_RECEIPT -> {
                if (Objects.equals(sideButtonHandler.getSideButtonState(), SideButtonState.YES)) {
                    atmState = AtmState.PRINT_RECEIPT;
                } else if (Objects.equals(sideButtonHandler.getSideButtonState(), SideButtonState.NO)) {
                    atmState = AtmState.NO_RECEIPT;
                }
            }
            case NO_RECEIPT, PRINT_RECEIPT -> {
                if (true == cashPressed) {
                    atmState = AtmState.HELLO;
                }
                cashPressed = false;
            }
            case INSUFFICIENT_FUNDS -> {
                if (true == cardPressed) {
                    atmState = AtmState.HELLO;
                }
            }
            case BALANCE_REQUEST_ONGOING -> {
                if ("balance".equals(clientRequestUtil.getSelectedRequest()) || "failure".equals(clientRequestUtil.getSelectedRequest()) || "success".equals(clientRequestUtil.getSelectedRequest())) {
                    atmState = AtmState.BALANCE;
                    requestActive = false;
                }
            }
            case BALANCE -> {
                if (keyboardHandler.getKeyboardState() != KeyboardState.OK || keyboardHandler.getInput() != "") {
                    atmState = AtmState.HELLO;
                }
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
            case CHOOSE_RECEIPT -> {
                requestActive = false;
                controller.handleAtmState(atmState);
            }
            case INSUFFICIENT_FUNDS -> {
                requestActive = false;
                controller.handleAtmState(atmState);
            }
            case AMOUNT_CHOICE -> {
                controller.handleAtmState(atmState);
                if (true == isSideButtonStateAnAmountChoice(sideButtonHandler.getSideButtonState()) && false == requestActive) {
                    moneyInfoStorage.setWholeUnits(Long.parseLong(sideButtonHandler.getAmount()));
                    atmClient.setMoneyInfo(moneyInfoStorage);
                    atmClient.setRequest("withdraw");
                    atmClient.sendRequest();
                    requestActive = true;
                }
            }
            case NO_RECEIPT -> {
                controller.handleAtmState(atmState);
                requestActive = false;
                if (true == cardPressed) {
                    controller.setCreditCardVisible(false);
                    controller.setReceiptVisible(false);
                    controller.setCashVisible(true);
                }
                cashPressed = false;
            }

            case PRINT_RECEIPT -> {
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
