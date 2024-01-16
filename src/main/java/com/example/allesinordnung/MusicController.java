package com.example.allesinordnung;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
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

public class MusicController extends AllesinOrdnungController {
    @FXML
    private Button cancelAddMusic;
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
    private TableColumn<Music, String> commentColumn;
    private FilteredList<Music> filteredData;

    @FXML
    public void initialize() {
        headerBG.widthProperty().bind(body.widthProperty());

        // Initialisierung der TableView-Spalten
        artistNameColumn.setCellValueFactory(new PropertyValueFactory<>("artistName"));
        songTitleColumn.setCellValueFactory(new PropertyValueFactory<>("songTitle"));
        songDateColumn.setCellValueFactory(new PropertyValueFactory<>("songDate"));
        ratingColumn.setCellValueFactory(new PropertyValueFactory<>("rating"));
        commentColumn.setCellValueFactory(new PropertyValueFactory<>("comment"));
        setTooltipForColumn(artistNameColumn);
        setTooltipForColumn(songTitleColumn);
        setTooltipForColumn(songDateColumn);
        setTooltipForColumn(ratingColumn);
        setTooltipForColumn(commentColumn);


        // Laden der Daten aus JSON
        loadDataFromJson();
        // Erstellung einer gefilterten Datenliste
        filteredData = new FilteredList<>(musicData, p -> true);

        // Change Listener der auf änderungen im Suchfeld reagiert
        searchField.textProperty().addListener((observable, oldValue, newValue) -> {
            // Hier wird ein neues Filterkriterium für die gefilterte Datenliste festgelegt
            // wenn 'true' anzeigen / wenn 'false' ausblenden
            filteredData.setPredicate(music -> {
                //
                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }
                String lowerCaseFilter = newValue.toLowerCase();
                // Wenn ja, wird 'true' zurückgegeben und das Buch wird angezeigt, sonst wird 'false' zurückgegeben und das Buch wird ausgeblendet.
                if (music.getSongTitle().toLowerCase().contains(lowerCaseFilter)) {
                    return true;
                } else if (music.getArtistName().toLowerCase().contains(lowerCaseFilter)) {
                    return true;
                } else if (String.valueOf(music.getSongDate()).contains(newValue)) {
                    return true;
                } else if (String.valueOf(music.getRating()).contains(newValue)) {
                    return true;
                } else if (music.getComment().toLowerCase().contains(lowerCaseFilter)) {
                    return true;
                }
                return false;
            });
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

    @FXML
    private void addNewMusic() {
        // Erfassen der Benutzereingaben
        String artistName = artistNameField.getText();
        String songTitle = songTitleField.getText();
        String songDate = songDateField.getText();
        Double rating = Double.valueOf(ratingField.getText());
        String comment = commentField.getText();

        // Erstellen eines neuen Musikobjekts
        Music newMusic = new Music(artistName, songTitle, songDate, rating, comment);

        // Hinzufügen des neuen Musikstücks zur Liste
        addMusicData(newMusic);

        // Speichern der aktualisierten Daten in einer JSON-Datei
        saveMusicDataToJson();

        // Löschen der Textfelder im Formular
        artistNameField.clear();
        songTitleField.clear();
        songDateField.clear();
        ratingField.clear();
        commentField.clear();
    }

    private void fillFormWithMusic(Music music) {
        // Befüllen der Formularfelder mit den Daten des ausgewählten Musikstücks
        artistNameField.setText(music.getArtistName());
        songTitleField.setText(music.getSongTitle());
        songDateField.setText(music.getSongDate());
        ratingField.setText(String.valueOf(music.getRating()));
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
        // Holen des ausgewählten Musikstücks
        Music selectedMusic = tableView.getSelectionModel().getSelectedItem();

        // Überprüfen, ob ein Musikstück ausgewählt wurde
        if (selectedMusic != null) {
            // Entfernen des Musikstücks aus der Liste
            musicData.remove(selectedMusic);

            // Speichern der aktualisierten Liste in der JSON-Datei
            saveMusicDataToJson();

            // Löschen der Formularfelder
            artistNameField.clear();
            songTitleField.clear();
            songDateField.clear();
            ratingField.clear();
            commentField.clear();
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
            String comment = commentField.getText();

            // Aktualisieren der Musikdaten
            selectedMusic.setArtistName(artistName);
            selectedMusic.setSongTitle(songTitle);
            selectedMusic.setSongDate(songDate);
            selectedMusic.setRating(rating);
            selectedMusic.setComment(comment);

            // Aktualisieren des Musikstücks in der Liste
            musicData.set(musicData.indexOf(selectedMusic), selectedMusic);

            // Speichern der aktualisierten Liste in der JSON-Datei
            saveMusicDataToJson();
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
