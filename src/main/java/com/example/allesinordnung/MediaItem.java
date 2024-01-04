package com.example.allesinordnung;

public class MediaItem {

    private String barcode;
    private int year;
    private String title;
    private String comment;
    private double rating;

    public MediaItem(){

    }

    public MediaItem(String barcode,int year,String title,String comment,double rating){
        this.barcode = barcode;
        this.year = year;
        this.title = title;
        this.comment = comment;
        this.rating = rating;
    }

    public String getBarcode() {
        return barcode;
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

    public void setBarcode(String barcode) {
        this.barcode = barcode;
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
