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

    // Private Methods
    private String buildQuery(ArrayList<SearchField> params, SearchEnum qryType)
    {
        // Local constants

        // Local variables
        SearchField q1 = params.get(0);
        SearchField q2 = params.get(1);
        SearchField q3 = params.get(2);
        SearchField q4 = params.get(3);

        /****** start buildQuery() ******/

        if(qryType.getTable().equals(SearchEnum.SONG.getTable()))
        {
            return "SELECT * FROM " + q1.getTable() + " WHERE " + q1.getFkey() +
                    " IN (SELECT " + q2.getPkey() + " FROM " + q2.getTable() +
                    " WHERE " + q2.getFkey() + " IN (SELECT " + q3.getPkey() +
                    " FROM " + q3.getTable() + " WHERE " + q3.getFkey() +
                    " IN (SELECT " + q4.getPkey() + " FROM " + q4.getTable() +
                    " WHERE " + q4.getfrName() + " LIKE '" + q4.getValue() +
                    "') AND " + q3.getfrName() + " LIKE '" + q3.getValue() +
                    "') AND " + q2.getfrName() + " LIKE '" + q2.getValue() +
                    "') AND " + q1.getfrName() + " LIKE '" + q1.getValue() + "'";
        }

        else if(qryType.getTable().equals(SearchEnum.ALBUM.getTable()))
        {
            return "SELECT * FROM " + q2.getTable() + " WHERE " + q2.getFkey() +
                    " IN (SELECT " + q3.getPkey() + " FROM " + q3.getTable() +
                    " WHERE " + q3.getFkey() + " IN (SELECT " + q4.getPkey() +
                    " FROM " + q4.getTable() + " WHERE " + q4.getfrName() +
                    " LIKE '" + q4.getValue() + "') AND " + q3.getfrName() +
                    " LIKE '" + q3.getValue() + "') AND " + q2.getfrName() +
                    " LIKE '" + q2.getValue() + "'";
        }

        else if(qryType.getTable().equals(SearchEnum.ARTIST.getTable()))
        {
            return "SELECT * FROM " + q3.getTable() + " WHERE " + q3.getFkey() +
                    " IN (SELECT " + q4.getPkey() + " FROM " + q4.getTable() +
                    " WHERE " + q4.getfrName() + " LIKE '" + q4.getValue() + "') AND " +
                    q3.getfrName() + " LIKE '" + q3.getValue() + "'";
        }

        else if(qryType.getTable().equals(SearchEnum.LABEL.getTable()))
        {
            return "SELECT * FROM " + q4.getTable() + " WHERE " + q4.getfrName() +
                    " LIKE '" + q4.getValue() + "'";
        }

        else
            return null;
    }

    private ArrayList<SearchObject> parseResults(ResultSet rset, SearchEnum qryType)
    {
        // Local constants

        // Local variables
        ArrayList<SearchObject> rtn = new ArrayList<>();

        /****** start parseResults() ******/

        try
        {
            while (rset.next())
            {
                if (qryType.getTable().equals(SearchEnum.SONG.getTable()))
                {
                    rtn.add(new Song(rset.getInt("sum_ratings"), rset.getInt("tot_ratings"),
                            rset.getInt("release_year"), rset.getBoolean("explicit"),
                            rset.getString("title"), rset.getString("length"),
                            rset.getString("genre"), rset.getString("album_bc")));
                }

                else if (qryType.getTable().equals(SearchEnum.ALBUM.getTable()))
                {
                    rtn.add(new Album(rset.getString("barcode"), rset.getString("style"),
                            rset.getString("genre"), rset.getString("title"),
                            rset.getInt("sum_rating"), rset.getInt("tot_rating")));
                }

                else if (qryType.getTable().equals(SearchEnum.ARTIST.getTable()))
                {
                    rtn.add(new Artist(rset.getInt("artist_id"), rset.getInt("debut_year"),
                            rset.getString("name"), rset.getString("real_name"),
                            rset.getString("label_name")));
                }

                else if (qryType.getTable().equals(SearchEnum.LABEL.getTable()))
                {
                    rtn.add(new Label(rset.getString("name"), rset.getInt("formation_year"),
                            rset.getDouble("net_worth")));
                }
            }
        }

        catch (SQLException ex)
        {
            ex.printStackTrace();
            return rtn;
        }

        return rtn;
    }

    public SQLManager() {
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

    public boolean validateUsername(String username)
    {
        // Local constants

        // Local variables
        Statement stmt;
        String qry;
        ResultSet rset;

        /****** start validateUsername() ******/

        try
        {
            stmt = con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,
                    ResultSet.CONCUR_READ_ONLY);
            qry = "SELECT username FROM users WHERE users.username = '" + username + "'";
            rset = stmt.executeQuery(qry);

            return rset.first();
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
        Statement stmt;
        String qry;
        ResultSet rset;

        /****** start validatePassword() ******/

        try
        {
            stmt = con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,
                    ResultSet.CONCUR_READ_ONLY);
            qry = "SELECT password FROM users WHERE users.username = '" + username + "'";
            rset = stmt.executeQuery(qry);
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
        Statement stmt;
        String qry;
        ResultSet rset;

        /****** start validateAdministrator() ******/

        try
        {
            stmt = con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,
                    ResultSet.CONCUR_READ_ONLY);
            qry = "SELECT administrator FROM users WHERE users.username = '" + username + "'";
            rset = stmt.executeQuery(qry);
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
        Statement stmt;
        String qry;

        /****** start addUser() ******/

        try
        {
            nameTaken = validateUsername(username);

            if(!(nameTaken))
            {
                stmt = con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,
                        ResultSet.CONCUR_READ_ONLY);
                qry = "INSERT INTO users VALUES ('"+username+"', '"+password+"'," +
                        " '"+firstname+"', '"+lastname+"')";
                stmt.executeUpdate(qry);
                goodInsert = validateUsername(username);

                if(goodInsert)
                    return 0;
                else
                    return 2;
            }

            else
                return 1;
        }

        catch(SQLException ex)
        {
            ex.printStackTrace();
            return 2;
        }
    }

    /*
    public ResultSet getUserData(String username){
        try{
            Statement stmt = con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,
                    ResultSet.CONCUR_READ_ONLY);



        }catch(SQLException ex){
            ex.printStackTrace();
            return null;
        }
    }*/

    public HashMap<SearchEnum, ArrayList<SearchObject>>
    parseSearch(String song, String album, String artist, String label)
    {
        // Local constants

        // Local variables
        Statement stmt;
        ArrayList<SearchField> params;
        ArrayList<SearchObject> rtnarray;
        HashMap<SearchEnum, ArrayList<SearchObject>> rtn;
        String qry;
        SearchEnum qryType = null;
        int sections = 0;
        ResultSet rset;

        /****** start parseSearch() ******/

        try
        {
            stmt = con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,
                    ResultSet.CONCUR_READ_ONLY);
            params = new ArrayList<>();

            if(!(song.equals(" ") || song.equals("")))
            {
                params.add(new SearchField(SearchEnum.SONG, song));
                sections++;
                qryType = SearchEnum.SONG;
            }

            else
                params.add(null);

            if(!(album.equals(" ") || album.equals("")))
            {
                params.add(new SearchField(SearchEnum.ALBUM, album));
                sections++;

                if(qryType == null)
                    qryType = SearchEnum.ALBUM;

            }

            else if(qryType != null)
                params.add(new SearchField(SearchEnum.ALBUM, SQL_DEFAULT));

            else
                params.add(null);

            if(!(artist.equals(" ") || artist.equals("")))
            {
                params.add(new SearchField(SearchEnum.ARTIST, artist));
                sections++;

                if(qryType == null)
                    qryType = SearchEnum.ARTIST;
            }

            else if(qryType != null)
                params.add(new SearchField(SearchEnum.ARTIST, SQL_DEFAULT));

            else
                params.add(null);

            if(!(label.equals(" ") || label.equals("")))
            {
                params.add(new SearchField(SearchEnum.LABEL, label));
                sections++;

                if(qryType == null)
                    qryType = SearchEnum.LABEL;

            }

            else if(qryType != null)
                params.add(new SearchField(SearchEnum.LABEL, SQL_DEFAULT));

            if(sections == 0)
                return null;

            qry = buildQuery(params, qryType);

            if(qry == null)
                return null;

            System.out.println(qry);
            rset = stmt.executeQuery(qry);
            rtnarray = parseResults(rset, qryType);
            if(rtnarray == null)
                return null;
            rtn = new HashMap<>();
            rtn.put(qryType, rtnarray);
            return rtn;

        }

        catch(SQLException ex)
        {
            ex.printStackTrace();
            return null;
        }
    }
}
