package com.example.allesinordnung;

import com.fasterxml.jackson.core.type.TypeReference;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import com.fasterxml.jackson.databind.ObjectMapper;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.text.Text;
import javafx.util.Callback;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class BooksController extends AllesinOrdnungController {
    @FXML
    private Button cancelAddBook;
    @FXML
    private Button saveBook;
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
    private void initialize() {
        // Initialisieren der TableView-Spalten
        genreColumn.setCellValueFactory(new PropertyValueFactory<>("genre"));
        yearColumn.setCellValueFactory(new PropertyValueFactory<>("year"));
        authorColumn.setCellValueFactory(new PropertyValueFactory<>("author"));
        titleColumn.setCellValueFactory(new PropertyValueFactory<>("title"));
        ratingColumn.setCellValueFactory(new PropertyValueFactory<>("rating"));
        commentColumn.setCellValueFactory(new PropertyValueFactory<>("comment"));

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

        // Hinzufügen eines Listeners für das Suchfeld
        searchField.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredData.setPredicate(book -> {
                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }

                String lowerCaseFilter = newValue.toLowerCase();

                // Check if the input is a valid rating using RatingUtils
                if (RatingUtils.isValidRatingInput(newValue)) {
                    return RatingUtils.compareRating(book.getRating(), newValue);
                } else {
                    // Perform regular text search on other fields
                    return book.getTitle().toLowerCase().contains(lowerCaseFilter) ||
                            book.getAuthor().toLowerCase().contains(lowerCaseFilter) ||
                            String.valueOf(book.getYear()).contains(newValue) ||
                            book.getGenre().toLowerCase().contains(lowerCaseFilter) ||
                            book.getComment().toLowerCase().contains(lowerCaseFilter);
                }
            });
        });

        // Setzen der gefilterten Liste als Datenquelle für die TableView
        tableView.setItems(filteredData);

        // Listener für die Auswahl von Zeilen in der TableView
        tableView.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                fillFormWithBook(newSelection);
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




    @FXML
    private void addNewBook() {
        // Erfasst die Benutzereingaben
        String genre = genreField.getText();
        int year = Integer.parseInt(yearField.getText());
        String author = authorField.getText();
        String title = titleField.getText();
        String comment = commentField.getText();
        double rating = Double.parseDouble(ratingField.getText());

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



    private String getFullText(String text) {
        Text helper = new Text(text);
        double textWidth = helper.getBoundsInLocal().getWidth();
        double cellWidth = 150; // Set your cell width here

        if (textWidth > cellWidth) {
            return text;
        } else {
            return "";
        }
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

