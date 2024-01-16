package com.example.allesinordnung;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class Music {
    private String artistName;
    private String songTitle;
    private String songDate;
    private Double rating;
    private String comment;

    @JsonCreator
    public Music(@JsonProperty("artistName") String artistName,
                 @JsonProperty("songTitle") String songTitle,
                 @JsonProperty("songDate") String songDate,
                 @JsonProperty("rating") Double rating,
                 @JsonProperty("comment") String comment) {

        this.artistName = artistName;
        this.songTitle = songTitle;
        this.songDate = songDate;
        this.rating = rating;
        this.comment = comment;

    }

    public String getArtistName() {
        return artistName;
    }

    public void setArtistName(String artistName) {
        this.artistName = artistName;
    }

    public String getSongTitle() {
        return songTitle;
    }

    public void setSongTitle(String songTitle) {
        this.songTitle = songTitle;
    }

    public String getSongDate() {
        return songDate;
    }

    public void setSongDate(String songDate) {
        this.songDate = songDate;
    }

    public Double getRating() {
        return rating;
    }

    public void setRating(Double rating) {
        this.rating = rating;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }
}
