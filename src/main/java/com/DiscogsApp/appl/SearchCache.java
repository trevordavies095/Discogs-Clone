package com.DiscogsApp.appl;

import com.DiscogsApp.model.*;

import java.util.ArrayList;
import java.util.HashMap;

public class SearchCache {

    private HashMap<String, UserSearch> cache;

    public SearchCache(){
        this.cache = new HashMap<>();
    }

    public void addUser(String username){
        cache.put(username, null);
    }

    public void updateCache(String username, UserSearch userSearch){
        cache.put(username, userSearch);
    }

    public Song getSong(String username, String song){
        UserSearch cCache = cache.get(username);
        if(cCache != null) {
            ArrayList<SearchObject> data = cCache.getSongs();
            for (SearchObject curr : data) {
                if (curr.toString().equals(song) || ((Song) curr).getTitle().equals(song)) {
                    return (Song) curr;
                }
            }
        }
        return null;
    }

    public Album getAlbum(String username, String album){
        UserSearch cCache = cache.get(username);
        if(cCache != null) {
            ArrayList<SearchObject> data = cCache.getAlbums();
            for (SearchObject curr : data) {
                if (curr.toString().equals(album) || ((Album) curr).getTitle().equals(album)) {
                    return (Album) curr;
                }
            }
        }
        return null;
    }

    public Artist getArtist(String username, String artist){
        UserSearch cCache = cache.get(username);
        if(cCache != null) {
            ArrayList<SearchObject> data = cCache.getArtists();
            for (SearchObject curr : data) {
                if (curr.toString().equals(artist) || ((Artist) curr).getName().equals(artist)) {
                    return (Artist) curr;
                }
            }
        }
        return null;
    }

    public Label getLabel(String username, String label){
        UserSearch cCache = cache.get(username);
        if(cCache != null) {
            ArrayList<SearchObject> data = cCache.getLabels();
            for (SearchObject curr : data) {
                if (curr.toString().equals(label) || ((Label) curr).getName().equals(label)) {
                    return (Label) curr;
                }
            }
        }
        return null;
    }
}
