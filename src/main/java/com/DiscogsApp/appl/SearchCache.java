package com.DiscogsApp.appl;

import com.DiscogsApp.model.*;
import java.util.ArrayList;
import java.util.HashMap;

public class SearchCache
{
    // Class constants

    // Class variables
    private HashMap<String, UserSearch> cache;

    /**
     * Constructor for SearchCache class
     */
    public SearchCache()
    {
        // Local constants

        // Local variables

        /****** start SearchCache() ******/

        this.cache = new HashMap<>();
    }

    public void addUser(String username)
    {
        // Local constants

        // Local variables

        /****** start addUser() ******/

        cache.put(username, null);
    }

    public void updateCache(String username, UserSearch userSearch)
    {
        // Local constants

        // Local variables

        /****** start updateCache() ******/

        cache.put(username, userSearch);
    }

    public Song getSong(String username, String song)
    {
        // Local constants

        // Local variables
        UserSearch cCache = cache.get(username);

        /****** start getSong() ******/

        if(cCache != null)
        {
            ArrayList<SearchObject> data = cCache.getSongs();
            for (SearchObject curr : data)
            {
                if (curr.toString().equals(song) || ((Song) curr).getTitle().equals(song))
                    return (Song) curr;
            }
        }
        return null;
    }

    public Album getAlbum(String username, String album)
    {
        // Local constants

        // Local variables
        UserSearch cCache = cache.get(username);

        /****** start getAlbum() ******/

        if(cCache != null)
        {
            ArrayList<SearchObject> data = cCache.getAlbums();
            for (SearchObject curr : data)
            {
                if (curr.toString().equals(album) || ((Album) curr).getTitle().equals(album))
                    return (Album) curr;
            }
        }
        return null;
    }

    public Artist getArtist(String username, String artist)
    {
        // Local constants

        // Local variables
        UserSearch cCache = cache.get(username);

        /****** start getArtist() ******/

        if(cCache != null)
        {
            ArrayList<SearchObject> data = cCache.getArtists();
            for (SearchObject curr : data)
            {
                if (curr.toString().equals(artist) || ((Artist) curr).getName().equals(artist))
                    return (Artist) curr;
            }
        }
        return null;
    }

    public Label getLabel(String username, String label)
    {
        // Local constants

        // Local variables
        UserSearch cCache = cache.get(username);

        /****** start getLabel() ******/

        if(cCache != null)
        {
            ArrayList<SearchObject> data = cCache.getLabels();
            for (SearchObject curr : data)
            {
                if (curr.toString().equals(label) || ((Label) curr).getName().equals(label))
                    return (Label) curr;
            }
        }
        return null;
    }
}
