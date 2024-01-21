package com.example.allesinordnung;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.scene.shape.Rectangle;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import org.controlsfx.control.Notifications;

public class MusicController extends AllesinOrdnungController {
    @FXML
    private Button cancelAddMusic;
    @FXML
    private Button updateButton;
    @FXML
    private Button saveMusic;
    @FXML
    private TextField searchField;
    @FXML
    private TextField artistNameField;
    @FXML
    private TextField songTitleField;
    @FXML
    private TextField songDateField;
    @FXML
    private TextField ratingField;
    @FXML
    private TextField genreField;
    @FXML
    private TextField commentField;
    @FXML
    private AnchorPane body;
    @FXML
    private Rectangle headerBG;

    @FXML
    private Button home;
    @FXML
    private TableView<Music> tableView;
    private ObservableList<Music> musicData = FXCollections.observableArrayList();
    @FXML
    private TableColumn<Music, String> artistNameColumn;
    @FXML
    private TableColumn<Music, String> songTitleColumn;
    @FXML
    private TableColumn<Music, String> songDateColumn;
    @FXML
    private TableColumn<Music, Double> ratingColumn;
    @FXML
    private TableColumn<Music, String> genreColumn;
    @FXML
    private TableColumn<Music, String> commentColumn;
    private FilteredList<Music> filteredData;

    @FXML
    public void initialize() {
        headerBG.widthProperty().bind(body.widthProperty());

        // Initialisierung der TableView-Spalten
        artistNameColumn.setCellValueFactory(new PropertyValueFactory<>("artist"));
        songTitleColumn.setCellValueFactory(new PropertyValueFactory<>("title"));
        songDateColumn.setCellValueFactory(new PropertyValueFactory<>("year"));
        ratingColumn.setCellValueFactory(new PropertyValueFactory<>("rating"));
        genreColumn.setCellValueFactory(new PropertyValueFactory<>("genre"));
        commentColumn.setCellValueFactory(new PropertyValueFactory<>("comment"));
        setTooltipForColumn(artistNameColumn);
        setTooltipForColumn(songTitleColumn);
        setTooltipForColumn(songDateColumn);
        setTooltipForColumn(ratingColumn);
        setTooltipForColumn(genreColumn);
        setTooltipForColumn(commentColumn);

        //Tooltips für Knöpfe
        Tooltip cancelAddMovieTooltip = new Tooltip("Deletes Selected Entry");
        Tooltip updateButtonTooltip = new Tooltip("Adopts Changes");
        Tooltip saveMovieTooltip = new Tooltip("Save the Song");
        Tooltip homeButtonTooltip = new Tooltip("Go to Homepage");

        cancelAddMusic.setTooltip(cancelAddMovieTooltip);
        updateButton.setTooltip(updateButtonTooltip);
        saveMusic.setTooltip(saveMovieTooltip);
        home.setTooltip(homeButtonTooltip);


        // Laden der Daten aus JSON
        loadDataFromJson();
        // Erstellung einer gefilterten Datenliste
        filteredData = new FilteredList<>(musicData, p -> true);

        searchField.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                filteredData.setPredicate(new Predicate<Music>() {
                    @Override
                    public boolean test(Music music) {

                        if (newValue == null || newValue.isEmpty()) {
                            return true;
                        }

                        String lowerCaseFilter = newValue.toLowerCase();

                        // Prüfen, ob die Eingabe ein Zahl ist
                        if (isNumeric(lowerCaseFilter)) {
                            double numericInput = Double.parseDouble(lowerCaseFilter);
                            // Prüfen, ob die Zahl <=10 , wenn ja -> ist es ein Rating
                            if (numericInput <= 10) {
                                // Sucht nach Büchern mit einem höheren Rating
                                return music.getRating() >= numericInput;
                            }
                            // Wenn nein -> wird nach dem Jahr gesucht
                            else{
                                return music.getYear() == numericInput;
                            }
                        }

                        if (music.getTitle() != null && music.getTitle().toLowerCase().contains(lowerCaseFilter)) {
                            return true;
                        }
                        if (music.getArtist() != null && music.getArtist().toLowerCase().contains(lowerCaseFilter)) {
                            return true;
                        }
                        if (music.getGenre() != null && music.getGenre().toLowerCase().contains(lowerCaseFilter)) {
                            return true;
                        }
                        if (music.getComment() != null && music.getComment().toLowerCase().contains(lowerCaseFilter)) {
                            return true;
                        }
                        return false;
                    }
                });
            }
        });

        // Setzen der gefilterten Daten als Datenquelle für die TableView
        tableView.setItems(filteredData);

        // Hinzufügen eines Listeners für die Auswahl von Zeilen in der TableView
        tableView.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                fillFormWithMusic(newSelection);
            }
        });
    }

    //Prüfen ob übergebener Wert eine Zahl ist
    private boolean isNumeric(String str) {
        try {
            Double.parseDouble(str);
            return true;
        } catch(NumberFormatException e) {
            return false;
        }
    }

    @FXML
    private void addNewMusic() {
        // Erfassen der Benutzereingaben
        String artist = artistNameField.getText().isEmpty() ? null : artistNameField.getText();
        String title = songTitleField.getText().isEmpty() ? null : songTitleField.getText();
        int year;
        if (!songDateField.getText().isEmpty() && isNumeric(songDateField.getText())) {
            year = Integer.parseInt(songDateField.getText());
        } else if (!songDateField.getText().isEmpty()) {
            showAlert("Please enter a valid year in numerals.");
            return;
        } else {
            year = 0; // Default value when no input is provided
        }
        String ratingText = ratingField.getText();
        Double rating;
        if (!ratingText.isEmpty()) {
            if (isNumeric(ratingText)) {
                rating = Double.parseDouble(ratingText);
                if (rating < 1 || rating > 10) {
                    showAlert("Please enter a valid rating between 1 and 10.");
                    return;
                }
            } else {
                showAlert("Please enter a valid numeric rating.");
                return;
            }
        } else {
            rating = Double.NaN; // "unrated"
        }
        // Check if at least title and artist are provided
        if (title == null || artist == null) {
            showAlert("Title and Artist are required fields.");
            return;
        }
        String genre = genreField.getText().isEmpty() ? null : genreField.getText();
        String comment = commentField.getText().isEmpty() ? null : commentField.getText();

        // Erstellen eines neuen Musikobjekts
        Music newMusic = new Music(year, title, artist, rating, genre, comment);


        // Hinzufügen des neuen Musikstücks zur Liste
        addMusicData(newMusic);

        // Speichern der aktualisierten Daten in einer JSON-Datei
        saveMusicDataToJson();

        // Löschen der Textfelder im Formular
        artistNameField.clear();
        songTitleField.clear();
        songDateField.clear();
        ratingField.clear();
        genreField.clear();
        commentField.clear();
        // Show a success message in the corner of the screen
        Notifications.create().text("Song added successfully!").showInformation();
    }


    private void fillFormWithMusic(Music music) {
        // Befüllen der Formularfelder mit den Daten des ausgewählten Musikstücks
        artistNameField.setText(music.getArtist());
        songTitleField.setText(music.getTitle());
        songDateField.setText(String.valueOf(music.getYear()));
        ratingField.setText(String.valueOf(music.getRating()));
        genreField.setText(String.valueOf(music.getGenre()));
        commentField.setText(music.getComment());
    }

    private void loadDataFromJson() {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            List<Music> loadedMusic = objectMapper.readValue(new File("src/main/resources/data/music.json"), new TypeReference<List<Music>>() {
            });
            // Löschen der vorhandenen Daten und Hinzufügen der geladenen Daten
            musicData.clear();
            musicData.addAll(loadedMusic);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void saveMusicDataToJson() {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.enable(SerializationFeature.INDENT_OUTPUT);
        try {
            objectMapper.writeValue(new File("src/main/resources/data/music.json"), musicData);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void addMusicData(Music newMusic) {
        musicData.add(newMusic);
    }
    @FXML
    private void deleteSelectedMusic() {
        // Get the selected SOng
        Music selectedMusic = tableView.getSelectionModel().getSelectedItem();

        // Check if a song was selected
        if (selectedMusic != null) {
            // Create a confirmation dialog
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Confirmation Dialog");
            alert.setHeaderText("Delete " + selectedMusic.getTitle());
            alert.setContentText("Are you sure you want to delete the selected song?\n"
                    + "Title: " + selectedMusic.getTitle() + "\nArtist: " + selectedMusic.getArtist());

            // Show the dialog and wait for user action
            Optional<ButtonType> result = alert.showAndWait();

            // Check if the user selected "OK"
            if (result.isPresent() && result.get() == ButtonType.OK) {
                // Remove the song from the ObservableList
                musicData.remove(selectedMusic);

                // Save the updated list to the JSON file
                saveMusicDataToJson();

                // Clear the form fields
                genreField.clear();
                songDateField.clear();
                artistNameField.clear();
                songTitleField.clear();
                ratingField.clear();
                commentField.clear();
                // Show a success message in the corner of the screen
                Notifications.create().text("Song deleted successfully!").showInformation();
            }
        }
    }
    @FXML
    private void updateSelectedMusic() {
        // Holen des ausgewählten Musikstücks
        Music selectedMusic = tableView.getSelectionModel().getSelectedItem();

        // Überprüfen, ob ein Musikstück ausgewählt wurde
        if (selectedMusic != null) {
            // Holen der neuen Daten aus den Formularfeldern
            String artistName = artistNameField.getText();
            String songTitle = songTitleField.getText();
            String songDate = songDateField.getText();
            double rating = Double.parseDouble(ratingField.getText());
            String genre = genreField.getText();
            String comment = commentField.getText();

            // Aktualisieren der Musikdaten
            selectedMusic.setArtist(artistName);
            selectedMusic.setTitle(songTitle);
            selectedMusic.setYear(Integer.parseInt(songDate));
            selectedMusic.setRating(rating);
            selectedMusic.setGenre(genre);
            selectedMusic.setComment(comment);

            // Aktualisieren des Musikstücks in der Liste
            musicData.set(musicData.indexOf(selectedMusic), selectedMusic);

            // Speichern der aktualisierten Liste in der JSON-Datei
            saveMusicDataToJson();
            // Zeigt eine Erfolgsmeldung in der Ecke des Bildschirms an
            Notifications.create().text("Song updated successfully!").showInformation();
        }
    }

    @FXML
    private void Home() {
        openPage(home, "Homepage.fxml");
    }

    private <T> void setTooltipForColumn(TableColumn<Music, T> column) {
        column.setCellFactory(tc -> new TableCell<Music, T>() {
            @Override
            protected void updateItem(T item, boolean empty) {
                super.updateItem(item, empty);

                if (empty || item == null) {
                    setText(null);
                    setTooltip(null);
                } else {
                    setText(item.toString());
                    Tooltip tooltip = new Tooltip(item.toString());
                    setTooltip(tooltip);
                }
            }
        });
    }

}
