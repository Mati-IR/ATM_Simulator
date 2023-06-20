package com.Client.Peripherials;
import com.Client.Peripherials.PeripherialsHandler.AtmState;

import static com.Client.Peripherials.PeripherialsHandler.AtmState.*;

public class SideButtonHandler {
    public enum SideButtonState {
        EMPTY, OK, YES, NO , CHOICE_50, CHOICE_100, CHOICE_150, CHOICE_200, CHOICE_300, CHOICE_400, CHOICE_500, OTHER,
        WITHDRAW_PLN, WITHDRAW_EUR, DEPOSIT, TOP_UP_PHONE, BALANCE,
        OPERATION_PRINT, PIN_CHANGE, ERROR
    }
   private SideButtonState sideButtonState = SideButtonState.EMPTY;
    public void handleSideButton(int buttonNumber, AtmState atmState) {
        switch (buttonNumber){
            case 1 -> {
                if (atmState == OPERATION_CHOICE)
                    sideButtonState = SideButtonState.WITHDRAW_PLN;
                else if (atmState == WITHDRAW_AMOUNT_CHOICE)
                    sideButtonState = SideButtonState.CHOICE_50;
                else
                    sideButtonState = SideButtonState.EMPTY;
            }
            case 2 -> {
                if (atmState == OPERATION_CHOICE)
                    sideButtonState = SideButtonState.WITHDRAW_EUR;
                else if (atmState == WITHDRAW_AMOUNT_CHOICE)
                    sideButtonState = SideButtonState.CHOICE_100;
                else if (atmState == CHOOSE_RECEIPT_WITHDRAWAL || atmState == CHOOSE_RECEIPT_DEPOSIT)
                    sideButtonState = SideButtonState.YES;
                else
                    sideButtonState = SideButtonState.EMPTY;
            }
            case 3 -> {
                if (atmState == OPERATION_CHOICE)
                    sideButtonState = SideButtonState.BALANCE;
                else if (atmState == WITHDRAW_AMOUNT_CHOICE)
                    sideButtonState = SideButtonState.CHOICE_150;
                else
                    sideButtonState = SideButtonState.EMPTY;
            }
            case 4 -> {
                if (atmState == OPERATION_CHOICE)
                    sideButtonState = SideButtonState.DEPOSIT;
                else if (atmState == WITHDRAW_AMOUNT_CHOICE)
                    sideButtonState = SideButtonState.CHOICE_200;
                else
                    sideButtonState = SideButtonState.EMPTY;
            }
            case 5 -> {
                if (atmState == OPERATION_CHOICE)
                    sideButtonState = SideButtonState.TOP_UP_PHONE;
                else if (atmState == WITHDRAW_AMOUNT_CHOICE)
                    sideButtonState = SideButtonState.CHOICE_300;
                else
                    sideButtonState = SideButtonState.EMPTY;
            }
            case 6 -> {
                if (atmState == INPUT_PIN || atmState == AGAIN_PIN || atmState == INPUT_CASH
                        || atmState == PIN_CHANGE || atmState == GIVE_TELE
                        || atmState == TELE_AMOUNT || atmState == DEPOSIT_AMOUNT_CHOICE)
                    sideButtonState = SideButtonState.OK;
                else if (atmState == OPERATION_CHOICE)
                    sideButtonState = SideButtonState.PIN_CHANGE;
                else if (atmState == WITHDRAW_AMOUNT_CHOICE)
                    sideButtonState = SideButtonState.CHOICE_400;
                else if (atmState == CHOOSE_RECEIPT_WITHDRAWAL || atmState == CHOOSE_RECEIPT_DEPOSIT)
                    sideButtonState = SideButtonState.NO;
                else
                    sideButtonState = SideButtonState.EMPTY;
            }
            case 7 -> {
                if (atmState == OPERATION_CHOICE)
                    sideButtonState = SideButtonState.OPERATION_PRINT;
                else if (atmState == WITHDRAW_AMOUNT_CHOICE)
                    sideButtonState = SideButtonState.CHOICE_500;
                else
                    sideButtonState = SideButtonState.EMPTY;
            }

            case 8 ->{
                if (atmState == WITHDRAW_AMOUNT_CHOICE)
                    sideButtonState = SideButtonState.OTHER;
                else
                    sideButtonState = SideButtonState.EMPTY;
            }
            default -> {
                sideButtonState = SideButtonState.ERROR;
            }
        }
    }

    public SideButtonState getSideButtonState() {
        return sideButtonState;
    }

    public String getAmount() {
        switch (sideButtonState) {
            case CHOICE_50:
                return "50";
            case CHOICE_100:
                return "100";
            case CHOICE_150:
                return "150";
            case CHOICE_200:
                return "200";
            case CHOICE_300:
                return "300";
            case CHOICE_400:
                return "400";
            case CHOICE_500:
                return "500";
            default:
                return "";
        }
    }
}
