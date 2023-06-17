package com.MoneyInfoStorage;

/**
 * Represents a currency value consisting of a currency type (PLN, EUR) and an amount of money.
 */
public class MoneyInfoStorage {
    /**
     * Enum representing the currency types.
     */
    public enum Currency {
        PLN, EUR
    }

    private Currency currency;
    private long wholeUnits;
    private long fractionalUnits;

    /**
     * Constructs a CurrencyValue object with the specified currency type and amount.
     *
     * @param currency       the currency type (PLN, EUR)
     * @param wholeUnits     the whole units of the amount
     * @param fractionalUnits the fractional units of the amount
     */
    public MoneyInfoStorage(Currency currency, long wholeUnits, long fractionalUnits) {
        this.currency = currency;
        this.wholeUnits = wholeUnits;
        this.fractionalUnits = fractionalUnits;
    }

    public MoneyInfoStorage() {
        this.currency = Currency.PLN;
        this.wholeUnits = 0;
        this.fractionalUnits = 0;
    }

    /**
     * Constructs a CurrencyValue object from its string representation.
     * The string representation consists of the currency type (PLN, EUR) and the amount of money separated by a dot.
     * The amount of money consists of the whole units and the fractional units separated by a dot.
     * For example: "123.45.EUR".
     */
    public MoneyInfoStorage(String currencyValue) {
        String[] currencyValueParts = currencyValue.split("\\.");
        this.currency = Currency.valueOf(currencyValueParts[2]);
        String[] amountParts = currencyValueParts[0].split("\\.");
        this.wholeUnits = Long.parseLong(amountParts[0]);
        this.fractionalUnits = Long.parseLong(amountParts[1]);
    }

    /**
     * Retrieves the currency type.
     *
     * @return the currency type
     */
    public Currency getCurrency() {
        return currency;
    }

    /**
     * Sets the currency type.
     *
     * @param currency the currency type to set
     */
    public void setCurrency(Currency currency) {
        this.currency = currency;
    }

    /**
     * Retrieves the whole units of the amount.
     *
     * @return the whole units
     */
    public long getWholeUnits() {
        return wholeUnits;
    }

    /**
     * Sets the whole units of the amount.
     *
     * @param wholeUnits the whole units to set
     */
    public void setWholeUnits(long wholeUnits) {
        this.wholeUnits = wholeUnits;
    }

    /**
     * Retrieves the fractional units of the amount.
     *
     * @return the fractional units
     */
    public long getFractionalUnits() {
        return fractionalUnits;
    }

    /**
     * Sets the fractional units of the amount.
     *
     * @param fractionalUnits the fractional units to set
     */
    public void setFractionalUnits(long fractionalUnits) {
        this.fractionalUnits = fractionalUnits;
    }

    public int getExchangeRate() {
        if (currency == Currency.PLN) {
            return 1;
        } else if (currency == Currency.EUR) {
            return 4;
        }
        return 0;
    }

    /**
     * Returns the string representation of the CurrencyValue object.
     *
     * @return the string representation
     */
    @Override
    public String toString() {
        return wholeUnits + "." + fractionalUnits + "." + currency;
    }

    /**
     * Creates a new CurrencyValue object with the specified currency type and amount.
     *
     * @param currency       the currency type (PLN, EUR)
     * @param wholeUnits     the whole units of the amount
     * @param fractionalUnits the fractional units of the amount
     * @return the newly created CurrencyValue object
     */
    public static MoneyInfoStorage create(Currency currency, long wholeUnits, long fractionalUnits) {
        return new MoneyInfoStorage(currency, wholeUnits, fractionalUnits);
    }

    /**
     * Reads the currency value from a database or external source using the specified ID.
     *
     * @param id the ID of the currency value to read
     * @return the CurrencyValue object with the specified ID, or null if not found
     */
    public static MoneyInfoStorage read(int id) {
        // Implementation for reading from a database or external source goes here
        // Return null if not found
        return null;
    }

    /**
     * Updates the currency value with the specified ID, setting the new currency type and amount.
     *
     * @param id             the ID of the currency value to update
     * @param currency       the new currency type (PLN, EUR)
     * @param wholeUnits     the new whole units of the amount
     * @param fractionalUnits the new fractional units of the amount
     * @return true if the update was successful, false otherwise
     */
    public static boolean update(int id, Currency currency, long wholeUnits, long fractionalUnits) {
        // Implementation for updating in a database or external source goes here
        // Return true if successful, false otherwise
        return false;
    }

    /**
     * Deletes the currency value with the specified ID.
     *
     * @param id the ID of the currency value to delete
     * @return true if the deletion was successful, false otherwise
     */
    public static boolean delete(int id) {
        // Implementation for deleting from a database or external source goes here
        // Return true if successful, false otherwise
        return false;
    }
}
