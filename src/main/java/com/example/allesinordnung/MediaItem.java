package com.example.allesinordnung;

public class MediaItem {

    private String genre;
    private int year;
    private String title;
    private String comment;
    private double rating;

    public MediaItem(){

    }

    ////hbfsdhfb

    public MediaItem(String genre, int year, String title, String comment, double rating){
        this.genre = genre;
        this.year = year;
        this.title = title;
        this.comment = comment;
        this.rating = rating;
    }
    public String getGenre() {
        return genre;
    }

    public int getYear() {
        return year;
    }

    public String getTitle() {
        return title;
    }

    public String getComment() {
        return comment;
    }

    public double getRating() {
        return rating;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }
}
