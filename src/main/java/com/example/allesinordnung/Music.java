package com.example.allesinordnung;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

public class Music extends MediaItem{
   private String artist;

    @JsonCreator
    public Music(@JsonProperty("releaseYear") @JsonInclude(JsonInclude.Include.NON_NULL) Integer year,
                 @JsonProperty("title") String title,
                 @JsonProperty("artist") String artist,
                 @JsonProperty("rating") @JsonInclude(JsonInclude.Include.NON_NULL) Double rating,
                 @JsonProperty("genre") String genre,
                 @JsonProperty("comment") String comment) {

        super(genre, (year != null) ? year : 0, title, comment, rating);
        this.artist = artist;
    }

    public Music(String artistName, String songTitle, String songDate, Double rating, String genre, String comment) {
        this.artist = artistName;
        this.setTitle(songTitle);
        this.setYear(Integer.parseInt(songDate));
        this.setRating(rating);
        this.setGenre(genre);
        this.setComment(comment);
    }

    public String getArtist() {
        return artist;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }
}
