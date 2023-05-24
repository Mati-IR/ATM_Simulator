package com.ServerPackage;

import MoneyInfoStorage.MoneyInfoStorage;

public class ServerResponse {
    public enum OperationType {
        WITHDRAW_PLN, WITHDRAW_EUR, DEPOSIT_PLN, GET_BALANCE, GET_HISTORY, CHANGE_PIN, TOP_UP_PHONE
    }

    private OperationType operationType;
    private MoneyInfoStorage moneyInfoStorage; /** This is the amount of money to be withdrawn or deposited */

public ServerResponse(String request){

    }
}
