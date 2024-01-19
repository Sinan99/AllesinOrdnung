package com.example.allesinordnung;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import static com.example.allesinordnung.AllesinOrdnungController.showAlert;

public class Book extends MediaItem {
    private String author;
    public Book() {
        super();
    }
    public Book(String genre, int year, String author, String title, double rating, String comment) {
        super(genre, year, title, comment, rating);
        setAuthor(author);
    }


    @JsonCreator
    public Book(@JsonProperty("genre") String genre,
                @JsonProperty("year") int year,
                @JsonProperty("author") String author,
                @JsonProperty("title") String title,
                @JsonProperty("rating") Double rating,
                @JsonProperty("comment") String comment) {

        super(genre,year,title,comment,rating);
        this.author = author;
        // Validate year and rating before creating the book
        if (!validateNumber(String.valueOf(year), "year", Integer.MIN_VALUE, Integer.MAX_VALUE)) {
            throw new IllegalArgumentException("Invalid year value");
        }

        if (rating != null && (rating < 1 || rating > 10)) {
            throw new IllegalArgumentException("Invalid rating value");
        }
    }




    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

}
