package com.example.allesinordnung;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
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
import org.controlsfx.control.Notifications;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
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
    private TextField yearField;
    @FXML
    private TextField genreField;
    @FXML
    private TextField commentField;

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
    private TableColumn<Movie, Integer> yearColumn;

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
        yearColumn.setCellValueFactory(new PropertyValueFactory<>("year")); // Verwenden Sie das "year" -Attribut
        genreColumn.setCellValueFactory(new PropertyValueFactory<>("genre"));
        ratingColumn.setCellValueFactory(new PropertyValueFactory<>("rating"));
        commentColumn.setCellValueFactory(new PropertyValueFactory<>("comment"));
        //Tooltips
        setTooltipForColumn(directorColumn);
        setTooltipForColumn(titleColumn);
        setTooltipForColumn(yearColumn);
        setTooltipForColumn(genreColumn);
        setTooltipForColumn(ratingColumn);
        setTooltipForColumn(commentColumn);
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

                        if (movie.getTitle() != null && movie.getTitle().toLowerCase().contains(lowerCaseFilter)) {
                            return true;
                        }
                        if (movie.getDirector() != null && movie.getDirector().toLowerCase().contains(lowerCaseFilter)) {
                            return true;
                        }
                        if (movie.getGenre() != null && movie.getGenre().toLowerCase().contains(lowerCaseFilter)) {
                            return true;
                        }
                        if (movie.getComment() != null && movie.getComment().toLowerCase().contains(lowerCaseFilter)) {
                            return true;
                        }
                        return false;
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
    // Hilfsmethode zum Parsen von Double oder null zurückgeben, falls das Parsen fehlschlägt
    private Double parseDoubleOrNull(String value) {
        try {
            return Double.parseDouble(value);
        } catch (NumberFormatException e) {
            return null;
        }
    }
    @FXML
    private void addNewMovie() {
        // Capture user inputs
        String genre = genreField.getText().isEmpty() ? null : genreField.getText();

        int year;
        if (!yearField.getText().isEmpty() && isNumeric(yearField.getText())) {
            year = Integer.parseInt(yearField.getText());
        } else if (!yearField.getText().isEmpty()) {
            showAlert("Please enter a valid year in numerals.");
            return;
        } else {
            year = 0; // Default value when no input is provided
        }

        String director = directorField.getText().isEmpty() ? null : directorField.getText();
        String title = titleField.getText().isEmpty() ? null : titleField.getText();
        String comment = commentField.getText().isEmpty() ? null : commentField.getText();

        String ratingText = ratingField.getText();
        double rating;
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
        // Check if at least title and director are provided
        if (title == null || director == null) {
            showAlert("Title and director are required fields.");
            return;
        }
        // Create a new Movie object
        Movie newMovie = new Movie(year, title, director, rating,genre,comment);

        // Add the new movie to movieData
        addMovieData(newMovie);

        // Save the updated data to the JSON file
        saveMovieDataToJson();

        // Clear the form fields
        genreField.clear();
        yearField.clear();
        directorField.clear();
        titleField.clear();
        ratingField.clear();
        commentField.clear();
        // Show a success message in the corner of the screen
        Notifications.create().text("Movie added successfully!").showInformation();
    }

    private void fillFormWithMovie(Movie movie) {
        // Formularfelder mit den Daten des ausgewählten Films füllen
        directorField.setText(movie.getDirector());
        titleField.setText(movie.getTitle());
        yearField.setText(String.valueOf(movie.getYear()));
        genreField.setText(movie.getGenre());
        ratingField.setText(String.valueOf(movie.getRating()));
        commentField.setText(movie.getComment());
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
        // Get the selected movie
        Movie selectedMovie = tableView.getSelectionModel().getSelectedItem();

        // Check if a movie was selected
        if (selectedMovie != null) {
            // Create a confirmation dialog
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Confirmation Dialog");
            alert.setHeaderText("Delete " + selectedMovie.getTitle());
            alert.setContentText("Are you sure you want to delete the selected movie?\n"
                    + "Title: " + selectedMovie.getTitle() + "\nDirector: " + selectedMovie.getDirector());

            // Show the dialog and wait for user action
            Optional<ButtonType> result = alert.showAndWait();

            // Check if the user selected "OK"
            if (result.isPresent() && result.get() == ButtonType.OK) {
                // Remove the movie from the ObservableList
                movieData.remove(selectedMovie);

                // Save the updated list to the JSON file
                saveMovieDataToJson();

                // Clear the form fields
                genreField.clear();
                yearField.clear();
                directorField.clear();
                titleField.clear();
                ratingField.clear();
                commentField.clear();
                // Show a success message in the corner of the screen
                Notifications.create().text("Movie deleted successfully!").showInformation();
            }
        }
    }

    @FXML
    private void updateSelectedMovie() {
        Movie selectedMovie = tableView.getSelectionModel().getSelectedItem();

        if (selectedMovie != null) {
            String director = directorField.getText().isEmpty() ? null : directorField.getText();
            String title = titleField.getText().isEmpty() ? null : titleField.getText();

            int year;
            if (!yearField.getText().isEmpty() && isNumeric(yearField.getText())) {
                year = Integer.parseInt(yearField.getText());
            } else if (!yearField.getText().isEmpty()) {
                showAlert("Please enter a valid year in numerals.");
                return;
            } else {
                year = 0; // Default value when no input is provided
            }

            String genre = genreField.getText().isEmpty() ? null : genreField.getText();
            String comment = commentField.getText().isEmpty() ? null : commentField.getText();

            String ratingText = ratingField.getText();
            double rating;
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

            // Check if at least title and director are provided
            if (title == null || director == null) {
                showAlert("Title and director are required fields.");
                return;
            }

            selectedMovie.setRating(rating);
            selectedMovie.setComment(comment);
            selectedMovie.setDirector(director);
            selectedMovie.setTitle(title);
            selectedMovie.setYear(year);
            selectedMovie.setGenre(genre);
            selectedMovie.setRating(rating);
            selectedMovie.setComment(comment);

            movieData.set(movieData.indexOf(selectedMovie), selectedMovie);

            saveMovieDataToJson();
            // Zeigt eine Erfolgsmeldung in der Ecke des Bildschirms an
            Notifications.create().text("Movie updated successfully!").showInformation();
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