package com.DiscogsApp.appl;


import java.sql.*;
import java.util.ArrayList;


public class SQLManager
{
    // Class constants
    static private final String dburl = "jdbc:postgresql://reddwarf.cs.rit.edu:5432/p32004e";
    static private final String dbusername = "p32004e";
    static private final String dbpassword = "ievip6se0pha1sahchuD";

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

    public boolean validateAdministrator(String username){
        try {
            Statement stmt = con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,
                    ResultSet.CONCUR_READ_ONLY);
            String qry = "SELECT administrator FROM users WHERE users.username = '" + username + "'";
            ResultSet rset = stmt.executeQuery(qry);
            rset.next();
            return rset.getBoolean("administrator");
        }catch(SQLException ex){
            ex.printStackTrace();
            return false;
        }
    }

    public int addUser(String username, String password, String firstname, String lastname)
    {
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

    public ArrayList<String> parseSearch(String song, String artist, String album, String label){
        try{
            Statement stmt = con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,
                    ResultSet.CONCUR_READ_ONLY);
            ArrayList<SearchField> include = new ArrayList<>();
            String qry = "";
            int sections = 0;
            if(!(song.equals(" ") || song.equals(""))){
                include.add(new SearchField(SearchEnum.SONG, song));
                sections++;
            }
            if(!(album.equals(" ") || album.equals(""))){
                include.add(new SearchField(SearchEnum.ALBUM, album));
                sections++;
            }
            if(!(artist.equals(" ") || artist.equals(""))){
                include.add(new SearchField(SearchEnum.ARTIST, artist));
                sections++;
            }
            if(!(label.equals(" ") || label.equals(""))){
                include.add(new SearchField(SearchEnum.LABEL, label));
                sections++;
            }
            if(sections == 0){
                return null;
            }else if(sections == 1){
                qry = "SELECT * FROM " + include.get(0).getType().getTable() +
                        " WHERE " + include.get(0).getType().getAttr() + " LIKE '%" + include.get(0).getValue() + "%'";
            }
            ResultSet rset = stmt.executeQuery(qry);
            return null;
        }catch(SQLException ex){
            ex.printStackTrace();
            return null;
        }
    }
}
