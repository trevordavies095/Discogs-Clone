package com.DiscogsApp.model;

public class Album implements SearchObject {

    private static final SearchEnum type = SearchEnum.ALBUM;

    private final String barcode;

    private final String style;

    private final String genre;

    private final String title;

    private final int rating;

    public Album(String barcode, String style, String genre, String title, int sumrating, int numrating){
        this.barcode = barcode;
        this.genre = genre;
        this.title = title;
        this.style = style;
        this.rating = sumrating/numrating;
    }

    public static SearchEnum getType() {
        return type;
    }

    public String getBarcode() {
        return barcode;
    }

    public String getStyle() {
        return style;
    }

    public String getGenre() {
        return genre;
    }

    public String getTitle() {
        return title;
    }

    public int getRating() {
        return rating;
    }

    public String toString(){
        return "Album: " + title + ", Genre: " + genre + ", Style: " +
                style + ", Barcode: " + barcode;
    }
}
