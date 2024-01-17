package com.example.allesinordnung;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class Book extends MediaItem {
    private String author;


    @JsonCreator
    public Book(@JsonProperty("genre") String genre,
                @JsonProperty("year") int year,
                @JsonProperty("author") String author,
                @JsonProperty("title") String title,
                @JsonProperty("rating") Double rating,
                @JsonProperty("comment") String comment) {

        super(genre,year,title,comment,rating);
        this.author = author;

    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

}
