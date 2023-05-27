package ClientRequestUtil;

import java.util.HashMap;
import java.util.Map;

public class ClientRequestUtil {
    private static final Map<String, String> requestMappings = new HashMap<>();

    static {
        requestMappings.put("withdraw", "W");
        requestMappings.put("deposit", "D");
        requestMappings.put("balance", "B");
        requestMappings.put("history", "H");
        requestMappings.put("changepin", "C");
        requestMappings.put("topup", "T");
    }

    public static String encodeRequest(String userInput) {
        String[] parts = userInput.trim().split("\\s+", 2);
        String command = parts[0].toLowerCase();

        if (requestMappings.containsKey(command)) {
            String encodedCommand = requestMappings.get(command);
            String encodedRequest = encodedCommand;

            if (parts.length > 1) {
                // Split the arguments into PIN, amount, and currency
                String[] arguments = parts[1].trim().split("\\s+");
                String encodedPIN = encodePIN(arguments[0]);
                String encodedAmount = encodeAmount(arguments[1]);
                String encodedCurrency = encodeCurrency(arguments[2]);

                // Validate and append the encoded arguments
                if (encodedPIN != null && encodedAmount != null && encodedCurrency != null) {
                    encodedRequest += " " + encodedPIN + " " + encodedAmount + " " + encodedCurrency;
                    return encodedRequest;
                }
            }

            return encodedRequest;
        }

        return "";
    }

    public static String decodeRequest(String encodedRequest) {
        String[] parts = encodedRequest.trim().split("\\s+", 2);
        String encodedCommand = parts[0].toUpperCase();

        for (Map.Entry<String, String> entry : requestMappings.entrySet()) {
            if (entry.getValue().equals(encodedCommand)) {
                String command = entry.getKey();

                if (parts.length > 1) {
                    String decodedArguments = decodeArguments(parts[1]);
                    if (!decodedArguments.isEmpty()) {
                        return command + " " + decodedArguments;
                    }
                } else {
                    return command;
                }
            }
        }

        return "";
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
}
