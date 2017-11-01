package com.DiscogsApp.model;

public enum SearchEnum {

    SONG("song", "song.title", "album_bc", "song_id"),
    ALBUM("album", "album.title", "artist_id", "barcode"),
    ARTIST("artist", "artist.name", "label_name", "artist_id"),
    LABEL("record_label", "record_label.name", "none", "name"),
    GENERIC("null", "null", "null", "null");

    private final String table;

    private final String frName;

    private final String fkey;

    private final String pkey;

    private SearchEnum(String table, String frName, String fkey, String pkey){
        this.frName = frName;
        this.table = table;
        this.fkey = fkey;
        this.pkey = pkey;
    }

    public String getfrName(){
        return this.frName;
    }

    public String getTable(){
        return this.table;
    }

    public String getFkey(){
        return this.fkey;
    }

    public String getPkey(){
        return this.pkey;
    }
}
