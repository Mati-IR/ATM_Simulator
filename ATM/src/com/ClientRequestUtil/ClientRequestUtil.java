package ClientRequestUtil;

import MoneyInfoStorage.MoneyInfoStorage;

import java.util.Currency;
import java.util.HashMap;
import java.util.Map;

public class ClientRequestUtil {
    private boolean isRequestValid = false;
    private String selectedRequest = null;
    private String userNumber = null;

    /* Authentication variables */
    private String pin = null;

    /* Withdrawal variables */
    private String amount = null;
    private MoneyInfoStorage.Currency currency = null;


    private static final Map<String, String> requestMappings = new HashMap<>();

    static {
        requestMappings.put("authenticate", "A");
        requestMappings.put("withdraw", "W");
        requestMappings.put("deposit", "D");
        requestMappings.put("balance", "B");
        requestMappings.put("history", "H");
        requestMappings.put("changepin", "C");
        requestMappings.put("topup", "T");
        requestMappings.put("A", "authenticate");
        requestMappings.put("W", "withdraw");
        requestMappings.put("D", "deposit");
        requestMappings.put("B", "balance");
        requestMappings.put("H", "history");
        requestMappings.put("C", "changepin");
        requestMappings.put("T", "topup");
    }


    public String encodeRequest() {
        try {

            switch (this.selectedRequest) {
                case "A": {
                    return "A " + this.pin + " " + this.userNumber;
                }
                case "W": {
                    return "W " + this.userNumber + " " + this.amount + this.currency.toString();
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
                this.userNumber = decodeUserNumber(parts[2]);

                if (this.selectedRequest != null && this.pin != null && this.userNumber != null) {
                    this.isRequestValid = true;
                    return;
                } else {
                    this.isRequestValid = false;
                    return;
                }
            case "W":
                this.selectedRequest = requestMappings.get(parts[0]);
                this.userNumber = decodeUserNumber(parts[1]);
                this.amount = decodeAmount(parts[2]);
                this.currency = decodeCurrency(parts[3]);

                if (this.selectedRequest != null && this.userNumber != null && this.amount != null && this.currency != null) {
                    this.isRequestValid = true;
                    return;
                } else {
                    this.isRequestValid = false;
                    return;
                }
        }
    }

    private static String encodePIN(String pin) {
        // Perform PIN validation and return encoded value
        if (pin.matches("\\d{4}")) {
            return pin;
        }
        return null;
    }

    private static String encodeAmount(String amount) {
        // Perform amount validation and return encoded value
        if (amount.matches("\\d+")) {
            return amount;
        }
        return null;
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
            this.currency = decodeCurrency(parts[2]);

            if (this.pin != null && this.amount != null && this.currency != null) {
                this.isRequestValid = true;
                return this.pin + " " + this.amount + " " + this.currency.toString();
            } else {
                this.isRequestValid = false;
                return "";
            }
        }

        return "";
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

    public void setRequest(String Request) {
        this.selectedRequest = requestMappings.get(Request.toLowerCase());
    }

    public void setPin(String pin) {
        this.pin = pin;
    }

    public void setUserNumber(String userNumber) {
        this.userNumber = userNumber;
    }

    public boolean getIsRequestValid() {
        return this.isRequestValid;
    }

    public String getSelectedRequest() {
        return this.selectedRequest;
    }

    public String getUserNumber() {
        return this.userNumber;
    }

    public MoneyInfoStorage.Currency getCurrency() {
        return this.currency;
    }

    public int getAmount() {
        return Integer.parseInt(this.amount);
    }

    public String getPin() {
        return this.pin;
    }
}
