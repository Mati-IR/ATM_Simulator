package com.Client.Peripherials;

import com.Client.ATMClient;
import com.Client.GUI.MainController;
import com.Client.Peripherials.KeyboardHandler.*;
import com.Client.Peripherials.SideButtonHandler.SideButtonState;
import com.ClientRequestUtil.ClientRequestUtil;
import com.MoneyInfoStorage.MoneyInfoStorage;

import java.util.Objects;

import static com.Client.Peripherials.SideButtonHandler.SideButtonState;
import static com.Client.Peripherials.SideButtonHandler.SideButtonState.*;
//import static com.Client.Peripherials.SideButtonHandler.SideButtonState.WITHDRAW_PLN;

public class PeripherialsHandler {
    public enum AtmState {
        HELLO, INPUT_PIN, AGAIN_PIN, AUTHENTICATION_ONGOING, OPERATION_CHOICE,
        WITHDRAW_PLN, WITHDRAW_EUR, DEPOSIT, TOP_UP_PHONE, BALANCE,
        OPERATION_PRINT, PIN_CHANGE, HOW_MANY_CASH, CHOICE_RECEIPT,
        CASH_INPUT_W, GIVE_TELE, TELE_AMOUNT, EXIT
    }
    private AtmState atmState = AtmState.HELLO;
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
            System.out.println("Message: " + message);
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
    public void handleSideButton(int buttonNumber,AtmState atmState ) {
        sideButtonHandler.handleSideButton(buttonNumber,atmState);
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
                SideButtonState sideButtonState = sideButtonHandler.getSideButtonState();
                switch (sideButtonState) {
                    case WITHDRAW_PLN -> {
                        atmState = AtmState.WITHDRAW_PLN;
                    }
                    case WITHDRAW_EUR -> {
                        atmState = AtmState.WITHDRAW_EUR;
                    }
                    case DEPOSIT -> {
                        atmState = AtmState.DEPOSIT;
                    }
                    case TOP_UP_PHONE -> {
                        atmState = AtmState.TOP_UP_PHONE;
                    }
                    case BALANCE -> {
                        atmState = AtmState.BALANCE;
                    }
                    case OPERATION_PRINT -> {
                        atmState = AtmState.OPERATION_PRINT;
                    }
                    case PIN_CHANGE -> {
                        atmState = AtmState.PIN_CHANGE;
                    }
                }

            }
        }


    }


    private void runState() {
        switch (atmState){
            case HELLO -> {
                controller.handleAtmState(atmState);
            }
            case INPUT_PIN -> {
                controller.handleAtmState(atmState);
                controller.setPinStars(keyboardHandler.getInput().length());
                atmClient.setRequest("authenticate");
                atmClient.setCardNumber(cardReaderHandler.getCardNumber());
                if (Objects.equals(keyboardHandler.getKeyboardState(), KeyboardState.ENTER)) {
                    atmClient.setPin(keyboardHandler.getInput());
                    atmClient.sendRequest();
                }
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
