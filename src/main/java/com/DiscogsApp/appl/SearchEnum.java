package com.DiscogsApp.appl;

public enum SearchEnum {

    SONG("song", "title"),
    ALBUM("album", "title"),
    ARTIST("artist", "name"),
    LABEL("label", "name");

    private final String table;

    private final String attr;

    private SearchEnum(String table, String attr){
        this.attr = attr;
        this.table = table;
    }

    public String getAttr(){
        return this.attr;
    }

    public String getTable(){
        return this.table;
    }
}
