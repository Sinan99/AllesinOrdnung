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
import java.util.Objects;
import java.util.function.Predicate;

public class MoviesController extends AllesinOrdnungController {
    @FXML
    private TextField searchField;
    @FXML
    private TextField ratingField;

    private FilteredList<Movie> filteredData;

    @FXML
    private Button cancelAddMovie;
    @FXML
    private Button updateButton;
    @FXML
    private Button saveMovie;
    @FXML
    private AnchorPane body;
    @FXML
    private Rectangle headerBG;
    @FXML
    private TextField directorField;
    @FXML
    private TextField titleField;
    @FXML
    private TextField releaseYearField;
    @FXML
    private TextField genreField;
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
    public void initialize() {
        headerBG.widthProperty().bind(body.widthProperty());

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
        //Tooltips für Knöpfe
        Tooltip cancelAddMovieTooltip = new Tooltip("Deletes Selected Entry");
        Tooltip updateButtonTooltip = new Tooltip("Adopts Changes");
        Tooltip saveMovieTooltip = new Tooltip("Save Movie");
        Tooltip homeButtonTooltip = new Tooltip("Go to Homepage");

        cancelAddMovie.setTooltip(cancelAddMovieTooltip);
        updateButton.setTooltip(updateButtonTooltip);
        saveMovie.setTooltip(saveMovieTooltip);
        home.setTooltip(homeButtonTooltip);


        // Daten aus der JSON-Datei laden
        loadDataFromJson();

        // Erstellen einer gefilterten Liste für die Suche
        filteredData = new FilteredList<>(movieData, p -> true);

        searchField.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                filteredData.setPredicate(new Predicate<Movie>() {
                    @Override
                    public boolean test(Movie movie) {

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
                                return movie.getRating() >= numericInput;
                            }
                            // Wenn nein -> wird nach dem Jahr gesucht
                            else{
                                return movie.getYear() == numericInput;
                            }
                        }

                        return  movie.getTitle().toLowerCase().contains(lowerCaseFilter) ||
                                movie.getDirector().toLowerCase().contains(lowerCaseFilter) ||
                                movie.getGenre().toLowerCase().contains(lowerCaseFilter) ||
                                movie.getComment().toLowerCase().contains(lowerCaseFilter);
                    }
                });
            }
        });

        // Setzen der gefilterten Liste als Datenquelle für die TableView
        tableView.setItems(filteredData);

        // Listener für die Auswahl von Zeilen in der TableView
        tableView.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                fillFormWithMovie(newSelection);
            }
        });

    }

    private boolean isNumeric(String str) {
        try {
            Double.parseDouble(str);
            return true;
        } catch(NumberFormatException e) {
            return false;
        }
    }

    @FXML
    private void addNewMovie() {

        //Textfelder in Variablen packen -> Falls Textfelder leer sind -> null rein packen
        String director = directorField.getText().isEmpty() ? null : directorField.getText();
        String title = titleField.getText().isEmpty() ? null : titleField.getText();
        Integer releaseYear = releaseYearField.getText().isEmpty() ? 0 : Integer.parseInt(releaseYearField.getText());
        String genre = genreField.getText().isEmpty() ? null : genreField.getText();
        Double rating = ratingField.getText().isEmpty() ? 0 : Double.parseDouble(ratingField.getText());
        String comment = commentField.getText().isEmpty() ? null : commentField.getText();

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
        objectMapper.enable(SerializationFeature.INDENT_OUTPUT);
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