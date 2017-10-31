package com.DiscogsApp.model;

public class Artist implements SearchObject {

    private static final SearchEnum type = SearchEnum.ARTIST;

    private final int artist_id;

    private final String name;

    private final String real_name;

    private final int debut_year;

    private final String label_name;

    public Artist(int artist_id, int debut_year, String name, String real_name, String label_name){
        this.artist_id = artist_id;
        this.debut_year = debut_year;
        this.name = name;
        this.real_name = real_name;
        this.label_name = label_name;
    }

    public String getLabel_name() {
        return label_name;
    }

    public static SearchEnum getType() {
        return type;
    }

    public int getArtist_id() {
        return artist_id;
    }

    public String getName() {
        return name;
    }

    public String getReal_name() {
        return real_name;
    }

    public int getDebut_year() {
        return debut_year;
    }
}
