package com.Client;

import com.Client.GUI.MainController;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;

import java.util.Objects;
import com.Client.ATMClient;

/**
 * The AtmApplication class represents the main entry point for the ATM application.
 * It extends the JavaFX Application class and sets up the user interface and client backend.
 */
public class AtmApplication extends Application {
    static ATMClient atmClient;

    /**
     * Starts the ATM application by setting up the stage, loading the GUI, and starting the client backend.
     *
     * @param stage The primary stage of the application.
     */
    @Override
    public void start(Stage stage) {
        try {
            // set up the stage
            stage.setTitle("ATM");
            FXMLLoader fxmlLoader = new FXMLLoader(Objects.requireNonNull(getClass().getResource("GUI/ATM_GUI.fxml")));

            Parent root = fxmlLoader.load();
            MainController mainController = fxmlLoader.getController();
            Scene scene = new Scene(root, 700, 900);
            stage.setScene(scene);
            stage.setMaximized(false);
            stage.setResizable(false);

            // start client backend
            Thread backendThread = new Thread(new Runnable() {
                @Override
                public void run() {
                    atmClient.setController(mainController);
                }
            });
            backendThread.setDaemon(true);
            backendThread.start();

            stage.show();


            Stage cardSelectionStage = new Stage();
            cardSelectionStage.setTitle("Card Selection");
            Parent cardSelectionRoot = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("GUI/ATM_GUI_CARD_SELECT.fxml")));
            Scene cardSelectionScene = new Scene(cardSelectionRoot, 200, 200);
            cardSelectionStage.setScene(cardSelectionScene);
            cardSelectionStage.setMaximized(false);
            cardSelectionStage.setResizable(false);
            //set modality to application modal
            cardSelectionStage.initOwner(stage);
            cardSelectionStage.initModality(javafx.stage.Modality.APPLICATION_MODAL);
            cardSelectionStage.show();

        } catch (Exception e){
            System.out.println("Error: " + e.getMessage());
        }
    }

    /**
     * The main method of the ATM application.
     * It sets up the client backend and launches the JavaFX application.
     *
     * @param args The command line arguments.
     */
    public static void main(String[] args) {
        // set up the client backend
        atmClient = ATMClient.getInstance(Integer.parseInt(args[1]), args[2], Integer.parseInt(args[3]));

        launch();
    }
}
