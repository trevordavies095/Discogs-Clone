package com.DiscogsApp.appl;

import com.DiscogsApp.model.*;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Searches cache for DisClones
 * @author Patrick Ehrenreich (pxe1833@rit.edu)
 * @author Loren Davies (ltd9938@rit.edu)
 * @since October 21st, 2017
 */
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
        ArrayList<SearchObject> data = cCache.getSongs();

        /****** start getSong() ******/

        if(cCache != null)
        {
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
        ArrayList<SearchObject> data = cCache.getAlbums();

        /****** start getAlbum() ******/

        if(cCache != null)
        {
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
        ArrayList<SearchObject> data = cCache.getArtists();

        /****** start getArtist() ******/

        if(cCache != null)
        {
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
        ArrayList<SearchObject> data = cCache.getLabels();

        /****** start getLabel() ******/

        if(cCache != null)
        {
            for (SearchObject curr : data)
            {
                if (curr.toString().equals(label) || ((Label) curr).getName().equals(label))
                    return (Label) curr;
            }
        }
        return null;
    }
}
