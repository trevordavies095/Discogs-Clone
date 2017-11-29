package com.DiscogsApp.model;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class UserAccount {

    private final String firstname;

    private final String lastname;

    private final String username;

    private ArrayList<String> songsRated;

    public UserAccount(String firstname, String lastname, String username,
                       ArrayList<String> songsRated){
        this.firstname = firstname;
        this.lastname = lastname;
        this.username = username;
        this.songsRated = songsRated;
    }

    public String getFirstname() {
        return firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public String getUsername() {
        return username;
    }

    public ArrayList<String> getSongsRated() {
        return songsRated;
    }
}

