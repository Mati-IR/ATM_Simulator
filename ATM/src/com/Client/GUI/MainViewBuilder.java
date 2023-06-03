package com.Client.GUI;

//import com.atm.gui.peripherals.CardReader;
//import com.atm.gui.peripherals.Keyboard;
//import com.atm.gui.peripherals.Printer;
//import com.atm.gui.staticelements.Display;
//import com.atm.gui.staticelements.Keypad;
//import com.atm.gui.touchscreen.TouchscreenPanel;
//import javafx.scene.layout.BorderPane;
//import javafx.scene.layout.VBox;


public class MainViewBuilder {
    //private BorderPane rootPane;
    //private CardReader cardReader;
    //private Keyboard keyboard;
    //private Printer printer;
    //private Display display;
    //private Keypad keypad;
    //private TouchscreenPanel touchscreenPanel;

    public MainViewBuilder() {
        // Initialize components and set up the GUI hierarchy
        initializeComponents();
        buildGUI();
        addEventHandlers();
    }

    private void initializeComponents() {
        // TODO: Initialize all the necessary components
        // Create instances of CardReader, Keyboard, Printer, Display, Keypad, TouchscreenPanel, etc.
        // Set up any required configurations or dependencies
    }

    private void buildGUI() {
        // TODO: Build the GUI hierarchy using the initialized components
        // Use BorderPane as the root container
        // Add the necessary components to the appropriate positions within the BorderPane
        // Consider using additional containers like VBox or other layout managers to organize the components
    }

    private void addEventHandlers() {
        // TODO: Add event handlers for user interactions with the GUI components
        // Set up event listeners for button clicks, touch events, etc.
        // Implement the necessary logic to handle user inputs and trigger appropriate actions
    }

    /*public BorderPane getRootPane() {
        return rootPane;
    }*/

    // Add additional methods as needed to interact with the GUI components

    // TODO: Implement methods to handle various actions and behaviors of the ATM GUI
    // For example: handleCardInsertion, handlePinEntry, handleWithdrawal, handleDeposit, etc.
}
