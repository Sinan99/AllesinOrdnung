package com.example.allesinordnung;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;


public class AllesinOrdnung extends Application {

    @Override
    public void start(Stage stage) throws IOException {
        // Hier wird ein FXMLLoader erstellt, um die grafische Benutzeroberfläche aus der Datei "Homepage.fxml" zu laden.
        FXMLLoader fxmlLoader = new FXMLLoader(AllesinOrdnung.class.getResource("Homepage.fxml"));
        // Eine neue Szene (Scene) wird erstellt und mit der geladenen Benutzeroberfläche (FXML-Datei) initialisiert.
        Scene scene = new Scene(fxmlLoader.load());
        stage.setResizable(true);
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}
