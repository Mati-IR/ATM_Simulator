package com.Client;

import com.Client.GUI.MainController;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;

import java.util.Objects;
import com.Client.ATMClient;

public class AtmApplication extends Application {
    static ATMClient atmClient;

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
            Scene cardSelectionScene = new Scene(cardSelectionRoot, 180, 180);
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

    public static void main(String[] args) {
        // set up the client backend
        atmClient = ATMClient.getInstance();

        launch();
    }
}
