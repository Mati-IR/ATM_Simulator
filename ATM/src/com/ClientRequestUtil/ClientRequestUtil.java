package com.ClientRequestUtil;

import com.MoneyInfoStorage.MoneyInfoStorage;

import java.util.HashMap;
import java.util.Map;

public class ClientRequestUtil {
    private boolean isRequestValid = false;
    private String selectedRequest = null;
    private String cardNumber = null;

    /* Authentication variables */
    private String pin = null;

    /* Withdrawal variables */
    private String amount = "0";
    private String phoneNumber = "";
    private MoneyInfoStorage moneyInfo = new MoneyInfoStorage();


    private static final Map<String, String> requestMappings = new HashMap<>();

    static {
        requestMappings.put("authenticate", "A");
        requestMappings.put("withdraw", "W");
        requestMappings.put("deposit", "D");
        requestMappings.put("balance", "B");
        requestMappings.put("history", "H");
        requestMappings.put("changepin", "C");
        requestMappings.put("topup", "T");
        requestMappings.put("success", "S");
        requestMappings.put("failure", "F");
        requestMappings.put("A", "authenticate");
        requestMappings.put("W", "withdraw");
        requestMappings.put("D", "deposit");
        requestMappings.put("B", "balance");
        requestMappings.put("H", "history");
        requestMappings.put("C", "changepin");
        requestMappings.put("T", "topup");
        requestMappings.put("S", "success");
        requestMappings.put("F", "failure");
    }


    public String encodeRequest() {
        try {

            switch (this.selectedRequest) {
                case "A": {
                    return "A " + this.pin + " " + this.cardNumber;
                }
                case "W": {
                    return "W " + this.cardNumber + " " + this.moneyInfo.getWholeUnits()+ " " + this.moneyInfo.getCurrency().toString();
                }
                case "D": {
                    return "D " + this.cardNumber + " " + this.moneyInfo.getWholeUnits()+ " " + this.moneyInfo.getCurrency().toString();
                }
                case "B": {
                    return "B " + this.cardNumber + " " + this.amount;
                }
                case "H": {
                    return "H " + this.cardNumber;
                }
                case "C": {
                    return "C " + this.cardNumber + " " + this.pin;
                }
                case "T": {
                    return "T " + this.cardNumber + " " + this.phoneNumber + " " + this.moneyInfo.getWholeUnits()+ " " + this.moneyInfo.getCurrency().toString();
                }
                case "S": {
                    return "S";
                }
                case "F": {
                    return "F";
                }
            }
        } catch (Exception e) {
            System.out.println("Error: " + e);
        }
        return "Error";
    }

    public void decodeRequest(String encodedRequest) {
        // Split the encoded request and perform decoding/validation
        String[] parts = encodedRequest.trim().split("\\s+");
        String operation = parts[0];

        switch (operation){
            case "A":
                this.selectedRequest = requestMappings.get(parts[0]);
                this.pin = decodePIN(parts[1]);
                this.cardNumber = decodeUserNumber(parts[2]);

                if (this.selectedRequest != null && this.pin != null && this.cardNumber != null) {
                    this.isRequestValid = true;
                    return;
                } else {
                    this.isRequestValid = false;
                    return;
                }
            case "W":
                this.selectedRequest = requestMappings.get(parts[0]);
                this.cardNumber = decodeUserNumber(parts[1]);
                this.amount = decodeAmount(parts[2]);
                this.moneyInfo.setCurrency(decodeCurrency(parts[3]));

                if (this.selectedRequest != null && this.cardNumber != null && this.amount != null && this.moneyInfo.getCurrency() != null) {
                    this.isRequestValid = true;
                    return;
                } else {
                    this.isRequestValid = false;
                    return;
                }
            case "D":{
                this.selectedRequest = requestMappings.get(parts[0]);
                this.cardNumber = decodeUserNumber(parts[1]);
                this.amount = decodeAmount(parts[2]);
                this.moneyInfo.setCurrency(decodeCurrency(parts[3]));

                if (this.selectedRequest != null && this.cardNumber != null && this.amount != null && this.moneyInfo.getCurrency() != null) {
                    this.isRequestValid = true;
                    return;
                } else {
                    this.isRequestValid = false;
                    return;
                }
            }
            case "B":{
                this.selectedRequest = requestMappings.get(parts[0]);
                this.cardNumber = decodeUserNumber(parts[1]);
                this.amount = decodeAmount(parts[2]);

                if (this.selectedRequest != null && this.cardNumber != null) {
                    this.isRequestValid = true;
                    return;
                } else {
                    this.isRequestValid = false;
                    return;
                }
            }
            case "T": {
                this.selectedRequest = requestMappings.get(parts[0]);
                this.cardNumber = decodeUserNumber(parts[1]);
                this.phoneNumber = decodePhoneNumber(parts[2]);
                this.amount = decodeAmount(parts[3]);
                this.moneyInfo.setCurrency(decodeCurrency(parts[4]));

                if (this.selectedRequest != null && this.cardNumber != null && this.amount != null && this.moneyInfo.getCurrency() != null && this.phoneNumber != null) {
                    this.isRequestValid = true;
                    return;
                } else {
                    this.isRequestValid = false;
                    return;
                }
            }
            case "S":
            case "F":
                this.selectedRequest = requestMappings.get(parts[0]);
                    this.isRequestValid = true;
                    return;
        }
    }

    private static String encodePIN(String pin) {
        // Perform PIN validation and return encoded value
        if (pin.matches("\\d{4}")) {
            return pin;
        }
        return null;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    private static String encodeCurrency(String currency) {
        // Perform currency validation and return encoded value
        if (currency.matches("EUR|PLN")) {
            return currency;
        }
        return null;
    }

    private String decodeArguments(String encodedArguments) {
        // Split the encoded arguments and perform decoding/validation
        String[] parts = encodedArguments.trim().split("\\s+");

        if (parts.length == 3) {
            this.pin = decodePIN(parts[0]);
            this.amount = decodeAmount(parts[1]);
            this.moneyInfo.setCurrency(decodeCurrency(parts[2]));

            if (this.pin != null && this.amount != null && this.moneyInfo.getCurrency() != null) {
                this.isRequestValid = true;
                return this.pin + " " + this.amount + " " + this.moneyInfo.getCurrency().toString();
            } else {
                this.isRequestValid = false;
                return "";
            }
        }

        return "";
    }

    private String decodePhoneNumber(String encodedPhoneNumber) {
        // Perform decoding/validation of phone number and return decoded value
        if (encodedPhoneNumber.matches("\\d+")) {
            return encodedPhoneNumber;
        }
        return null;
    }
    private static String decodePIN(String encodedPIN) {
        // Perform decoding/validation of PIN and return decoded value
        if (encodedPIN.matches("\\d{4}")) {
            return encodedPIN;
        }
        return null;
    }

    private static String decodeUserNumber(String encodedUserNumber) {
        // Perform decoding/validation of user number and return decoded value
        if (encodedUserNumber.matches("\\d+")) {
            return encodedUserNumber;
        }
        return null;
    }

    private static String decodeAmount(String encodedAmount) {
        // Perform decoding/validation of amount and return decoded value
        if (encodedAmount.matches("\\d+")) {
            return encodedAmount;
        }
        return null;
    }

    private static MoneyInfoStorage.Currency decodeCurrency(String encodedCurrency) {
        // Perform decoding/validation of currency and return decoded value
        if (encodedCurrency.matches("PLN")) {
            return MoneyInfoStorage.Currency.PLN;
        } else if (encodedCurrency.matches("EUR")) {
            return MoneyInfoStorage.Currency.EUR;
        }
        return null;
    }

    public void setMoneyInfo(MoneyInfoStorage moneyInfo) {
        this.moneyInfo = moneyInfo;
    }

    public void setRequest(String Request) {
        this.selectedRequest = requestMappings.get(Request.toLowerCase());
    }

    public void setPin(String pin) {
        this.pin = pin;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public void setCardNumber(String cardNumber) {
        this.cardNumber = cardNumber;
    }

    public boolean getIsRequestValid() {
        return this.isRequestValid;
    }

    public String getSelectedRequest() {
        return this.selectedRequest;
    }

    public String getCardNumber() {
        return this.cardNumber;
    }

    public MoneyInfoStorage.Currency getCurrency() {
        return this.moneyInfo.getCurrency();
    }

    public int getAmount() {
        return Integer.parseInt(this.amount);
    }

    public String getPin() {
        return this.pin;
    }

    public void clear() {
        this.selectedRequest = "";
        this.pin = "";
        this.cardNumber = "";
        this.amount = "";
        this.isRequestValid = false;
    }
}
