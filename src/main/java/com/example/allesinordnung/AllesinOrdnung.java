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
        stage.setResizable(false);
        stage.setScene(scene);
        stage.show();
    }
    public static void main(String[] args) {
        launch();
    }
}