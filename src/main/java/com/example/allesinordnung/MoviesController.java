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

        // Daten aus der JSON-Datei laden
        loadDataFromJson();

        // Erstellen einer gefilterten Liste für die Suche
        filteredData = new FilteredList<>(movieData, p -> true);

        // Hinzufügen eines Listeners für das Suchfeld
        searchField.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredData.setPredicate(movie -> {
                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }

                String lowerCaseFilter = newValue.toLowerCase();

                // Check if the input is a number greater than 0 and up to 10
                if (newValue.matches("\\d+") && Double.parseDouble(newValue) > 0 && Double.parseDouble(newValue) <= 10) {
                    // If it's a valid number, perform rating search
                    return compareRating(movie.getRating(), newValue);
                } else {
                    // Otherwise, perform regular text search on other fields
                    return movie.getTitle().toLowerCase().contains(lowerCaseFilter) ||
                            movie.getDirector().toLowerCase().contains(lowerCaseFilter) ||
                            String.valueOf(movie.getYear()).contains(newValue) ||
                            movie.getGenre().toLowerCase().contains(lowerCaseFilter);
                }
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
    private boolean compareRating(Double rating, String filter) {
        System.out.println("Rating: " + rating + ", Filter: " + filter);

        try {
            if (rating == null || rating < 0 || rating > 10) {
                // Exclude null ratings and those outside the range [0, 10]
                return false;
            }

            char operator;
            double filterValue;

            if (filter.matches("\\d+")) {
                // If the filter contains only digits (no operator), set the operator to '='
                operator = '=';
                filterValue = Double.parseDouble(filter);
            } else {
                // Extract operator and filter value
                operator = filter.charAt(0);
                filterValue = Double.parseDouble(filter.substring(1));
            }

            // Check if the filter value is within the range (1 to 10, inclusive)
            boolean excludeYear = (filterValue > 0 && filterValue <= 10);

            // Adjust the comparison based on the operator
            switch (operator) {
                case '>':
                    return excludeYear && rating > filterValue;
                case '<':
                    return excludeYear && rating < filterValue;
                case '=':
                    return excludeYear && Math.floor(rating) == filterValue;
                default:
                    // If no operator is provided, default to '>=' comparison
                    return excludeYear && rating >= filterValue;
            }
        } catch (NumberFormatException | StringIndexOutOfBoundsException e) {
            // Handle the case where the input is not a valid double or does not contain a valid operator
            return false;
        }
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
}