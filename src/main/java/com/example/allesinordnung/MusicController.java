package com.example.allesinordnung;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;

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
    private FilteredList<Music> filteredData;

    @FXML
    private void initialize() {
        // Initialisieren der TableView-Spalten
        artistNameColumn.setCellValueFactory(new PropertyValueFactory<>("artistName"));
        songTitleColumn.setCellValueFactory(new PropertyValueFactory<>("songTitle"));
        songDateColumn.setCellValueFactory(new PropertyValueFactory<>("songDate"));

        loadDataFromJson();

        filteredData = new FilteredList<>(musicData, p -> true);

        searchField.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredData.setPredicate(music -> {
                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }
                String lowerCaseFilter = newValue.toLowerCase();
                if (music.getSongTitle().toLowerCase().contains(lowerCaseFilter)) {
                    return true;
                } else if (music.getArtistName().toLowerCase().contains(lowerCaseFilter)) {
                    return true;
                } else if (music.getSongDate().contains(newValue)) {
                    return true;
                }
                return false;
            });
        });

        // Setzen Sie filteredData als die Datenquelle für die TableView
        tableView.setItems(filteredData);

        // Listener für die Auswahl von Zeilen in der TableView
        tableView.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                fillFormWithMusic(newSelection);
            }
        });
    }

    @FXML
    private void addNewMusic() {
        // Erfassen Sie die Benutzereingaben
        String artistName = artistNameField.getText();
        String songTitle = songTitleField.getText();
        String songDate = songDateField.getText();

        // Erstellen Sie ein neues Musikobjekt
        Music newMusic = new Music(artistName, songTitle, songDate);

        // Fügen Sie das neue Musikstück zur musicData hinzu
        addMusicData(newMusic);

        // Save the updated data to the JSON file
        saveMusicDataToJson();

        // Löschen Sie die Textfelder im Formular
        artistNameField.clear();
        songTitleField.clear();
        songDateField.clear();
    }

    private void fillFormWithMusic(Music music) {
        // Befüllen Sie die Formularfelder mit den Daten des ausgewählten Musikstücks
        artistNameField.setText(music.getArtistName());
        songTitleField.setText(music.getSongTitle());
        songDateField.setText(music.getSongDate());
    }

    private void loadDataFromJson() {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            List<Music> loadedMusic = objectMapper.readValue(new File("src/main/resources/data/music.json"), new TypeReference<List<Music>>() {
            });
            // Clear the existing data and add the loaded data
            musicData.clear();
            musicData.addAll(loadedMusic);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void saveMusicDataToJson() {
        ObjectMapper objectMapper = new ObjectMapper();
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
        // Holen Sie das ausgewählte Musikstück
        Music selectedMusic = tableView.getSelectionModel().getSelectedItem();

        // Prüfen Sie, ob ein Musikstück ausgewählt wurde
        if (selectedMusic != null) {
            // Entfernen Sie das Musikstück aus der ObservableList
            musicData.remove(selectedMusic);

            // Speichern Sie die aktualisierte Liste in der JSON-Datei
            saveMusicDataToJson();
            artistNameField.clear();
            songTitleField.clear();
            songDateField.clear();
        }
    }

    @FXML
    private void updateSelectedMusic() {
        // Holen Sie das ausgewählte Musikstück
        Music selectedMusic = tableView.getSelectionModel().getSelectedItem();

        // Prüfen Sie, ob ein Musikstück ausgewählt wurde
        if (selectedMusic != null) {
            // Holen Sie die neuen Daten aus den Formularfeldern
            String artistName = artistNameField.getText();
            String songTitle = songTitleField.getText();
            String songDate = songDateField.getText();

            // Aktualisieren Sie die Musikdaten
            selectedMusic.setArtistName(artistName);
            selectedMusic.setSongTitle(songTitle);
            selectedMusic.setSongDate(songDate);

            // Aktualisieren Sie das Musikstück in der ObservableList
            musicData.set(musicData.indexOf(selectedMusic), selectedMusic);

            // Speichern Sie die aktualisierte Liste in der JSON-Datei
            saveMusicDataToJson();
        }
    }

    @FXML
    private void Home() {
        openPage(home, "Homepage.fxml");
    }
}
