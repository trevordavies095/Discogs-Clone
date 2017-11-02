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
        if(genre != null) {
            this.genre = genre;
        } else {
            this.genre = "unknown";
        }
        this.title = title;
        if(style != null) {
            this.style = style;
        } else {
            this.style = "unknown";
        }
        if(sumrating != 0 && numrating != 0) {
            this.rating = sumrating / numrating;
        } else {
            this.rating = 0;
        }
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
