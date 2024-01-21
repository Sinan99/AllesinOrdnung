package com.example.allesinordnung;

import javafx.beans.binding.Bindings;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Cursor;
import javafx.scene.ImageCursor;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;

import java.io.IOException;


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
    // Die bilder für cursor laden
    private final Image booksCursorImage = new Image("/Icons/book-bookmark.png");
    private final Image musicCursorImage = new Image("/Icons/music-alt.png");
    private final Image moviesCursorImage = new Image("/Icons/film.png");
    private final Image closeProgramCursorImage = new Image("/Icons/hand-fingers-crossed.png");

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

    // Set default cursors
        openBooks.setCursor(Cursor.DEFAULT);
        openMusic.setCursor(Cursor.DEFAULT);
        openMovies.setCursor(Cursor.DEFAULT);
        closeProgram.setCursor(Cursor.DEFAULT);

    // Add event handlers for each button
    setCursorOnHover(openBooks, booksCursorImage);
    setCursorOnHover(openMusic, musicCursorImage);
    setCursorOnHover(openMovies, moviesCursorImage);
    setCursorOnHover(closeProgram, closeProgramCursorImage);
}
    private void setCursorOnHover(Button button, Image cursorImage) {
        // Set custom cursor on mouse enter
        button.setOnMouseEntered(event -> {
            button.setCursor(Cursor.NONE); // Hide the default cursor
            button.setCursor(new ImageCursor(cursorImage));
        });

        // Revert to default cursor on mouse exit
        button.setOnMouseExited(event -> {
            button.setCursor(Cursor.DEFAULT);
        });
    }
    public static void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
    public static void showSuccessAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Success");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    @FXML
    private void openBooks() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("Books.fxml"));
            Parent newPageRoot = loader.load();
            Stage currentStage = (Stage) openBooks.getScene().getWindow();
            currentStage.setScene(new Scene(newPageRoot, currentStage.getScene().getWidth(), currentStage.getScene().getHeight()));
        } catch (IOException e) {
            showAlert("Error opening Books: " + e.getMessage());
            e.printStackTrace(); // Log the exception for further investigation
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