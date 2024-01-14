package com.example.allesinordnung;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Objects;

public class MoviesController extends AllesinOrdnungController {
    @FXML
    private TextField searchField;
    @FXML
    private TextField ratingField;

    private FilteredList<Movie> filteredData;

    @FXML
    private Button cancelAddMovie;
    @FXML
    private Button saveMovie;

    @FXML
    private TextField directorField;
    @FXML
    private TextField titleField;
    @FXML
    private TextField releaseYearField;
    @FXML
    private TextField genreField;
    @FXML
    private TextField rattingField; // Hinzugefügtes Bewertungsfeld
    @FXML
    private TextField commentField; // Hinzugefügtes Kommentarfeld

    @FXML
    private Button home;
    @FXML
    private TableView<Movie> tableView;
    private ObservableList<Movie> movieData = FXCollections.observableArrayList();

    @FXML
    private TableColumn<Movie, String> directorColumn;

    @FXML
    private TableColumn<Movie, String> titleColumn;

    @FXML
    private TableColumn<Movie, Integer> releaseYearColumn;

    @FXML
    private TableColumn<Movie, String> genreColumn;

    @FXML
    private TableColumn<Movie, Double> ratingColumn;

    @FXML
    private TableColumn<Movie, String> commentColumn;



    @FXML
    private void initialize() {
        // Initialisieren der TableView-Spalten
        directorColumn.setCellValueFactory(new PropertyValueFactory<>("director"));
        titleColumn.setCellValueFactory(new PropertyValueFactory<>("title"));
        releaseYearColumn.setCellValueFactory(new PropertyValueFactory<>("year")); // Verwenden Sie das "year" -Attribut
        genreColumn.setCellValueFactory(new PropertyValueFactory<>("genre"));
        ratingColumn.setCellValueFactory(new PropertyValueFactory<>("rating"));
        commentColumn.setCellValueFactory(new PropertyValueFactory<>("comment"));
        setTooltipForColumn(directorColumn);
        setTooltipForColumn(titleColumn);
        setTooltipForColumn(releaseYearColumn);
        setTooltipForColumn(genreColumn);
        setTooltipForColumn(ratingColumn);
        setTooltipForColumn(commentColumn);


        // Daten aus der JSON-Datei laden
        loadDataFromJson();

        // Erstellen einer gefilterten Liste für die Suche
        filteredData = new FilteredList<>(movieData, p -> true);

        // Change Listener der auf änderungen im Suchfeld reagiert
        searchField.textProperty().addListener((observable, oldValue, newValue) -> {
            // Hier wird ein neues Filterkriterium für die gefilterte Datenliste festgelegt
            // wenn 'true' anzeigen / wenn 'false' ausblenden
            filteredData.setPredicate(movie -> {
                //
                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }
                String lowerCaseFilter = newValue.toLowerCase();
                // Wenn ja, wird 'true' zurückgegeben und das Buch wird angezeigt, sonst wird 'false' zurückgegeben und das Buch wird ausgeblendet.
                if (movie.getTitle().toLowerCase().contains(lowerCaseFilter)){
                    return true;
                } else if (movie.getDirector().toLowerCase().contains(lowerCaseFilter)) {
                    return true;
                } else if (movie.getGenre().contains(lowerCaseFilter)) {
                    return true;
                } else if (String.valueOf(movie.getRating()).contains(newValue)) {
                    return true;
                } else if (movie.getComment().toLowerCase().contains(lowerCaseFilter)) {
                    return true;
                }
                return false;
            });
        });


        // Setzen der gefilterten Liste als Datenquelle für die TableView
        tableView.setItems(filteredData);

        // Listener für die Auswahl von Zeilen in der TableView
        tableView.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                fillFormWithMovie(newSelection);
            }
        });

        // Set the filtered data as the data source for the TableView
        tableView.setItems(filteredData);

    }

    @FXML
    private void addNewMovie() {
        String director = directorField.getText();
        String title = titleField.getText();
        int releaseYear = Integer.parseInt(releaseYearField.getText());
        String genre = genreField.getText();
        Double rating = parseDoubleOrNull(ratingField.getText()); // Bewertung oder null parsen
        String comment = commentField.getText(); // Kommentar holen

        // Neues Movie-Objekt erstellen
        Movie newMovie = new Movie(releaseYear, title, director, rating, genre, comment);

        // Movie-Daten hinzufügen
        addMovieData(newMovie);

        // Movie-Daten in JSON-Datei speichern
        saveMovieDataToJson();

        // Textfelder im Formular leeren
        directorField.clear();
        titleField.clear();
        releaseYearField.clear();
        genreField.clear();
        ratingField.clear(); // Bewertungsfeld leeren
        commentField.clear(); // Kommentarfeld leeren
    }

    // Hilfsmethode zum Parsen von Double oder null zurückgeben, falls das Parsen fehlschlägt
    private Double parseDoubleOrNull(String value) {
        try {
            return Double.parseDouble(value);
        } catch (NumberFormatException e) {
            return null;
        }
    }

    private void fillFormWithMovie(Movie movie) {
        // Formularfelder mit den Daten des ausgewählten Films füllen
        directorField.setText(movie.getDirector());
        titleField.setText(movie.getTitle());
        releaseYearField.setText(String.valueOf(movie.getYear()));
        genreField.setText(movie.getGenre());
        ratingField.setText(String.valueOf(movie.getRating())); // Bewertungsfeld setzen
        commentField.setText(movie.getComment()); // Kommentarfeld setzen
    }

    private void loadDataFromJson() {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            List<Movie> loadedMovies = objectMapper.readValue(new File("src/main/resources/data/movies.json"), new TypeReference<List<Movie>>() {
            });
            movieData.clear();
            movieData.addAll(loadedMovies);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void saveMovieDataToJson() {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            objectMapper.writeValue(new File("src/main/resources/data/movies.json"), movieData);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void addMovieData(Movie newMovie) {
        movieData.add(newMovie);
    }

    @FXML
    private void deleteSelectedMovie() {
        Movie selectedMovie = tableView.getSelectionModel().getSelectedItem();

        if (selectedMovie != null) {
            movieData.remove(selectedMovie);
            saveMovieDataToJson();
            directorField.clear();
            titleField.clear();
            releaseYearField.clear();
            genreField.clear();
            ratingField.clear(); // Bewertungsfeld leeren
            commentField.clear(); // Kommentarfeld leeren
        }
    }

    @FXML
    private void updateSelectedMovie() {
        Movie selectedMovie = tableView.getSelectionModel().getSelectedItem();

        if (selectedMovie != null) {
            String director = directorField.getText();
            String title = titleField.getText();
            int releaseYear = Integer.parseInt(releaseYearField.getText());
            String genre = genreField.getText();
            double rating = Double.parseDouble(ratingField.getText()); // Bewertungsfeld hinzugefügt
            String comment = commentField.getText(); // Kommentarfeld hinzugefügt


            selectedMovie.setRating(rating);
            selectedMovie.setComment(comment);
            selectedMovie.setDirector(director);
            selectedMovie.setTitle(title);
            selectedMovie.setYear(releaseYear);
            selectedMovie.setGenre(genre);
            selectedMovie.setRating(rating); // Bewertungsfeld setzen
            selectedMovie.setComment(comment); // Kommentarfeld setzen

            movieData.set(movieData.indexOf(selectedMovie), selectedMovie);

            saveMovieDataToJson();
        }
    }

    @FXML
    private void Home() {
        openPage(home, "Homepage.fxml");
    }
    private <T> void setTooltipForColumn(TableColumn<Movie, T> column) {
        column.setCellFactory(tc -> new TableCell<Movie, T>() {
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