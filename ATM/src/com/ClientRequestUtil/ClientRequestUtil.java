package com.ClientRequestUtil;

import com.MoneyInfoStorage.MoneyInfoStorage;

import java.util.HashMap;
import java.util.Map;

/**
 * The ClientRequestUtil class provides utility methods for encoding and decoding client requests,
 * as well as managing request-related data.
 */
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
    private String history = "";


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


    /**
     * Encodes the selected request along with associated data into a formatted request string.
     *
     * @return The encoded request string.
     */
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
                    if (this.history == "") {
                        return "H " + this.cardNumber + " " + "empty";
                    }
                    return "H " + this.cardNumber + " " + this.history;
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

    /**
     * Decodes and validates an encoded request string.
     *
     * @param encodedRequest The encoded request string to decode.
     */
    public void decodeRequest(String encodedRequest) {
        // Split the encoded request and perform decoding/validation
        String[] parts = encodedRequest.trim().split("\\s+");
        String operation = parts[0];

        switch (operation){
            case "A":
                this.selectedRequest = requestMappings.get(parts[0]);
                this.pin = decodePIN(parts[1]);
                if (parts.length > 2) {
                    this.cardNumber = decodeUserNumber(parts[2]);
                } else {
                    this.cardNumber = null;
                }

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
            case "C": {
                this.selectedRequest = requestMappings.get(parts[0]);
                this.cardNumber = decodeUserNumber(parts[1]);
                this.pin = decodePIN(parts[2]);

                if (this.selectedRequest != null && this.cardNumber != null && this.pin != null) {
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
            case "H": {
                this.selectedRequest = requestMappings.get(parts[0]);
                this.cardNumber = decodeUserNumber(parts[1]);
                this.history = decodeHistory(parts);

                if (this.selectedRequest != null && this.cardNumber != null && this.history != null) {
                    this.isRequestValid = true;
                    return;
                } else {
                    this.isRequestValid = false;
                    return;
                }
            }
        }
    }

    /**
     * Encodes the given PIN value.
     *
     * @param pin the PIN value to encode
     * @return the encoded PIN value
     */
    private static String encodePIN(String pin) {
        // Perform PIN validation and return encoded value
        if (pin.matches("\\d{4}")) {
            return pin;
        }
        return null;
    }


    /**
     * Sets the amount.
     *
     * @param amount the amount to set
     */
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

    /**
     * Decodes the encoded arguments and performs decoding/validation.
     *
     * @param encodedArguments the encoded arguments to decode
     * @return the decoded arguments as a string
     */
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

    /**
     * Decodes the history from the encoded history array.
     *
     * @param encodedHistory the encoded history array to decode
     * @return the decoded history as a string
     */
    private static String decodeHistory(String encodedHistory[]) {
        // Perform decoding/validation of history and return decoded value
        //if history is one or more characters and includes both letters and numbers
        //ignore first two elements of array
        String resultHistory = "";
        for (int i = 2; i < encodedHistory.length; i++) {
            resultHistory += encodedHistory[i] + " ";
        }
        if (resultHistory.length() > 0) {
            return resultHistory;
        }
        return null;
    }

    /**
     * Decodes the encoded phone number.
     *
     * @param encodedPhoneNumber the encoded phone number to decode
     * @return the decoded phone number
     */
    private String decodePhoneNumber(String encodedPhoneNumber) {
        // Perform decoding/validation of phone number and return decoded value
        if (encodedPhoneNumber.matches("\\d+")) {
            return encodedPhoneNumber;
        }
        return null;
    }

    /**
     * Decodes the encoded PIN value.
     *
     * @param encodedPIN the encoded PIN value to decode
     * @return the decoded PIN value
     */
    private static String decodePIN(String encodedPIN) {
        // Perform decoding/validation of PIN and return decoded value
        if (encodedPIN.matches("\\d{4}")) {
            return encodedPIN;
        }
        return null;
    }

    /**
     * Decodes the encoded user number.
     *
     * @param encodedUserNumber the encoded user number to decode
     * @return the decoded user number
     */
    private static String decodeUserNumber(String encodedUserNumber) {
        // Perform decoding/validation of user number and return decoded value
        if (encodedUserNumber.matches("\\d+")) {
            return encodedUserNumber;
        }
        return null;
    }

    /**
     * Decodes the encoded amount value.
     *
     * @param encodedAmount the encoded amount value to decode
     * @return the decoded amount value
     */
    private static String decodeAmount(String encodedAmount) {
        // Perform decoding/validation of amount and return decoded value
        if (encodedAmount.matches("\\d+")) {
            return encodedAmount;
        }
        return null;
    }

    /**
     * Decodes the encoded currency value.
     *
     * @param encodedCurrency the encoded currency value to decode
     * @return the decoded currency value
     */
    private static MoneyInfoStorage.Currency decodeCurrency(String encodedCurrency) {
        // Perform decoding/validation of currency and return decoded value
        if (encodedCurrency.matches("PLN")) {
            return MoneyInfoStorage.Currency.PLN;
        } else if (encodedCurrency.matches("EUR")) {
            return MoneyInfoStorage.Currency.EUR;
        }
        return null;
    }

    /**
     * Sets the money information for the client request.
     *
     * @param moneyInfo The MoneyInfoStorage object containing the money information.
     */
    public void setMoneyInfo(MoneyInfoStorage moneyInfo) {
        this.moneyInfo = moneyInfo;
    }

    /**
     * Sets the selected request based on the provided request string.
     * The request string will be converted to lowercase before mapping.
     *
     * @param request The request string to set.
     */
    public void setRequest(String Request) {
        this.selectedRequest = requestMappings.get(Request.toLowerCase());
    }

    /**
     * Sets the PIN for authentication.
     *
     * @param pin The PIN to set.
     */
    public void setPin(String pin) {
        this.pin = pin;
    }

    /**
     * Sets the phone number for the client request.
     *
     * @param phoneNumber The phone number to set.
     */
    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    /**
     * Sets the card number for the client request.
     *
     * @param cardNumber The card number to set.
     */
    public void setCardNumber(String cardNumber) {
        this.cardNumber = cardNumber;
    }

    /**
     * Returns the status of the client request validity.
     *
     * @return true if the request is valid, false otherwise.
     */
    public boolean getIsRequestValid() {
        return this.isRequestValid;
    }

    /**
     * Returns the selected request.
     *
     * @return The selected request string.
     */
    public String getSelectedRequest() {
        return this.selectedRequest;
    }

    /**
     * Returns the card number associated with the client request.
     *
     * @return The card number.
     */
    public String getCardNumber() {
        return this.cardNumber;
    }

    /**
     * Returns the currency associated with the client request.
     *
     * @return The currency value as a MoneyInfoStorage.Currency enum.
     */
    public MoneyInfoStorage.Currency getCurrency() {
        return this.moneyInfo.getCurrency();
    }

    /**
     * Returns the amount associated with the client request as an integer value.
     *
     * @return The amount value.
     */
    public int getAmount() {
        return Integer.parseInt(this.amount);
    }

    /**
     * Returns the PIN associated with the client request.
     *
     * @return The PIN value.
     */
    public String getPin() {
        return this.pin;
    }

    /**
     * Sets the history string for the client request.
     *
     * @param history The history string to set.
     */
    public void setHistory(String history) {
        this.history = history;
    }

    /**
     * Returns the history string associated with the client request.
     *
     * @return The history string.
     */
    public String getHistory() {
        return this.history;
    }

    /**
     * Resets all the fields of the client request to their initial values.
     */
    public void clear() {
        this.selectedRequest = "";
        this.pin = "";
        this.cardNumber = "";
        this.amount = "";
        this.isRequestValid = false;
        this.history = "";
        this.phoneNumber = "";
    }
}
