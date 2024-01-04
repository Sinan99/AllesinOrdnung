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

    private FilteredList<Movie> filteredData;

    @FXML
    private void initialize() {
        // Initialize TableView columns
        directorColumn.setCellValueFactory(new PropertyValueFactory<>("director"));
        titleColumn.setCellValueFactory(new PropertyValueFactory<>("title"));
        releaseYearColumn.setCellValueFactory(new PropertyValueFactory<>("releaseYear"));
        genreColumn.setCellValueFactory(new PropertyValueFactory<>("genre"));

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
                } else if (String.valueOf(movie.getReleaseYear()).contains(newValue)) {
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

        Movie newMovie = new Movie(director, title, releaseYear, genre);

        addMovieData(newMovie);

        saveMovieDataToJson();

        directorField.clear();
        titleField.clear();
        releaseYearField.clear();
        genreField.clear();
    }

    private void fillFormWithMovie(Movie movie) {
        directorField.setText(movie.getDirector());
        titleField.setText(movie.getTitle());
        releaseYearField.setText(String.valueOf(movie.getReleaseYear()));
        genreField.setText(movie.getGenre());
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

            selectedMovie.setDirector(director);
            selectedMovie.setTitle(title);
            selectedMovie.setReleaseYear(releaseYear);
            selectedMovie.setGenre(genre);

            movieData.set(movieData.indexOf(selectedMovie), selectedMovie);

            saveMovieDataToJson();
        }
    }

    @FXML
    private void Home() {
        openPage(home, "Homepage.fxml");
    }
}
