package com.example.allesinordnung;

import com.fasterxml.jackson.core.type.TypeReference;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import com.fasterxml.jackson.databind.ObjectMapper;


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
    private TextField barcodeField;
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
    private TableColumn<Book, String> barcodeColumn;

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
        barcodeColumn.setCellValueFactory(new PropertyValueFactory<>("barcode"));
        yearColumn.setCellValueFactory(new PropertyValueFactory<>("year"));
        authorColumn.setCellValueFactory(new PropertyValueFactory<>("author"));
        titleColumn.setCellValueFactory(new PropertyValueFactory<>("title"));
        ratingColumn.setCellValueFactory(new PropertyValueFactory<>("rating"));
        commentColumn.setCellValueFactory(new PropertyValueFactory<>("comment"));

        loadDataFromJson();

        filteredData = new FilteredList<>(bookData, p -> true);

        searchField.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredData.setPredicate(book -> {
                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }
                String lowerCaseFilter = newValue.toLowerCase();
                if (book.getTitle().toLowerCase().contains(lowerCaseFilter)) {
                    return true;
                } else if (book.getAuthor().toLowerCase().contains(lowerCaseFilter)) {
                    return true;
                } else if (String.valueOf(book.getYear()).contains(newValue)) {
                    return true;
                } else if (book.getBarcode().contains(newValue)) {
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
                fillFormWithBook(newSelection);
            }
        });
    }

    @FXML
    private void addNewBook() {
        // Erfassen Sie die Benutzereingaben
        String barcode = barcodeField.getText();
        int year = Integer.parseInt(yearField.getText());
        String author = authorField.getText();
        String title = titleField.getText();
        String comment = commentField.getText();
        double rating = Double.parseDouble(ratingField.getText());

        // Erstellen Sie ein neues Buchobjekt
        Book newBook = new Book(barcode, year, author, title,rating,comment);

        // Fügen Sie das neue Buch zur bookData hinzu
        addBookData(newBook);

        // Save the updated data to the JSON file
        saveBookDataToJson();

        // Löschen Sie die Textfelder im Formular
        barcodeField.clear();
        yearField.clear();
        authorField.clear();
        titleField.clear();
        ratingField.clear();
        commentField.clear();
    }

    private void fillFormWithBook(Book book) {
        // Befüllen Sie die Formularfelder mit den Daten des ausgewählten Buches
        barcodeField.setText(book.getBarcode());
        yearField.setText(String.valueOf(book.getYear())); // Jahr in String umwandeln
        authorField.setText(book.getAuthor());
        titleField.setText(book.getTitle());
        ratingField.setText(String.valueOf(book.getRating()));
        commentField.setText(String.valueOf(book.getComment()));
    }

    private void loadDataFromJson() {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            List<Book> loadedBooks = objectMapper.readValue(new File("src/main/resources/data/books.json"), new TypeReference<List<Book>>() {
            });
            // Clear the existing data and add the loaded data
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
        // Holen Sie das ausgewählte Buch
        Book selectedBook = tableView.getSelectionModel().getSelectedItem();

        // Prüfen Sie, ob ein Buch ausgewählt wurde
        if (selectedBook != null) {
            // Entfernen Sie das Buch aus der ObservableList
            bookData.remove(selectedBook);

            // Speichern Sie die aktualisierte Liste in der JSON-Datei
            saveBookDataToJson();
            barcodeField.clear();
            yearField.clear();
            authorField.clear();
            titleField.clear();
            ratingField.clear();;
            commentField.clear();
        }
    }

    @FXML
    private void updateSelectedBook() {
        // Holen Sie das ausgewählte Buch
        Book selectedBook = tableView.getSelectionModel().getSelectedItem();

        // Prüfen Sie, ob ein Buch ausgewählt wurde
        if (selectedBook != null) {
            // Holen Sie die neuen Daten aus den Formularfeldern
            String barcode = barcodeField.getText();
            int year = Integer.parseInt(yearField.getText()); // Achten Sie auf Fehlerbehandlung
            String author = authorField.getText();
            String title = titleField.getText();
            double raing = Double.parseDouble(ratingField.getText());
            String comment = commentField.getText();

            // Aktualisieren Sie die Buchdaten
            selectedBook.setBarcode(barcode);
            selectedBook.setYear(year);
            selectedBook.setAuthor(author);
            selectedBook.setTitle(title);
            selectedBook.setRating(raing);
            selectedBook.setComment(comment);

            // Aktualisieren Sie das Buch in der ObservableList
            bookData.set(bookData.indexOf(selectedBook), selectedBook);

            // Speichern Sie die aktualisierte Liste in der JSON-Datei
            saveBookDataToJson();
        }
    }

    @FXML
    private void Home() {
        openPage(home, "Homepage.fxml");
    }
}

