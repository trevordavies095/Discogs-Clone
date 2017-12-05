package com.DiscogsApp.appl;

import com.DiscogsApp.model.*;

import java.sql.*;
import java.util.*;


public class SQLManager
{
    // Class constants
    static private final String dburl = "jdbc:postgresql://reddwarf.cs.rit.edu:5432/p32004e";
    static private final String dbusername = "p32004e";
    static private final String dbpassword = "ievip6se0pha1sahchuD";
    static private final String SQL_DEFAULT = "%%";

    // Class variables
    private Connection con;

    public SQLManager()
    {
        // Local constants

        // Local variables

        /****** start SQLManager() ******/

        try
        {
            this.con = DriverManager.getConnection(dburl, dbusername, dbpassword);
        }

        catch(SQLException ex)
        {
            ex.printStackTrace();
        }
    }

    private int getAtttendeeCount(int eventID){
        try {
            PreparedStatement atCnt = con.prepareStatement("SELECT count(event_attendees.user_username) " +
                    "FROM event_attendees WHERE event_id = ?");
            atCnt.setInt(1, eventID);
            ResultSet numAtnd = atCnt.executeQuery();
            numAtnd.next();
            return numAtnd.getInt("count");
        } catch(SQLException ex){
            ex.printStackTrace();
            return 0;
        }
    }

    private ArrayList<String> stringifyEvents(ResultSet events)
    {
        // Local constants

        // Local variables
        String curr;
        String currTS;
        ArrayList<String> results = new ArrayList<>();

        /****** start stringifyEvents() ******/

        try
        {
            while (events.next())
            {
                curr = events.getString("group_name") + " performing at " +
                        events.getString("event_location") + " on ";
                currTS = DiscEvent.parseTimestamp(events.getString("event_time"));
                curr = curr + currTS + " (ID: " + events.getInt("event_id") + ")";
                results.add(curr);
            }
            return results;
        }

        catch(SQLException ex)
        {
            ex.printStackTrace();
            return null;
        }
    }

    private UserSearch parseResults(ResultSet rset, int outerShell)
    {
        // Local constants

        // Local variables

        ArrayList<SearchObject> labels = new ArrayList<>();
        ArrayList<SearchObject> albums = new ArrayList<>();
        ArrayList<SearchObject> artists = new ArrayList<>();
        ArrayList<SearchObject> songs = new ArrayList<>();
        Label cLabel = null;
        Artist cArtist = null;
        Album cAlbum = null;
        Song cSong = null;

        /****** start parseResults() ******/

        if(outerShell < 1)
            return null;

        try
        {
            while (rset.next())
            {
                cLabel = new Label(rset.getString("label_name"), rset.getInt("formation_year"),
                        rset.getDouble("net_worth"));
                if(!labels.contains(cLabel)) labels.add(cLabel);
                if(outerShell >= 2){
                    cArtist = new Artist(rset.getInt("artist_id"), rset.getInt("debut_year"),
                            rset.getString("group_name"), rset.getString("real_name"),
                            rset.getString("label_name"), cLabel);
                    if(!artists.contains(cArtist)) artists.add(cArtist);
                }
                if(outerShell >= 3){
                    cAlbum = new Album(rset.getString("album_bc"), rset.getString("album_style"),
                            rset.getString("album_genre"), rset.getString("album_title"),
                            rset.getInt("album_rating"), cArtist);
                    if(!albums.contains(cAlbum)) albums.add(cAlbum);
                }
                if(outerShell >= 4){
                    cSong = new Song(rset.getInt("song_sratings"), rset.getInt("song_tratings"),
                            rset.getInt("song_release_year"), rset.getBoolean("song_explicit"),
                            rset.getString("song_title"), rset.getString("song_length"),
                            rset.getString("song_genre"), cAlbum,
                            Integer.parseInt(rset.getString("song_id")));
                    if(!songs.contains(cSong)) songs.add(cSong);
                }
            }

            return new UserSearch(songs, albums, artists, labels);
        }

        catch (SQLException ex)
        {
            ex.printStackTrace();
            return null;
        }
    }

    public boolean validateUsername(String username)
    {
        // Local constants

        // Local variables
        ResultSet rset;

        /****** start validateUsername() ******/

        try
        {
            PreparedStatement qry = con.prepareStatement("SELECT username FROM users" +
                    " WHERE username = ?");
            qry.setString(1, username);
            rset = qry.executeQuery();

            return rset.next();
        }

        catch(SQLException ex)
        {
            ex.printStackTrace();
            return false;
        }
    }

    public boolean validatePassword(String username, String password)
    {
        // Local constants

        // Local variables
        ResultSet rset;

        /****** start validatePassword() ******/

        try
        {
            PreparedStatement qry = con.prepareStatement("SELECT password FROM users " +
                    "WHERE users.username = ?");
            qry.setString(1, username);
            rset = qry.executeQuery();
            rset.next();

            return rset.getString("password").equals(password);
        }

        catch(SQLException ex)
        {
            ex.printStackTrace();
            return false;
        }
    }

    public boolean validateAdministrator(String username)
    {
        // Local constants

        // Local variables


        /****** start validateAdministrator() ******/

        try
        {
            PreparedStatement qry = con.prepareStatement("SELECT administrator FROM users WHERE username = ?");
            qry.setString(1, username);
            ResultSet rset = qry.executeQuery();
            rset.next();

            return rset.getBoolean("administrator");
        }

        catch(SQLException ex)
        {
            ex.printStackTrace();
            return false;
        }
    }

    public void addAttendee(int eventID, String username, boolean attending){
        try {
            PreparedStatement addAtt = con.prepareStatement("INSERT INTO event_attendees VALUES " +
                    "(?, ?, ?)");
            addAtt.setInt(1, eventID);
            addAtt.setString(2, username);
            addAtt.setBoolean(3, attending);
            addAtt.executeUpdate();
        } catch(SQLException ex) {
            ex.printStackTrace();
        }
    }

    public int addUser(String username, String password, String firstname, String lastname) {
        // Local constants

        // Local variables
        boolean nameTaken;
        boolean goodInsert;

        /****** start addUser() ******/

        try
        {
            nameTaken = validateUsername(username);

            if(!(nameTaken))
            {
                PreparedStatement nqry = con.prepareStatement("INSERT INTO users VALUES (" +
                        "?, ?, ?, ?, FALSE)");
                nqry.setString(1, username);
                nqry.setString(2, password);
                nqry.setString(3, firstname);
                nqry.setString(4, lastname);
                nqry.executeUpdate();
                goodInsert = validateUsername(username);

                if(goodInsert)
                    return 1;
                else
                    return 2;
            }

            else
                return 0;
        }

        catch(SQLException ex)
        {
            ex.printStackTrace();
            return 2;
        }
    }

    public UserSearch parseSearch(String song, String album, String artist, String label)
    {
        // Local constants

        // Local variables
        ResultSet pertinent;
        PreparedStatement getAll;
        int outerShell = 0;

        /****** parseSearch() ******/

        if(label.equals("")) label = SQL_DEFAULT; else label = "%"+label+"%";
        if(artist.equals("")) artist = SQL_DEFAULT; else artist = "%"+artist+"%";
        if(album.equals("")) album = SQL_DEFAULT; else album = "%"+album+"%";
        if(song.equals("")) song = SQL_DEFAULT; else song = "%"+song+"%";

        try
        {
            if(!(song.equals(SQL_DEFAULT)))
            {
                getAll = con.prepareStatement("SELECT * FROM song NATURAL JOIN album " +
                        "NATURAL JOIN artist NATURAL JOIN record_label WHERE song_title LIKE ?" +
                        "AND album_title LIKE ? AND group_name LIKE ? AND label_name LIKE ?");
                getAll.setString(1, song);
                getAll.setString(2, album);
                getAll.setString(3, artist);
                getAll.setString(4, label);
                outerShell = 4;
            }

            else if(!(album.equals(SQL_DEFAULT)))
            {
                getAll = con.prepareStatement("SELECT * FROM album NATURAL JOIN artist " +
                        "NATURAL JOIN record_label WHERE album_title LIKE ? AND group_name LIKE ? " +
                        "AND label_name LIKE ?");
                getAll.setString(1, album);
                getAll.setString(2, artist);
                getAll.setString(3, label);
                outerShell = 3;
            }

            else if(!(artist.equals(SQL_DEFAULT)))
            {
                getAll = con.prepareStatement("SELECT * FROM artist NATURAL JOIN record_label " +
                        "WHERE group_name LIKE ? AND label_name LIKE ?");
                getAll.setString(1, artist);
                getAll.setString(2, label);
                outerShell = 2;
            }

            else if(!(label.equals(SQL_DEFAULT)))
            {
                getAll = con.prepareStatement("SELECT * FROM record_label WHERE label_name " +
                        "LIKE ?");
                getAll.setString(1, label);
                outerShell = 1;
            }

            else
                return null;

            pertinent = getAll.executeQuery();
            return parseResults(pertinent, outerShell);
        }

        catch(SQLException ex)
        {
            ex.printStackTrace();
            return null;
        }
    }

    public int addLocation(String name, String cap, String city, String state) {
        try {
            PreparedStatement addLoc = con.prepareStatement("INSERT INTO locations VALUES " +
                    "(?, ?, ?, ?)");
            addLoc.setString(1, name);
            addLoc.setInt(2, Integer.parseInt(cap));
            addLoc.setString(3, city);
            addLoc.setString(4, state);
            return addLoc.executeUpdate();
        } catch(SQLException ex) {
            ex.printStackTrace();
            return 0;
        }
    }

    public int addEdition(String style, String baseBC, String edBC, String title) {
        try {
            PreparedStatement addEdi = con.prepareStatement("INSERT INTO edition VALUES " +
                    "(?, ?, ?, ?)");
            addEdi.setString(1, style);
            addEdi.setString(2, baseBC);
            addEdi.setString(3, title);
            addEdi.setString(4, edBC);
            return addEdi.executeUpdate();
        } catch(SQLException ex) {
            ex.printStackTrace();
            return 0;
        }
    }

    public int addSong(String title, String length, String genre, boolean explicit,
                       String release, String albumBC)
    {
        // Local constants

        // Local variables

        /****** start addSong() ******/

        try
        {
            PreparedStatement mkID = con.prepareStatement("SELECT COUNT (*) FROM song");
            ResultSet rset = mkID.executeQuery();
            rset.next();
            int songID = 1103 + rset.getInt("count");
            PreparedStatement pstate = con.prepareStatement("INSERT INTO song VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?)");
            pstate.setString(1, title);
            pstate.setString(2, length);
            pstate.setInt(3, 0);
            pstate.setInt(4,0);
            pstate.setString(5, genre);
            pstate.setBoolean(6, explicit);
            pstate.setInt(7, Integer.parseInt(release));
            pstate.setString(8, albumBC);
            pstate.setString(9, Integer.toString(songID));
            return pstate.executeUpdate();
        }

        catch(SQLException ex)
        {
            ex.printStackTrace();
            return 0;
        }
    }

    public int addArtist(String title, String realName, String labelName,
                         String debutYear)
    {
        // Local constants

        // Local variables

        /****** start addArtist() ******/

        try
        {
            PreparedStatement mkID = con.prepareStatement("SELECT COUNT (*) FROM artist");
            ResultSet rset = mkID.executeQuery();
            rset.next();
            int artistID = 1 + rset.getInt("count");
            PreparedStatement pstate = con.prepareStatement("INSERT INTO artist VALUES(?, ?, ?, ?, ?)");
            pstate.setString(1, title);
            pstate.setInt(2, artistID);
            pstate.setString(3, realName);
            pstate.setInt(4, Integer.parseInt(debutYear));
            pstate.setString(5, labelName);
            return pstate.executeUpdate();
        }
        catch(SQLException ex)
        {
            ex.printStackTrace();
            return 0;
        }
    }

    public int addAlbum(String barcode, String style, String genre, String title,
                        String artistID)
    {
        // Local constants

        // Local variables

        /****** start addAlbum() ******/

        try
        {
            PreparedStatement pstate = con.prepareStatement("INSERT INTO album VALUES(?, ?, ?, ?, ?, ?)");
            pstate.setString(1, barcode);
            pstate.setString(2, style);
            pstate.setString(3, genre);
            pstate.setInt(6,0);
            pstate.setString(4, title);
            pstate.setInt(5, Integer.parseInt(artistID));
            return pstate.executeUpdate();
        }

        catch(SQLException ex)
        {
            ex.printStackTrace();
            return 0;
        }
    }

    public int addLabel(String name, String formYear, String netWorth)
    {
        // Local constants

        // Local variables

        /****** start addLabel() ******/
        if(formYear.equals("")) formYear = "0";
        if(netWorth.equals("")) netWorth = "0";
        try
        {
            PreparedStatement pstate = con.prepareStatement("INSERT INTO record_label VALUES(?, ?, ?)");
            pstate.setString(1, name);
            pstate.setInt(2, Integer.parseInt(formYear));
            pstate.setDouble(3, Double.parseDouble(netWorth));
            return pstate.executeUpdate();
        }

        catch(SQLException ex)
        {
            ex.printStackTrace();
            return 0;
        }
    }

    public ArrayList<String> getAlbumSongs(Album album)
    {
        // Local constants

        // Local variables

        /****** start getAlbumSongs() ******/

        try
        {
            ArrayList<String> results = new ArrayList<>();
            PreparedStatement pstate = con.prepareStatement("SELECT * FROM song WHERE song.album_bc " +
                    "LIKE ?");
            pstate.setString(1, album.getBarcode());
            ResultSet rset = pstate.executeQuery();

            while(rset.next())
                results.add(rset.getString("song_title"));

            return results;
        }

        catch(SQLException ex)
        {
            ex.printStackTrace();
            return null;
        }
    }

    public ArrayList<String> getArtistAlbums(Artist artist)
    {
        // Local constants

        // Local variables

        /****** start getArtistAlbums() ******/

        try
        {
            ArrayList<String> results = new ArrayList<>();
            PreparedStatement pstate = con.prepareStatement("SELECT * FROM album WHERE album.artist_id " +
                    "LIKE ?");
            pstate.setString(1, Integer.toString(artist.getArtistID()));
            ResultSet rset = pstate.executeQuery();

            while(rset.next())
                results.add(rset.getString("album_title"));

            return results;
        }

        catch(SQLException ex)
        {
            ex.printStackTrace();
            return null;
        }
    }

    public ArrayList<String> getEvents()
    {
        // Local constants

        // Local variables

        /****** start getEvents() ******/

        try
        {
           PreparedStatement getem = con.prepareStatement("SELECT event_time," +
                   " event_location, event_id, artist.group_name FROM events INNER JOIN " +
                   "artist ON events.event_artist = artist.artist_id");
           ResultSet events = getem.executeQuery();
           return stringifyEvents(events);
        }

        catch(SQLException ex)
        {
            ex.printStackTrace();
            return null;
        }
    }

    public ArrayList<String> getEvents(int artistID)
    {
        // Local constants

        // Local variables

        /****** start getEvents() ******/

        try
        {
            PreparedStatement getEv = con.prepareStatement("SELECT event_time," +
                    " event_location, event_id, artist.group_name FROM events INNER JOIN " +
                    "artist ON events.event_artist = artist.artist_id WHERE artist_id = ?");
            getEv.setString(1, Integer.toString(artistID));
            ResultSet events = getEv.executeQuery();
            return stringifyEvents(events);
        }

        catch(SQLException ex)
        {
            ex.printStackTrace();
            return null;
        }
    }

    public ArrayList<String> getLabelArtists(Label label)
    {
        // Local constants

        // Local variables

        /****** start getLabelArtists() ******/

        try
        {
            ArrayList<String> results = new ArrayList<>();
            PreparedStatement pstate = con.prepareStatement("SELECT * FROM artist WHERE artist.label_name " +
                    "LIKE ?");
            pstate.setString(1, label.getName());
            ResultSet rset = pstate.executeQuery();

            while(rset.next())
                results.add(rset.getString("group_name"));

            return results;
        }

        catch(SQLException ex)
        {
            ex.printStackTrace();
            return null;
        }
    }

    public ArrayList<String> getAlbumEditions(Album album)
    {
        // Local constants

        // Local variables

        /****** start getAlbumEditions() ******/

        try
        {
            ArrayList<String> editions = new ArrayList<>();
            PreparedStatement getEd = con.prepareStatement("SELECT * FROM edition WHERE " +
                    "base_barcode = ?");
            getEd.setString(1, album.getBarcode());
            ResultSet eds = getEd.executeQuery();

            while(eds.next())
            {
                editions.add(eds.getString("edition_title") + ", barcode: " +
                        eds.getString("edition_barcode"));
            }

            return editions;
        }

        catch(SQLException ex)
        {
            ex.printStackTrace();
            return null;
        }
    }

    public DiscEvent getSingleEvent(String eventString)
    {
        // Local constants

        // Local variables

        /****** start getSingleEvent() ******/

        try
        {
            String[] splitter = eventString.split("ID: ");
            String strID = splitter[splitter.length - 1].trim().replace(")", "");
            int eID = Integer.parseInt(strID);
            PreparedStatement getEvent = con.prepareStatement("SELECT event_name, event_location, event_time," +
                    "event_id, event_time, artist.group_name FROM events INNER JOIN artist ON " +
                    "events.event_artist = artist.artist_id WHERE event_id = ?");
            getEvent.setInt(1, eID);
            ResultSet event = getEvent.executeQuery();
            event.next();
            return new DiscEvent(event.getString("event_name"), event.getString("event_time"),
                    event.getString("group_name"), event.getString("event_location"),
                    event.getInt("event_id"), getAtttendeeCount(event.getInt("event_id")));
        }

        catch(SQLException ex)
        {
            ex.printStackTrace();
            return null;
        }
    }

    public DiscEvent getSingleEvent(int eventID)
    {
        // Local constants

        // Local variables

        /****** start getSingleEvent() ******/

        try
        {
            PreparedStatement getEvent = con.prepareStatement("SELECT event_name, event_location, event_time," +
                    "event_id, event_time, artist.group_name FROM events INNER JOIN artist ON " +
                    "events.event_artist = artist.artist_id WHERE event_id = ?");
            getEvent.setInt(1, eventID);
            ResultSet event = getEvent.executeQuery();
            event.next();
            return new DiscEvent(event.getString("event_name"), event.getString("event_time"),
                    event.getString("group_name"), event.getString("event_location"),
                    event.getInt("event_id"), getAtttendeeCount(event.getInt("event_id")));
        }

        catch(SQLException ex)
        {
            ex.printStackTrace();
            return null;
        }
    }

    public UserAccount getUserData(String username)
    {
        // Local constants

        // Local variables

        /****** start getUserData() ******/

        try
        {
            ArrayList<URSong> rawSongs =  new ArrayList<>();
            ArrayList<String> songsRated = new ArrayList<>();
            PreparedStatement getUser = con.prepareStatement("SELECT * FROM users " +
                    "WHERE username = ?");
            getUser.setString(1, username);
            ResultSet userData = getUser.executeQuery();
            userData.next();
            PreparedStatement getData = con.prepareStatement("SELECT * FROM song INNER JOIN " +
                    "given_ratings ON song.song_id = given_ratings.song_rated INNER JOIN album ON " +
                    "song.album_bc = album.album_bc INNER JOIN artist ON album.artist_id = " +
                    "artist.artist_id WHERE username = ?");
            getData.setString(1, username);
            ResultSet ratingData = getData.executeQuery();

            while(ratingData.next())
            {
                rawSongs.add(new URSong(ratingData.getString("song_title"),
                        ratingData.getString("album_title"),
                        ratingData.getString("group_name"),
                        ratingData.getInt("rating_given")));
            }

            rawSongs.sort(URSong::compareTo);

            for(int i = 0; i < rawSongs.size(); i++)
            {
                songsRated.add(rawSongs.get(i).getTitle() + " by " + rawSongs.get(i).getArtistName() + ", Rating Given: " +
                    Integer.toString(rawSongs.get(i).getRating()));
            }

            return new UserAccount(userData.getString("firstname"), userData.getString("lastname"),
                    username, songsRated);
        }

        catch(SQLException ex)
        {
            ex.printStackTrace();
            return null;
        }
    }

    public boolean getUserAttending(DiscEvent devent, String username){
        try {
            PreparedStatement getStatus =  con.prepareStatement("SELECT event_attendees.user_attending FROM " +
                    "event_attendees WHERE event_id = ? AND user_username = ?");
            getStatus.setInt(1, devent.getEventID());
            getStatus.setString(2, username);
            ResultSet rtnVal = getStatus.executeQuery();
            return rtnVal.next();

        } catch(SQLException ex){
            ex.printStackTrace();
            return false;
        }
    }

    public boolean hasUserRated(String username, String songID)
    {
        // Local constants

        // Local variables

        /****** start hasUserRated() ******/

        try
        {
            PreparedStatement getRated = con.prepareStatement("SELECT * FROM given_ratings" +
                    " WHERE username = ? AND song_rated = ?");
            getRated.setString(1, username);
            getRated.setString(2, songID);
            ResultSet rRated = getRated.executeQuery();
            return rRated.next();
        }

        catch(SQLException ex)
        {
            ex.printStackTrace();
            return false;
        }
    }

    public int removeSong(String title, String id)
    {
        // Local constants

        // Local variables

        /****** start removeSong() ******/

        try
        {
            PreparedStatement pstate = con.prepareStatement("DELETE FROM song WHERE song.song_title = " +
                    "? AND song.song_id = ? ");
            PreparedStatement rmRatings = con.prepareStatement("DELETE FROM given_ratings WHERE " +
                    "given_ratings.song_rated = ?");
            PreparedStatement getSongs = con.prepareStatement("SELECT song.song_id FROM " +
                    "song WHERE song.song_title = ?");

            if(title.equals(""))
                title = SQL_DEFAULT;

            if(id.equals(""))
                id = SQL_DEFAULT;

            if(title.equals(SQL_DEFAULT) && id.equals(SQL_DEFAULT)) return 2;

            if(id.equals(SQL_DEFAULT)){
                getSongs.setString(1, title);
                ResultSet songers = getSongs.executeQuery();
                songers.next();
                id = songers.getString("song_id");
                rmRatings.setString(1, id);
                rmRatings.executeUpdate();
            } else {
                rmRatings.setString(1, id);
                rmRatings.executeUpdate();
            }

            pstate.setString(1, title);
            pstate.setString(2, id);
            return pstate.executeUpdate();
        }

        catch(SQLException ex)
        {
            ex.printStackTrace();
            return 0;
        }
    }

    public int removeAlbum(String title, String barcode)
    {
        // Local constants

        // Local variables

        /****** start removeAlbum() ******/

        try
        {
            PreparedStatement rmSongs = con.prepareStatement("DELETE FROM song WHERE " +
                    "song.album_bc LIKE ?");
            PreparedStatement rmRatings = con.prepareStatement("DELETE FROM given_ratings WHERE " +
                    "given_ratings.song_rated = ?");
            PreparedStatement getSongs = con.prepareStatement("SELECT song.song_id FROM " +
                    "song WHERE song.album_bc = ?");

            if(barcode.equals(""))
            {
                PreparedStatement getBC = con.prepareStatement("SELECT album.album_bc FROM album WHERE " +
                        "album.album_title LIKE ?");

                if(title.equals(""))
                    return 2;

                getBC.setString(1, title);
                ResultSet bcSet = getBC.executeQuery();
                bcSet.next();
                barcode = bcSet.getString("album_bc");
                if(barcode == null)
                    return 2;
            }
            getSongs.setString(1, barcode);
            ResultSet songs = getSongs.executeQuery();
            while(songs.next()){
                rmRatings.setString(1, songs.getString("song_id"));
                rmRatings.executeUpdate();
            }
            rmSongs.setString(1, barcode);
            rmSongs.executeUpdate();
            PreparedStatement rmAlbum = con.prepareStatement("DELETE FROM album WHERE " +
                    "album.album_bc = ? AND album.album_title = ?");
            rmAlbum.setString(1, barcode);
            rmAlbum.setString(2, title);
            return rmAlbum.executeUpdate();
        }

        catch(SQLException ex)
        {
            ex.printStackTrace();
            return 0;
        }
    }

    public int removeArtist(String name, String artistID)
    {
        // Local constants

        // Local variables

        /****** start removeArtist() ******/

        try
        {
            PreparedStatement rmRatings = con.prepareStatement("DELETE FROM given_ratings WHERE " +
                    "given_ratings.song_rated = ?");
            PreparedStatement rmSongs = con.prepareStatement("DELETE FROM song WHERE " +
                    "song.album_bc = ?");
            PreparedStatement rmAlbum = con.prepareStatement("DELETE FROM album WHERE " +
                    "album.album_bc = ?");
            PreparedStatement rmArtist = con.prepareStatement("DELETE FROM artist WHERE " +
                    "artist.group_name = ? AND artist.artist_id = ?");
            PreparedStatement getAlbums = con.prepareStatement("SELECT album.album_bc FROM " +
                    "album WHERE album.artist_id = ?");
            PreparedStatement getSongs = con.prepareStatement("SELECT song.song_id FROM " +
                    "song WHERE song.album_bc = ?");
            PreparedStatement rmEdition =  con.prepareStatement("DELETE FROM edition WHERE " +
                    "edition.base_barcode = ?");
            PreparedStatement getEvents = con.prepareStatement("SELECT events.event_id FROM " +
                    "events WHERE event_artist = ?");
            PreparedStatement rmEvents =  con.prepareStatement("DELETE FROM events WHERE " +
                    "event_id = ?");
            PreparedStatement rmAttendees = con.prepareStatement("DELETE FROM event_attendees WHERE " +
                    "event_attendees.event_id = ?");
            if(artistID.equals(""))
            {
                PreparedStatement getID = con.prepareStatement("SELECT artist_id FROM artist " +
                        "WHERE artist.group_name = ?");

                if(name.equals(""))
                    return 2;

                getID.setString(1, name);
                ResultSet idSet = getID.executeQuery();
                idSet.next();
                artistID = idSet.getString("artist_id");

                if(artistID == null)
                    return 2;

            }
            getAlbums.setString(1, artistID);
            getEvents.setString(1, artistID);
            ResultSet albBC = getAlbums.executeQuery();
            ResultSet badEvents =  getEvents.executeQuery();
            while(albBC.next())
            {
                getSongs.setString(1, albBC.getString("album_bc"));
                ResultSet sngOnAlb = getSongs.executeQuery();
                while(sngOnAlb.next()){
                    rmRatings.setString(1, sngOnAlb.getString("song_id"));
                    rmRatings.executeUpdate();
                }
                rmSongs.setString(1, albBC.getString("album_bc"));
                rmSongs.executeUpdate();
                rmEdition.setString(1, albBC.getString("album_bc"));
                rmEdition.executeUpdate();
                rmAlbum.setString(1, albBC.getString("album_bc"));
                rmAlbum.executeUpdate();
            }
            while(badEvents.next()){
                rmAttendees.setInt(1, badEvents.getInt("event_id"));
                rmAttendees.executeUpdate();
                rmEvents.setInt(1, badEvents.getInt("event_id"));
                rmEvents.executeUpdate();
            }
            rmArtist.setString(1, name);
            rmArtist.setString(2, artistID);
            return rmArtist.executeUpdate();
        }

        catch(SQLException ex)
        {
            ex.printStackTrace();
            return 0;
        }
    }

    public int removeLabel(String name)
    {
        // Local constants

        // Local variables

        /****** start removeLabel() ******/

        try
        {
            PreparedStatement getArtists = con.prepareStatement("SELECT FROM artist WHERE " +
                    "label_name LIKE ?");
            PreparedStatement setUnsigned = con.prepareStatement("UPDATE artist SET label_name =" +
                    "'UNSIGNED' WHERE label_name LIKE ?");
            PreparedStatement rmLabel = con.prepareStatement("DELETE FROM record_label WHERE " +
                    "label_name LIKE ?");
            if(name.equals(""))
                return 2;

            getArtists.setString(1, name);
            ResultSet artists = getArtists.executeQuery();

            while(artists.next())
            {
                setUnsigned.setString(1, name);
                setUnsigned.executeUpdate();
            }
            rmLabel.setString(1, name);
            return rmLabel.executeUpdate();
        }

        catch(SQLException ex)
        {
            ex.printStackTrace();
            return 0;
        }
    }

    public int removeLocation(String name) {
        try {
            PreparedStatement removeLoc = con.prepareStatement("DELETE FROM locations WHERE location_name" +
                    " = ?");
            PreparedStatement removeDependants = con.prepareStatement("DELETE FROM events WHERE " +
                    "events.event_location = ?");
            removeDependants.setString(1, name);
            removeLoc.setString(1, name);
            removeDependants.executeUpdate();
            return removeLoc.executeUpdate();
        } catch(SQLException ex) {
            ex.printStackTrace();
            return 0;
        }
    }

    public int removeEvent(int ID){
        try {
            PreparedStatement delAttend = con.prepareStatement("DELETE FROM event_attendees WHERE " +
                    "event_attendees.event_id = ?");
            PreparedStatement delEvent = con.prepareStatement("DELETE FROM events WHERE " +
                    "events.event_id = ?");
            delAttend.setInt(1, ID);
            delEvent.setInt(1, ID);
            delAttend.executeUpdate();
            return delEvent.executeUpdate();
        } catch(SQLException ex) {
            ex.printStackTrace();
            return 0;
        }
    }

    public int removeEdition(String edBC){
        try {
            PreparedStatement delEdition = con.prepareStatement("DELETE FROM edition WHERE " +
                    "edition_barcode = ?");
            delEdition.setString(1, edBC);
            return delEdition.executeUpdate();
        } catch(SQLException ex) {
            ex.printStackTrace();
            return 0;
        }
    }

    public void updateAlbumRating(Song song)
    {
        // Local constants

        // Local variables
        double count = 0;
        double sum_ratings = 0;

        /****** start updateAlbumRating() ******/

        try
        {
            PreparedStatement getAlbSong = con.prepareStatement("SELECT * " +
                    "FROM song WHERE song.album_bc = ?");
            getAlbSong.setString(1, song.getAlbum().getBarcode());
            ResultSet albSongs = getAlbSong.executeQuery();

            while(albSongs.next())
            {
                if(albSongs.getInt("song_tratings") != 0)
                {
                    count++;
                    sum_ratings += albSongs.getDouble("song_sratings")
                            / albSongs.getDouble("song_tratings");
                }
            }
            PreparedStatement upAlb = con.prepareStatement("UPDATE album SET album_rating = ? WHERE " +
                    "album_bc = ?");
            if(count == 0)
                count = 1;
            upAlb.setDouble(1, sum_ratings/count);
            upAlb.setString(2, song.getAlbum().getBarcode());
            upAlb.executeUpdate();
        }

        catch(SQLException ex)
        {
            ex.printStackTrace();
        }
    }

    public void updateRating(String username, Song song, int rating)
    {
        // Local constants

        // Local variables

        /****** start updateRating() ******/

        try
        {
            if(rating > 5)
                rating = 5;
            if(rating < 0)
                rating = 0;

            PreparedStatement updateRatings = con.prepareStatement("INSERT INTO given_ratings " +
                    "VALUES (?, ?, ?)");
            updateRatings.setString(1, username);
            updateRatings.setString(2, Integer.toString(song.getID()));
            updateRatings.setInt(3, rating);
            updateRatings.executeUpdate();
            updateRatings = con.prepareStatement("UPDATE song SET song_tratings = song_tratings + 1, " +
                    "song_sratings = song_sratings + ? WHERE song_id = ?");
            updateRatings.setInt(1, rating);
            updateRatings.setString(2, Integer.toString(song.getID()));
            updateRatings.executeUpdate();
            updateAlbumRating(song);
        }

        catch(SQLException ex)
        {
            ex.printStackTrace();
        }
    }

    public void updateFirstname(String username, String firstname)
    {
        // Local constants

        // Local variables

        /****** start updateFirstname() ******/

        try
        {
            PreparedStatement updateFN = con.prepareStatement("UPDATE users SET firstname = " +
                    "? WHERE username = ?");
            updateFN.setString(2, username);
            updateFN.setString(1, firstname);
            updateFN.executeUpdate();
        }

        catch(SQLException ex)
        {
            ex.printStackTrace();
        }
    }

    public void updateLastname(String username, String lastname)
    {
        // Local constants

        // Local variables

        /****** start updateLastname() ******/

        try
        {
            PreparedStatement updateFN = con.prepareStatement("UPDATE users SET lastname = " +
                    "? WHERE username = ?");
            updateFN.setString(2, username);
            updateFN.setString(1, lastname);
            updateFN.executeUpdate();
        }

        catch(SQLException ex)
        {
            ex.printStackTrace();
        }
    }

    public void updateUsername(String username, String nUsername)
    {
        // Local constants

        // Local variables

        /****** start updateUsername() ******/

        try
        {
            PreparedStatement updateUN = con.prepareStatement("UPDATE users SET username = " +
                    "? WHERE username = ?");
            PreparedStatement updateAtt = con.prepareStatement("UPDATE event_attendees SET user_username " +
                    "= 'UPDATING' WHERE user_username = ?");
            PreparedStatement fixAtt = con.prepareStatement("UPDATE event_attendees SET user_username " +
                    "= ? WHERE user_username = 'UPDATING'");
            PreparedStatement updateRat = con.prepareStatement("UPDATE given_ratings SET username " +
                    "= 'UPDATING' WHERE username = ?");
            PreparedStatement fixRat = con.prepareStatement("UPDATE given_ratings SET username " +
                    "= ? WHERE username = 'UPDATING'");
            updateAtt.setString(1, username);
            updateRat.setString(1, username);
            updateAtt.executeUpdate();
            updateRat.executeUpdate();
            updateUN.setString(2, username);
            updateUN.setString(1, nUsername);
            updateUN.executeUpdate();
            fixAtt.setString(1, nUsername);
            fixRat.setString(1, nUsername);
            fixAtt.executeUpdate();
            fixRat.executeUpdate();
        }

        catch(SQLException ex)
        {
            ex.printStackTrace();
        }
    }
}
