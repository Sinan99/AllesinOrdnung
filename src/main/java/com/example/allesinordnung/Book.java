package com.example.allesinordnung;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class Book {
    private String barcode;
    private int year;
    private String author;
    private String title;

    @JsonCreator
    public Book(@JsonProperty("barcode") String barcode,
                @JsonProperty("year") int year,
                @JsonProperty("author") String author,
                @JsonProperty("title") String title) {
        this.barcode = barcode;
        this.year = year;
        this.author = author;
        this.title = title;
    }



    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public String getBarcode() {
        return barcode;
    }

    public void setBarcode(String barcode) {
        this.barcode = barcode;
    }
}
