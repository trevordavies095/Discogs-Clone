package com.DiscogsApp.model;

public class Song implements SearchObject
{
    // Class constants
    public static final SearchEnum type = SearchEnum.SONG;
    private final String title;
    private final String length;
    private final double rating;
    private final String genre;
    private final boolean explicit;
    private final int releaseYear;
    private final Album album;
    private final int ID;

    // Class variables

    public Song(int sumrating, int numrating, int releaseYear, boolean explicit,
                String title, String length, String genre, Album album, int ID)
    {
        // Local constants

        // Local variables

        /****** start Song() ******/

        if(genre != null)
            this.genre = genre;
        else
            this.genre = "unknown";

        this.title = title;
        this.length = length;
        this.explicit = explicit;
        this.releaseYear = releaseYear;
        this.album = album;
        this.ID = ID;

        if(sumrating != 0 && numrating != 0)
            this.rating = (double)sumrating / (double)numrating;
        else
            this.rating = 0;
    }

    private String getExplicit()
    {
        // Local constants

        // Local variables

        /****** start getExplicit() ******/

        if(explicit)
            return "Yes";
        else
            return "No";
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
    public double getRating() {
        return rating;
    }
    public String getGenre() {
        return genre;
    }
    public String isExplicit() {
        return getExplicit();
    }
    public int getReleaseYear() {
        return releaseYear;
    }
    public Album getAlbum(){ return album; }
    public int getID(){ return ID; }
    public String toString(){
        return title + " by " + album.getArtist().getName();
    }

    @Override
    public boolean equals(Object o)
    {
        // Local constants

        // Local variables
        Song song;

        /****** start equals() ******/

        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;

        song = (Song) o;

        if (rating != song.rating)
            return false;
        if (explicit != song.explicit)
            return false;
        if (releaseYear != song.releaseYear)
            return false;
        if (!title.equals(song.title))
            return false;
        if (!length.equals(song.length))
            return false;

        return genre.equals(song.genre);
    }
}
