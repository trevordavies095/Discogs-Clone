package com.DiscogsApp.model;

public class Album implements SearchObject {

    private static final SearchEnum type = SearchEnum.ALBUM;

    private final String barcode;

    private final String style;

    private final String genre;

    private final String title;

    private final double rating;

    private final Artist artist;

    public Album(String barcode, String style, String genre, String title, int rating, Artist artist){
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
        this.rating = rating;
        this.artist = artist;
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

    public double getRating() {
        return rating;
    }

    public Artist getArtist(){ return this.artist; }

    public String toString(){
        return title + " by " + artist.getName();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Album album = (Album) o;

        if (rating != album.rating) return false;
        if (barcode != null ? !barcode.equals(album.barcode) : album.barcode != null) return false;
        if (style != null ? !style.equals(album.style) : album.style != null) return false;
        if (genre != null ? !genre.equals(album.genre) : album.genre != null) return false;
        if (artist != null ? !artist.equals(album.artist) : album.artist != null) return false;
        return title != null ? title.equals(album.title) : album.title == null;
    }
}
