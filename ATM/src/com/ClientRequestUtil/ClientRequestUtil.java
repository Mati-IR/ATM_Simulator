package ClientRequestUtil;

import java.util.HashMap;
import java.util.Map;

public class ClientRequestUtil {
    private boolean isRequestValid = false;
    private String selectedRequest = null;
    private String userNumber = null;

    /* Authentication variables */
    private String pin = null;


    private static final Map<String, String> requestMappings = new HashMap<>();

    static {
        requestMappings.put("authenticate", "A");
        requestMappings.put("withdraw", "W");
        requestMappings.put("deposit", "D");
        requestMappings.put("balance", "B");
        requestMappings.put("history", "H");
        requestMappings.put("changepin", "C");
        requestMappings.put("topup", "T");
    }


    public String encodeRequest() {
        switch (this.selectedRequest) {
            case "A": {
                return "A " + this.pin + " " + this.userNumber;
            }
        }
        return "Error";
    }

    public void decodeRequest(String encodedRequest) {
        // Split the encoded request and perform decoding/validation
        String[] parts = encodedRequest.trim().split("\\s+");

        if (parts[0] == "A") {
            this.selectedRequest = requestMappings.get(decodePIN(parts[0]));
            this.pin = decodePIN(parts[1]);
            this.userNumber = decodeUserNumber(parts[2]);

            if (this.selectedRequest != null && this.pin != null && this.userNumber != null) {
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

    private static String decodeArguments(String encodedArguments) {
        // Split the encoded arguments and perform decoding/validation
        String[] parts = encodedArguments.trim().split("\\s+");

        if (parts.length == 3) {
            String decodedPIN = decodePIN(parts[0]);
            String decodedAmount = decodeAmount(parts[1]);
            String decodedCurrency = decodeCurrency(parts[2]);

            if (decodedPIN != null && decodedAmount != null && decodedCurrency != null) {
                return decodedPIN + " " + decodedAmount + " " + decodedCurrency;
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

    private static String decodeCurrency(String encodedCurrency) {
        // Perform decoding/validation of currency and return decoded value
        if (encodedCurrency.matches("EUR|PLN")) {
            return encodedCurrency;
        }
        return null;
    }

    public void setRequest(String Request) {
        this.selectedRequest = requestMappings.get(Request);
    }

    public void setPin(String pin) {
        this.pin = pin;
    }

    public void setUserNumber(String userNumber) {
        this.userNumber = userNumber;
    }
}
