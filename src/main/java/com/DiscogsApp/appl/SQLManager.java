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

    private ArrayList<String> stringifyEvents(ResultSet events){
        String curr;
        String currTS;
        ArrayList<String> results = new ArrayList<>();
        try {
            while (events.next()) {
                curr = events.getString("group_name") + " playing at " +
                        events.getString("event_location") + " on ";
                currTS = DiscEvent.parseTimestamp(events.getString("event_time"));
                curr = curr + currTS + " (ID: " + events.getInt("event_id") + ")";
                results.add(curr);
            }
            return results;
        } catch(SQLException ex) {
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

        if(outerShell < 1){
            return null;
        }
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
                            rset.getInt("album_sratings"), rset.getInt("album_tratings"), cArtist);
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
                if(goodInsert) {
                    return 1;
                } else {
                    return 2;
                }
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
        ResultSet pertinent;
        PreparedStatement getAll;
        int outerShell = 0;
        if(label.equals("")) label = SQL_DEFAULT; else label = "%"+label+"%";
        if(artist.equals("")) artist = SQL_DEFAULT; else artist = "%"+artist+"%";
        if(album.equals("")) album = SQL_DEFAULT; else album = "%"+album+"%";
        if(song.equals("")) song = SQL_DEFAULT; else song = "%"+song+"%";
        try{
            if(!(song.equals(SQL_DEFAULT))){
                getAll = con.prepareStatement("SELECT * FROM song NATURAL JOIN album " +
                        "NATURAL JOIN artist NATURAL JOIN record_label WHERE song_title LIKE ?" +
                        "AND album_title LIKE ? AND group_name LIKE ? AND label_name LIKE ?");
                getAll.setString(1, song);
                getAll.setString(2, album);
                getAll.setString(3, artist);
                getAll.setString(4, label);
                outerShell = 4;
            }else if(!(album.equals(SQL_DEFAULT))){
                getAll = con.prepareStatement("SELECT * FROM album NATURAL JOIN artist " +
                        "NATURAL JOIN record_label WHERE album_title LIKE ? AND group_name LIKE ? " +
                        "AND label_name LIKE ?");
                getAll.setString(1, album);
                getAll.setString(2, artist);
                getAll.setString(3, label);
                outerShell = 3;
            }else if(!(artist.equals(SQL_DEFAULT))){
                getAll = con.prepareStatement("SELECT * FROM artist NATURAL JOIN record_label " +
                        "WHERE group_name LIKE ? AND label_name LIKE ?");
                getAll.setString(1, artist);
                getAll.setString(2, label);
                outerShell = 2;
            }else if(!(label.equals(SQL_DEFAULT))){
                getAll = con.prepareStatement("SELECT * FROM record_label WHERE label_name " +
                        "LIKE ?");
                getAll.setString(1, label);
                outerShell = 1;
            } else {
                return null;
            }
            pertinent = getAll.executeQuery();
            return parseResults(pertinent, outerShell);
        } catch(SQLException ex){
            ex.printStackTrace();
            return null;
        }
    }

    public int addSong(String title, String length, String genre, boolean explicit,
                       String release, String albumBC){
        try {
            PreparedStatement mkID = con.prepareStatement("SELECT COUNT (*) FROM song");
            ResultSet rset = mkID.executeQuery();
            rset.next();
            int songID = 1102 + rset.getInt("count");
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
        } catch(SQLException ex) {
            ex.printStackTrace();
            return 0;
        }
    }

    public int addArtist(String title, String realName, String labelName,
                         String debutYear){
        try {
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
        } catch(SQLException ex){
            ex.printStackTrace();
            return 0;
        }
    }

    public int addAlbum(String barcode, String style, String genre, String title,
                        String artistID){
        try {
            PreparedStatement pstate = con.prepareStatement("INSERT INTO album VALUES(?, ?, ?, ?, ?, ?, ?)");
            pstate.setString(1, barcode);
            pstate.setString(2, style);
            pstate.setString(3, genre);
            pstate.setInt(4,0);
            pstate.setInt(5,0);
            pstate.setString(6, title);
            pstate.setInt(7, Integer.parseInt(artistID));
            return pstate.executeUpdate();
        } catch(SQLException ex){
            ex.printStackTrace();
            return 0;
        }
    }

    public int addLabel(String name, String formYear, String netWorth){
        try {
            PreparedStatement pstate = con.prepareStatement("INSERT INTO record_label VALUES(?, ?, ?)");
            pstate.setString(1, name);
            pstate.setInt(2, Integer.parseInt(formYear));
            pstate.setDouble(3, Double.parseDouble(netWorth));
            return pstate.executeUpdate();
        } catch(SQLException ex){
            ex.printStackTrace();
            return 0;
        }
    }

    public ArrayList<String> getAlbumSongs(Album album){
        try {
            ArrayList<String> results = new ArrayList<>();
            PreparedStatement pstate = con.prepareStatement("SELECT * FROM song WHERE song.album_bc " +
                    "LIKE ?");
            pstate.setString(1, album.getBarcode());
            ResultSet rset = pstate.executeQuery();
            while(rset.next()){
                results.add(rset.getString("song_title"));
            }
            return results;
        } catch(SQLException ex) {
            ex.printStackTrace();
            return null;
        }
    }

    public ArrayList<String> getArtistAlbums(Artist artist){
        try {
            ArrayList<String> results = new ArrayList<>();
            PreparedStatement pstate = con.prepareStatement("SELECT * FROM album WHERE album.artist_id " +
                    "LIKE ?");
            pstate.setString(1, Integer.toString(artist.getArtistID()));
            ResultSet rset = pstate.executeQuery();
            while(rset.next()){
                results.add(rset.getString("album_title"));
            }
            return results;
        } catch(SQLException ex) {
            ex.printStackTrace();
            return null;
        }
    }

    public ArrayList<String> getEvents(){
        try {
           ArrayList<String> results = new ArrayList<>();
           PreparedStatement getem = con.prepareStatement("SELECT event_time," +
                   " event_location, event_id, artist.group_name FROM events INNER JOIN " +
                   "artist ON events.event_artist = artist.artist_id");
           ResultSet events = getem.executeQuery();
           return stringifyEvents(events);
        } catch(SQLException ex){
            ex.printStackTrace();
            return null;
        }
    }

    public ArrayList<String> getEvents(int artistID){
        try {
            ArrayList<String> results = new ArrayList<>();
            PreparedStatement getem = con.prepareStatement("SELECT event_time," +
                    " event_location, event_id, artist.group_name FROM events INNER JOIN " +
                    "artist ON events.event_artist = artist.artist_id WHERE artist_id = ?");
            getem.setString(1, Integer.toString(artistID));
            ResultSet events = getem.executeQuery();
            return stringifyEvents(events);
        } catch(SQLException ex){
            ex.printStackTrace();
            return null;
        }
    }

    public ArrayList<String> getLabelArtists(Label label){
        try {
            ArrayList<String> results = new ArrayList<>();
            PreparedStatement pstate = con.prepareStatement("SELECT * FROM artist WHERE artist.label_name " +
                    "LIKE ?");
            pstate.setString(1, label.getName());
            ResultSet rset = pstate.executeQuery();
            while(rset.next()){
                results.add(rset.getString("group_name"));
            }
            return results;
        } catch(SQLException ex) {
            ex.printStackTrace();
            return null;
        }
    }

    public DiscEvent getSingleEvent(String eventString){
        try {
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
                    event.getInt("event_id"));
        } catch(SQLException ex) {
            ex.printStackTrace();
            return null;
        }
    }

    public UserAccount getUserData(String username){
        try {
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
            while(ratingData.next()){
                rawSongs.add(new URSong(ratingData.getString("song_title"),
                        ratingData.getString("album_title"),
                        ratingData.getString("group_name"),
                        ratingData.getInt("rating_given")));
            }
            rawSongs.sort(URSong::compareTo);
            for(int i = 0; i < rawSongs.size(); i++){
                songsRated.add(rawSongs.get(i).getTitle() + " by " + rawSongs.get(i).getArtistName() + ", Rating Given: " +
                    Integer.toString(rawSongs.get(i).getRating()));
            }
            return new UserAccount(userData.getString("firstname"), userData.getString("lastname"),
                    username, songsRated);
        } catch(SQLException ex) {
            ex.printStackTrace();
            return null;
        }
    }

    public boolean hasUserRated(String username, String songID){
        try {
            PreparedStatement getRated = con.prepareStatement("SELECT * FROM given_ratings" +
                    " WHERE username = ? AND song_rated = ?");
            getRated.setString(1, username);
            getRated.setString(2, songID);
            ResultSet rRated = getRated.executeQuery();
            return rRated.next();
        } catch(SQLException ex) {
            ex.printStackTrace();
            return false;
        }
    }

    public int removeSong(String title, String id){
        try {
            PreparedStatement pstate = con.prepareStatement("DELETE FROM song WHERE song.song_title LIKE " +
                    "? AND song.song_id LIKE ? ");
            if(title.equals("")){
                title = SQL_DEFAULT;
            }
            if(id.equals("")){
                id = SQL_DEFAULT;
            }
            pstate.setString(1, title);
            pstate.setString(2, id);
            return pstate.executeUpdate();
        } catch(SQLException ex){
            ex.printStackTrace();
            return 0;
        }
    }

    public int removeAlbum(String title, String barcode){
        try {
            PreparedStatement rmSongs = con.prepareStatement("DELETE FROM song WHERE " +
                    "song.album_bc LIKE ?");
            if(barcode.equals("")){
                PreparedStatement getBC = con.prepareStatement("SELECT album.album_bc FROM album WHERE " +
                        "album.album_title LIKE ?");
                if(title.equals("")){
                    return 2;
                }
                getBC.setString(1, title);
                ResultSet bcSet = getBC.executeQuery();
                bcSet.next();
                barcode = bcSet.getString("barcode");
                if(barcode == null){
                    return 2;
                }
            }
            rmSongs.setString(1, barcode);
            rmSongs.executeUpdate();
            PreparedStatement rmAlbum = con.prepareStatement("DELETE FROM album WHERE " +
                    "album.album_bc LIKE ? AND album.album_title LIKE ?");
            rmAlbum.setString(1, barcode);
            rmAlbum.setString(2, title);
            return rmAlbum.executeUpdate();
        } catch(SQLException ex){
            ex.printStackTrace();
            return 0;
        }
    }

    public int removeArtist(String name, String artistID){
        try {
            PreparedStatement rmSongs = con.prepareStatement("DELETE FROM song WHERE " +
                    "song.album_bc LIKE ?");
            PreparedStatement rmAlbum = con.prepareStatement("DELETE FROM album WHERE " +
                    "album.album_bc LIKE ?");
            PreparedStatement rmArtist = con.prepareStatement("DELETE FROM artist WHERE " +
                    "artist.group_name LIKE ? AND artist.artist_id LIKE ?");
            PreparedStatement getAlbums = con.prepareStatement("SELECT album.album_bc FROM " +
                    "album WHERE album.artist_id LIKE ?");
            if(artistID.equals("")){
                PreparedStatement getID = con.prepareStatement("SELECT artist_id FROM artist " +
                        "WHERE artist.group_name LIKE ?");
                if(name.equals("")){
                    return 2;
                }
                getID.setString(1, name);
                ResultSet idSet = getID.executeQuery();
                idSet.next();
                artistID = idSet.getString("artist_id");
                if(artistID == null){
                    return 2;
                }
            }
            getAlbums.setString(1, artistID);
            ResultSet albBC = getAlbums.executeQuery();
            while(albBC.next()){
                rmSongs.setString(1, albBC.getString("barcode"));
                rmSongs.executeUpdate();
                rmAlbum.setString(1, albBC.getString("barcode"));
                rmAlbum.executeUpdate();
            }
            rmArtist.setString(1, name);
            rmArtist.setString(2, artistID);
            return rmArtist.executeUpdate();
        } catch(SQLException ex){
            ex.printStackTrace();
            return 0;
        }
    }

    public int removeLabel(String name){
        try{
            PreparedStatement getArtists = con.prepareStatement("SELECT FROM artist WHERE " +
                    "label_name LIKE ?");
            PreparedStatement setUnsigned = con.prepareStatement("UPDATE artist SET label_name =" +
                    "'UNSIGNED' WHERE label_name LIKE ?");
            PreparedStatement rmLabel = con.prepareStatement("DELETE FROM record_label WHERE " +
                    "label_name LIKE ?");
            if(name.equals("")){
                return 2;
            }
            getArtists.setString(1, name);
            ResultSet artists = getArtists.executeQuery();
            while(artists.next()){
                setUnsigned.setString(1, name);
                setUnsigned.executeUpdate();
            }
            rmLabel.setString(1, name);
            return rmLabel.executeUpdate();
        } catch(SQLException ex){
            ex.printStackTrace();
            return 0;
        }
    }

    public void updateRating(String username, String songID, int rating){
        try {
            if(rating > 5) rating = 5;
            if(rating < 0) rating = 0;
            PreparedStatement updateRatings = con.prepareStatement("INSERT INTO given_ratings " +
                    "VALUES (?, ?, ?)");
            updateRatings.setString(1, username);
            updateRatings.setString(2, songID);
            updateRatings.setInt(3, rating);
            updateRatings.executeUpdate();
            updateRatings = con.prepareStatement("UPDATE song SET song_tratings = song_tratings + 1, " +
                    "song_sratings = song_sratings + ? WHERE song_id = ?");
            updateRatings.setInt(1, rating);
            updateRatings.setString(2, songID);
            updateRatings.executeUpdate();
        } catch(SQLException ex) {
            ex.printStackTrace();
        }
    }

    public void updateFirstname(String username, String firstname){
        try {
            PreparedStatement updateFN = con.prepareStatement("UPDATE users SET firstname = " +
                    "? WHERE username = ?");
            updateFN.setString(2, username);
            updateFN.setString(1, firstname);
            updateFN.executeUpdate();
        } catch(SQLException ex) {
            ex.printStackTrace();
        }
    }

    public void updateLastname(String username, String lastname){
        try {
            PreparedStatement updateFN = con.prepareStatement("UPDATE users SET lastname = " +
                    "? WHERE username = ?");
            updateFN.setString(2, username);
            updateFN.setString(1, lastname);
            updateFN.executeUpdate();
        } catch(SQLException ex) {
            ex.printStackTrace();
        }
    }

    public void updateUsername(String username, String nUsername){
        try {
            PreparedStatement updateFN = con.prepareStatement("UPDATE users SET username = " +
                    "? WHERE username = ?");
            updateFN.setString(2, username);
            updateFN.setString(1, nUsername);
            updateFN.executeUpdate();
        } catch(SQLException ex) {
            ex.printStackTrace();
        }
    }
}
