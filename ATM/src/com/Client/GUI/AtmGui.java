package com.Client.GUI;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;

import javax.swing.*;
import java.util.Objects;

public class AtmGui extends Application {

    @Override
    public void start(Stage stage) {
        try {
            stage.setTitle("ATM");
            Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("ATM_GUI.fxml")));
            Scene scene = new Scene(root, 700, 900);
            stage.setScene(scene);
            stage.setMaximized(false);
            stage.setResizable(false);
            stage.show();
        } catch (Exception e){
            System.out.println("Error: " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        launch();
    }
}
