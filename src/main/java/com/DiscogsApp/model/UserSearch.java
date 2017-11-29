package com.DiscogsApp.model;

import java.util.ArrayList;

public class UserSearch {

    private ArrayList<SearchObject> songs;
    private ArrayList<SearchObject> albums;
    private ArrayList<SearchObject> artists;
    private ArrayList<SearchObject> labels;

    public UserSearch(ArrayList<SearchObject> songs, ArrayList<SearchObject> albums,
                      ArrayList<SearchObject> artists, ArrayList<SearchObject> labels){
        this.songs = songs;
        this.albums = albums;
        this.artists = artists;
        this.labels = labels;
    }

    public ArrayList<SearchObject> getSongs() {
        return songs;
    }

    public ArrayList<SearchObject> getAlbums() {
        return albums;
    }

    public ArrayList<SearchObject> getArtists() {
        return artists;
    }

    public ArrayList<SearchObject> getLabels() {
        return labels;
    }
}
