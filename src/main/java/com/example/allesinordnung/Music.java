package com.example.allesinordnung;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

public class Music extends MediaItem{
    private String artist;
    public Music(){
        super();
    }
    @JsonCreator
    public Music(@JsonProperty("year") int year,
                 @JsonProperty("title") String title,
                 @JsonProperty("artist") String artist,
                 @JsonProperty("rating") double rating,
                 @JsonProperty("genre") String genre,
                 @JsonProperty("comment") String comment) {

        super(genre, year, title, comment, rating);

        // Validate year and rating before creating the music piece
        if (!super.validateNumber(String.valueOf(rating), "rating", 1, 10)) {
            throw new IllegalArgumentException("Invalid rating value");}
            this.artist = artist;
        }

    public String getArtist() {
        return artist;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }
}
