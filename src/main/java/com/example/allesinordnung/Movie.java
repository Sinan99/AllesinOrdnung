package com.example.allesinordnung;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

public class Movie extends MediaItem {

    private String director;

    @JsonCreator
    public Movie(@JsonProperty("releaseYear") @JsonInclude(JsonInclude.Include.NON_NULL) Integer year,
                 @JsonProperty("title") String title,
                 @JsonProperty("director") String director,
                 @JsonProperty("rating") @JsonInclude(JsonInclude.Include.NON_NULL) Double rating,
                 @JsonProperty("genre") String genre,
                 @JsonProperty("comment") String comment) {

        super(genre, (year != null) ? year : 0, title, comment, rating);
        this.director = director;
    }

    public String getDirector() {
        return director;
    }

    public void setDirector(String director) {
        this.director = director;
    }
}
