package com.example.allesinordnung;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.controlsfx.control.Notifications;

import java.io.IOException;


public class AllesinOrdnung extends Application {

    @Override
    public void start(Stage stage) throws IOException {
        // Hier wird ein FXMLLoader erstellt, um die grafische Benutzeroberfläche aus der Datei "Homepage.fxml" zu laden.
        FXMLLoader fxmlLoader = new FXMLLoader(AllesinOrdnung.class.getResource("Homepage.fxml"));
        // Eine neue Szene (Scene) wird erstellt und mit der geladenen Benutzeroberfläche (FXML-Datei) initialisiert.
        Parent root = fxmlLoader.load();
        Scene scene = new Scene(root);

        // Set the stage properties
        stage.setResizable(true);
        stage.setScene(scene);
        stage.show();

    }

    public static void main(String[] args) {
        launch();
    }


}