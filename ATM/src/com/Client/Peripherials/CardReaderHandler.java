package com.Client.Peripherials;

public class CardReaderHandler {
    private String cardNumber = "";

    public void handleCardReaderInput(String cardNumber) {
        this.cardNumber = cardNumber;
    }

    public String getCardNumber() {
        return cardNumber;
    }

    public void clear() {
        cardNumber = "";
    }
}
