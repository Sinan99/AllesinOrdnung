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
        FXMLLoader fxmlLoader = new FXMLLoader(AllesinOrdnung.class.getResource("Homepage.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 900, 600);
        // Festlegen der Größe des Fensters
        stage.setScene(scene);
        stage.setWidth(900); // Festlegen der Breite
        stage.setHeight(600); // Festlegen der Höhe

        // Verhindern, dass Benutzer die Größe des Fensters ändern können
        stage.setResizable(false);

        stage.show();
    }
    public static void main(String[] args) {
        launch();
    }
}