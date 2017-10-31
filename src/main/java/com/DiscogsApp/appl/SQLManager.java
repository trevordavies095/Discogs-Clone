package com.DiscogsApp.appl;

import java.sql.*;

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

    /*public ResultSet getUserData(String username){
        try{
            Statement stmt = con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,
                    ResultSet.CONCUR_READ_ONLY);

        }catch(SQLException ex){
            ex.printStackTrace();
        }
    }*/
}
