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

public class MoviesController extends AllesinOrdnungController {
    @FXML
    private Button cancelAddMovie;
    @FXML
    private Button saveMovie;
    @FXML
    private TextField searchField;
    @FXML
    private TextField directorField;
    @FXML
    private TextField titleField;
    @FXML
    private TextField releaseYearField;
    @FXML
    private TextField genreField;
    @FXML
    private TextField ratingField; // Added rating field
    @FXML
    private TextField commentField; // Added comment field

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

    private FilteredList<Movie> filteredData;

    @FXML
    private void initialize() {
        // Initialize TableView columns
        directorColumn.setCellValueFactory(new PropertyValueFactory<>("director"));
        titleColumn.setCellValueFactory(new PropertyValueFactory<>("title"));
        releaseYearColumn.setCellValueFactory(new PropertyValueFactory<>("year")); // Use "year" property
        genreColumn.setCellValueFactory(new PropertyValueFactory<>("genre"));
        ratingColumn.setCellValueFactory(new PropertyValueFactory<>("rating"));
        commentColumn.setCellValueFactory(new PropertyValueFactory<>("comment"));

        loadDataFromJson();

        filteredData = new FilteredList<>(movieData, p -> true);

        searchField.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredData.setPredicate(movie -> {
                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }
                String lowerCaseFilter = newValue.toLowerCase();
                if (movie.getTitle().toLowerCase().contains(lowerCaseFilter)) {
                    return true;
                } else if (movie.getDirector().toLowerCase().contains(lowerCaseFilter)) {
                    return true;
                } else if (String.valueOf(movie.getYear()).contains(newValue)) {
                    return true;
                } else if (movie.getGenre().toLowerCase().contains(lowerCaseFilter)) {
                    return true;
                }
                return false;
            });
        });

        tableView.setItems(filteredData);

        tableView.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                fillFormWithMovie(newSelection);
            }
        });
    }

    @FXML
    private void addNewMovie() {
        String director = directorField.getText();
        String title = titleField.getText();
        int releaseYear = Integer.parseInt(releaseYearField.getText());
        String genre = genreField.getText();
        Double rating = parseDoubleOrNull(ratingField.getText()); // parse rating or null
        String comment = commentField.getText(); // get comment

        Movie newMovie = new Movie(releaseYear, title, director, rating, genre, comment); // Update Movie constructor

        addMovieData(newMovie);

        saveMovieDataToJson();

        directorField.clear();
        titleField.clear();
        releaseYearField.clear();
        genreField.clear();
        ratingField.clear(); // clear ratingField
        commentField.clear(); // clear commentField
    }

    // Helper method to parse Double or return null if parsing fails
    private Double parseDoubleOrNull(String value) {
        try {
            return Double.parseDouble(value);
        } catch (NumberFormatException e) {
            return null;
        }
    }


    private void fillFormWithMovie(Movie movie) {
        directorField.setText(movie.getDirector());
        titleField.setText(movie.getTitle());
        releaseYearField.setText(String.valueOf(movie.getYear()));
        genreField.setText(movie.getGenre());
        ratingField.setText(String.valueOf(movie.getRating())); // Set rating field
        commentField.setText(movie.getComment()); // Set comment field
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
            ratingField.clear(); // Clear rating field
            commentField.clear(); // Clear comment field
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
            double rating = Double.parseDouble(ratingField.getText()); // Added rating field
            String comment = commentField.getText(); // Added comment field

            selectedMovie.setDirector(director);
            selectedMovie.setTitle(title);
            selectedMovie.setYear(releaseYear);
            selectedMovie.setGenre(genre);
            selectedMovie.setRating(rating); // Set rating field
            selectedMovie.setComment(comment); // Set comment field

            movieData.set(movieData.indexOf(selectedMovie), selectedMovie);

            saveMovieDataToJson();
        }
    }

    @FXML
    private void Home() {
        openPage(home, "Homepage.fxml");
    }
}
