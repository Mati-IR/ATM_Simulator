package com.Client.GUI;

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
            Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("ATM_GUI.fxml")));
            Scene scene = new Scene(root, 700, 900);
            stage.setScene(scene);
            stage.setMaximized(false);
            stage.setResizable(false);

            // start client backend
            Thread guiThread = new Thread(new Runnable() {
                @Override
                public void run() {
                    atmClient.run();
                }
            });
            guiThread.setDaemon(true);
            guiThread.start();

            stage.show();
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
