package com.DiscogsApp.model;

public class URSong implements Comparable<URSong>
{
    // Class constants

    // Class variables
    private String title;
    private String artistName;
    private String albumName;
    private int rating;

    public URSong(String title, String albumName, String artistName, int rating)
    {
        // Local constants

        // Local variables

        /****** start URSong() ******/

        this.title = title;
        this.rating = rating;
        this.artistName = artistName;
        this.albumName = albumName;
    }

    public int getRating(){
        return this.rating;
    }
    public String getTitle(){
        return this.title;
    }
    public String getArtistName(){
        return this.artistName;
    }

    @Override
    public int compareTo(URSong u)
    {
        // Local constants

        // Local variables

        /****** start compareTo() ******/

        return u.getRating() - this.rating;
    }
}
