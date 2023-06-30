package com.Client.Peripherials;

/**
 * The CardReaderHandler class is responsible for handling the input from a card reader device.
 */
public class CardReaderHandler {
    private String cardNumber = "";

    /**
     * Handles the input from the card reader device.
     *
     * @param cardNumber The card number read from the card reader.
     */
    public void handleCardReaderInput(String cardNumber) {
        this.cardNumber = cardNumber;
    }

    /**
     * Retrieves the card number that was read from the card reader.
     *
     * @return The card number.
     */
    public String getCardNumber() {
        return cardNumber;
    }

    /**
     * Clears the stored card number.
     */
    public void clear() {
        cardNumber = "";
    }
}
