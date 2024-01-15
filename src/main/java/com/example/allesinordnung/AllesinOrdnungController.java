package com.example.allesinordnung;

import javafx.beans.binding.Bindings;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;


public class AllesinOrdnungController {
    @FXML
    private AnchorPane body;
    @FXML
    private VBox headerVBox;
    @FXML
    private Rectangle headerBG;
    @FXML
    private Button closeProgram;
    @FXML
    private Button openMovies;
    @FXML
    private Button openMusic;
    @FXML
    private Button openBooks;

    @FXML
    public void initialize() {
        headerBG.widthProperty().bind(body.widthProperty());
        headerVBox.prefWidthProperty().bind(body.widthProperty());
        openBooks.prefHeightProperty().bind(Bindings.divide(body.heightProperty(), 4.4));
        openBooks.prefWidthProperty().bind(Bindings.divide(body.widthProperty(), 2.7));
        openMovies.prefHeightProperty().bind(Bindings.divide(body.heightProperty(), 4.4));
        openMovies.prefWidthProperty().bind(Bindings.divide(body.widthProperty(), 2.7));
        openMusic.prefHeightProperty().bind(Bindings.divide(body.heightProperty(), 4.4));
        openMusic.prefWidthProperty().bind(Bindings.divide(body.widthProperty(), 2.7));
        closeProgram.prefHeightProperty().bind(Bindings.divide(body.heightProperty(), 4.4));
        closeProgram.prefWidthProperty().bind(Bindings.divide(body.widthProperty(), 2.7));
    }

    @FXML
    private void openBooks() {
        try {
            // Lade das FXML-Layout für die neue Seite
            FXMLLoader loader = new FXMLLoader(getClass().getResource("Books.fxml"));
            Parent newPageRoot = loader.load();
            // Erhalte die Stage der aktuellen Szene
            Stage currentStage = (Stage) openBooks.getScene().getWindow();
            // Setze die neue Scene auf der aktuellen Stage, um die Seite zu ändern
            currentStage.setScene(new Scene(newPageRoot, currentStage.getScene().getWidth(), currentStage.getScene().getHeight()));
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @FXML
    private void closeProgram() {
        System.exit(0);
    }

    @FXML
    private void openMusics() {
        try {
            // Lade das FXML-Layout für die neue Seite
            FXMLLoader loader = new FXMLLoader(getClass().getResource("Musics.fxml"));
            Parent newPageRoot = loader.load();
            // Erhalte die Stage der aktuellen Szene
            Stage currentStage = (Stage) openMusic.getScene().getWindow();
            // Setze die neue Scene auf der aktuellen Stage, um die Seite zu ändern
            currentStage.setScene(new Scene(newPageRoot, currentStage.getScene().getWidth(), currentStage.getScene().getHeight()));
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @FXML
    private void openMovies() {
        try {
            // Lade das FXML-Layout für die neue Seite
            FXMLLoader loader = new FXMLLoader(getClass().getResource("Movies.fxml"));
            Parent newPageRoot = loader.load();
            // Erhalte die Stage der aktuellen Szene
            Stage currentStage = (Stage) openMovies.getScene().getWindow();
            // Setze die neue Scene auf der aktuellen Stage, um die Seite zu ändern
            currentStage.setScene(new Scene(newPageRoot, currentStage.getScene().getWidth(), currentStage.getScene().getHeight()));
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    // Diese Methode kann verwendet werden, um allgemein Seiten zu öffnen, indem man den Button und den Dateinamen übergeben.
    protected void openPage(Button clicked, String document) {
        try {
            // Lade das FXML-Layout für die neue Seite
            FXMLLoader loader = new FXMLLoader(getClass().getResource(document));
            Parent newPageRoot = loader.load();
            // Erhalte die Stage der aktuellen Szene
            Stage currentStage = (Stage) clicked.getScene().getWindow();
            // Setze die neue Scene auf der aktuellen Stage, um die Seite zu ändern
            currentStage.setScene(new Scene(newPageRoot, currentStage.getScene().getWidth(), currentStage.getScene().getHeight()));
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

}