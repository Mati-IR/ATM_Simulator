package com.Client.Peripherials;


public class SideButtonHandler {
    public enum ButtonState {
        EMPTY, OK, YES, NO , 50, 100, 150, 200, 300, 400, 500, OTHER,
        WITHDRAW_PLN, WITHDRAW_EUR, DEPOSIT, TOP_UP_PHONE, BALANCE,
        OPERATION_PRINT, PIN_CHANGE, ERROR
    }
   private ButtonState buttonState = ButtonState.EMPTY;
    public void handleSideButton(int buttonNumber,AtmState atmState) {
        switch (buttonNumber){
            case 1:
                if(atmState==OPERATION_CHOICE)
                    buttonState=ButtonState.WITHDRAW_PLN;
                else if (atmState==HOW_MANY_CASH)
                    buttonState=ButtonState.50;
                else
                    buttonState=ButtonState.EMPTY;
            case 2:
                if(atmState==OPERATION_CHOICE)
                    buttonState=ButtonState.WITHDRAW_EUR;
                else if (atmState==HOW_MANY_CASH)
                    buttonState=ButtonState.100;
                else if (atmState==CHOICE_RECEIPT)
                    buttonState=ButtonState.YES;
                else
                    buttonState=ButtonState.EMPTY;
            case 3:
                if(atmState==OPERATION_CHOICE)
                    buttonState=ButtonState.BALANCE;
                else if (atmState==HOW_MANY_CASH)
                    buttonState=ButtonState.150;
                else
                    buttonState=ButtonState.EMPTY;
            case 4:
                if(atmState==OPERATION_CHOICE)
                    buttonState=ButtonState.DEPOSIT;
                else if (atmState==HOW_MANY_CASH)
                    buttonState=ButtonState.200;
                else
                    buttonState=ButtonState.EMPTY;
            case 5:
                if(atmState==OPERATION_CHOICE)
                    buttonState=ButtonState.TOP_UP_PHONE;
                else if (atmState==HOW_MANY_CASH)
                    buttonState=ButtonState.300;
                else
                    buttonState=ButtonState.EMPTY;
            case 6:
                if(atmState==INPUT_PIN || atmState==AGAIN_PIN || atmState==CASH_INPUT_W
                        || atmState==PIN_CHANGE || atmState==GIVE_TELE
                        || atmState==TELE_AMOUNT || atmState==DEPOSIT)
                    buttonState=ButtonState.OK;
                else if (atmState==OPERATION_CHOICE)
                    buttonState=ButtonState.PIN_CHANGE;
                else if (atmState==HOW_MANY_CASH)
                    buttonState=ButtonState.400;
                else if (atmState==CHOICE_RECEIPT)
                    buttonState=ButtonState.NO;
                else
                    buttonState=ButtonState.EMPTY;
            case 7:
                if(atmState==OPERATION_CHOICE)
                    buttonState=ButtonState.OPERATION_PRINT;
                else if (atmState==HOW_MANY_CASH)
                    buttonState=ButtonState.500;
                else
                    buttonState=ButtonState.EMPTY;
            case 8:
                if(atmState==HOW_MANY_CASH)
                    buttonState=ButtonState.OTHER;
                else
                    buttonState=ButtonState.EMPTY;
            default:
                buttonState = ButtonState.ERROR;
                break;
        }
    }
}
