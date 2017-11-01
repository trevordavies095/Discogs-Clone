package com.DiscogsApp.model;

public class Song implements SearchObject {

    public static final SearchEnum type = SearchEnum.SONG;

    private final String title;

    private final String length;

    private final int rating;

    private final String genre;

    private final boolean explicit;

    private final int release_year;

    private final String album_bc;

    private String getExplicit(){
        if(explicit){
            return "Yes";
        }else{
            return "No";
        }
    }

    public Song(int sumrating, int numrating, int release_year, boolean explicit,
                String title, String length, String genre, String album_bc){
        this.album_bc = album_bc;
        this.genre = genre;
        this.title = title;
        this.length = length;
        this.explicit = explicit;
        this.release_year = release_year;
        this.rating = sumrating/numrating;
    }

    public static SearchEnum getType() {
        return type;
    }

    public String getTitle() {
        return title;
    }

    public String getLength() {
        return length;
    }

    public int getRating() {
        return rating;
    }

    public String getGenre() {
        return genre;
    }

    public boolean isExplicit() {
        return explicit;
    }

    public int getRelease_year() {
        return release_year;
    }

    public String getAlbum_bc() {
        return album_bc;
    }

    public String toString(){
        return "Song: " + title + ", Length: " + length + ", Genre: " +
                genre + ", Release Year: " + release_year + ", Rating: " +
                rating + ", Explicit: " + getExplicit();
    }
}
