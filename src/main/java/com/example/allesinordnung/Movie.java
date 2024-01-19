package com.example.allesinordnung;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

public class Movie extends MediaItem {
    private String director;
    public Movie(){
        super();
    }
    public Movie(String genre, int year, String director, String title, double rating, String comment) {
        super(genre, year, title, comment, rating);
        setDirector(director);
    }
    @JsonCreator
    public Movie(@JsonProperty("year") int year,
                 @JsonProperty("title") String title,
                 @JsonProperty("director") String director,
                 @JsonProperty("rating") Double rating,
                 @JsonProperty("genre") String genre,
                 @JsonProperty("comment") String comment) {

        super(genre, year, title, comment, rating);

        // Validate year and rating before creating the movie
        if (!super.validateNumber(String.valueOf(rating), "rating", 1, 10)) {
            throw new IllegalArgumentException("Invalid rating value");
        }

        this.director = director;
    }
    public String getDirector() {
        return director;
    }

    public void setDirector(String director) {
        this.director = director;
    }
}
