package com.example.allesinordnung;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class Movie {
    private String director;
    private String title;
    private int releaseYear;
    private String genre;

    @JsonCreator
    public Movie(@JsonProperty("director") String director,
                 @JsonProperty("title") String title,
                 @JsonProperty("releaseYear") int releaseYear,
                 @JsonProperty("genre") String genre) {
        this.director = director;
        this.title = title;
        this.releaseYear = releaseYear;
        this.genre = genre;
    }

    public String getDirector() {
        return director;
    }

    public void setDirector(String director) {
        this.director = director;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getReleaseYear() {
        return releaseYear;
    }

    public void setReleaseYear(int releaseYear) {
        this.releaseYear = releaseYear;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }
}
