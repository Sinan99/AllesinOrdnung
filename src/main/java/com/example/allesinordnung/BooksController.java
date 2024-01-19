package com.example.allesinordnung;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.SerializationFeature;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import com.fasterxml.jackson.databind.ObjectMapper;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Rectangle;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.function.Predicate;

public class BooksController extends AllesinOrdnungController {
    @FXML
    private Button cancelAddBook;
    @FXML
    private Button updateButton;
    @FXML
    private Button saveBook;
    @FXML
    private AnchorPane body;
    @FXML
    private Rectangle headerBG;
    @FXML
    private TextField searchField;
    @FXML
    private TextField genreField;
    @FXML
    private TextField yearField;
    @FXML
    private TextField authorField;
    @FXML
    private TextField titleField;
    @FXML
    private TextField ratingField;
    @FXML
    private TextField commentField;
    @FXML
    private Button home;
    @FXML
    private TableView<Book> tableView;
    private ObservableList<Book> bookData = FXCollections.observableArrayList();
    @FXML
    private TableColumn<Book, String> genreColumn;
    @FXML
    private TableColumn<Book, Integer> yearColumn;
    @FXML
    private TableColumn<Book, String> authorColumn;
    @FXML
    private TableColumn<Book, String> titleColumn;
    @FXML
    private TableColumn<Book, Double> ratingColumn;
    @FXML
    private TableColumn<Book, String> commentColumn;
    private FilteredList<Book> filteredData;

    @FXML
    public void initialize() {

        headerBG.widthProperty().bind(body.widthProperty());
        // Initialisieren der TableView-Spalten
        genreColumn.setCellValueFactory(new PropertyValueFactory<>("genre"));
        yearColumn.setCellValueFactory(new PropertyValueFactory<>("year"));
        authorColumn.setCellValueFactory(new PropertyValueFactory<>("author"));
        titleColumn.setCellValueFactory(new PropertyValueFactory<>("title"));
        ratingColumn.setCellValueFactory(new PropertyValueFactory<>("rating"));
        commentColumn.setCellValueFactory(new PropertyValueFactory<>("comment"));

        //Tooltips für Knöpfe
        Tooltip cancelAddMovieTooltip = new Tooltip("Deletes Selected Entry");
        Tooltip updateButtonTooltip = new Tooltip("Adopts Changes");
        Tooltip saveMovieTooltip = new Tooltip("Save the Book");
        Tooltip homeButtonTooltip = new Tooltip("Go to Homepage");

        cancelAddBook.setTooltip(cancelAddMovieTooltip);
        updateButton.setTooltip(updateButtonTooltip);
        saveBook.setTooltip(saveMovieTooltip);
        home.setTooltip(homeButtonTooltip);

        // Set the cell factory for each column
        setTooltipForColumn(genreColumn);
        setTooltipForColumn(yearColumn);
        setTooltipForColumn(authorColumn);
        setTooltipForColumn(titleColumn);
        setTooltipForColumn(ratingColumn);
        setTooltipForColumn(commentColumn);
        // Lädt Daten aus der JSON-Datei
        loadDataFromJson();

        // Erstellt eine gefilterte Liste für die Suche
        filteredData = new FilteredList<>(bookData, p -> true);

        // Hinzufügen eines ChangeListeners zum searchField
        searchField.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                filteredData.setPredicate(new Predicate<Book>() {
                    @Override
                    public boolean test(Book book) {

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
                                return book.getRating() >= numericInput;
                            }
                            // Wenn nein -> wird nach dem Jahr gesucht
                            else{
                                return book.getYear() == numericInput;
                            }
                        }

                        return book.getTitle().toLowerCase().contains(lowerCaseFilter) ||
                                book.getAuthor().toLowerCase().contains(lowerCaseFilter) ||
                                book.getGenre().toLowerCase().contains(lowerCaseFilter) ||
                                book.getComment().toLowerCase().contains(lowerCaseFilter);
                    }
                });
            }
        });

        // Setzt die gefilterte Liste als Datenquelle für die TableView
        tableView.setItems(filteredData);

        // Listener für die Auswahl von Zeilen in der TableView
        tableView.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                fillFormWithBook(newSelection);
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
    private void addNewBook() {
        // Erfasst die Benutzereingaben
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

        String author = authorField.getText().isEmpty() ? null : authorField.getText();
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
        // Check if at least title and author are provided
        if (title == null || author == null) {
            showAlert("Title and author are required fields.");
            return;
        }
        // Erstellt ein neues Buchobjekt
        Book newBook = new Book(genre, year, author, title, rating, comment);

        // Fügt das neue Buch zur bookData hinzu
        addBookData(newBook);

        // Speichert die aktualisierten Daten in der JSON-Datei
        saveBookDataToJson();

        // Löscht die Textfelder im Formular
        genreField.clear();
        yearField.clear();
        authorField.clear();
        titleField.clear();
        ratingField.clear();
        commentField.clear();
    }
    private void fillFormWithBook(Book book) {
        // Befüllt die Formularfelder mit den Daten des ausgewählten Buches
        genreField.setText(book.getGenre());
        yearField.setText(String.valueOf(book.getYear())); // Jahr in String umwandeln
        authorField.setText(book.getAuthor());
        titleField.setText(book.getTitle());
        ratingField.setText(String.valueOf(book.getRating()));
        commentField.setText(book.getComment());
    }

    private void loadDataFromJson() {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            List<Book> loadedBooks = objectMapper.readValue(new File("src/main/resources/data/books.json"), new TypeReference<List<Book>>() {
            });
            // Löscht die vorhandenen Daten und fügt die geladenen Daten hinzu
            bookData.clear();
            bookData.addAll(loadedBooks);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void saveBookDataToJson() {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.enable(SerializationFeature.INDENT_OUTPUT);
        try {
            objectMapper.writeValue(new File("src/main/resources/data/books.json"), bookData);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void addBookData(Book newBook) {
        bookData.add(newBook);
    }

    @FXML
    private void deleteSelectedBook() {
        // Holt das ausgewählte Buch
        Book selectedBook = tableView.getSelectionModel().getSelectedItem();

        // Prüft, ob ein Buch ausgewählt wurde
        if (selectedBook != null) {
            // Entfernt das Buch aus der ObservableList
            bookData.remove(selectedBook);

            // Speichert die aktualisierte Liste in der JSON-Datei
            saveBookDataToJson();

            // Löscht die Formularfelder
            genreField.clear();
            yearField.clear();
            authorField.clear();
            titleField.clear();
            ratingField.clear();
            commentField.clear();
        }
    }

    @FXML
    private void updateSelectedBook() {
        // Holt das ausgewählte Buch
        Book selectedBook = tableView.getSelectionModel().getSelectedItem();

        // Prüft, ob ein Buch ausgewählt wurde
        if (selectedBook != null) {
            // Holt die neuen Daten aus den Formularfeldern
            String genre = genreField.getText();
            int year = Integer.parseInt(yearField.getText()); // Achten Sie auf Fehlerbehandlung
            String author = authorField.getText();
            String title = titleField.getText();
            double rating = Double.parseDouble(ratingField.getText());
            String comment = commentField.getText();

            // Aktualisiert die Buchdaten
            selectedBook.setGenre(genre);
            selectedBook.setYear(year);
            selectedBook.setAuthor(author);
            selectedBook.setTitle(title);
            selectedBook.setRating(rating);
            selectedBook.setComment(comment);

            // Aktualisiert das Buch in der ObservableList
            bookData.set(bookData.indexOf(selectedBook), selectedBook);

            // Speichert die aktualisierte Liste in der JSON-Datei
            saveBookDataToJson();
        }
    }

    @FXML
    private void Home() {
        openPage(home, "Homepage.fxml");
    }

    private <T> void setTooltipForColumn(TableColumn<Book, T> column) {
        column.setCellFactory(tc -> new TableCell<Book, T>() {
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

