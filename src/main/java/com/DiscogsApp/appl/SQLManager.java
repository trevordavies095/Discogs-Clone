package com.DiscogsApp.appl;

import com.DiscogsApp.ui.FTLKeys;
import spark.ModelAndView;

import java.sql.*;

public class SQLManager {

    static private final String dburl = "jdbc:postgresql://reddwarf.cs.rit.edu:5432/p32004e";

    static private final String dbusername = "p32004e";

    static private final String dbpassword = "ievip6se0pha1sahchuD";

    private Connection con;

    public SQLManager(){
        try{
            this.con = DriverManager.getConnection(dburl, dbusername,dbpassword);
        }catch(SQLException ex){
            ex.printStackTrace();
        }

    }

    public boolean validateUsername(String username){
        try {
            Statement stmt = con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,
                    ResultSet.CONCUR_READ_ONLY);
            String qry = "SELECT username FROM users WHERE users.username = '" + username + "'";
            ResultSet rset = stmt.executeQuery(qry);
            return rset.first();
        }catch(SQLException ex){
            ex.printStackTrace();
            return false;
        }
    }

    public boolean validatePassword(String username, String password){
        try {
            Statement stmt = con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,
                    ResultSet.CONCUR_READ_ONLY);
            String qry = "SELECT password FROM users WHERE users.username = '" + username + "'";
            ResultSet rset = stmt.executeQuery(qry);
            rset.next();
            return rset.getString("password").equals(password);
        }catch(SQLException ex){
            ex.printStackTrace();
            return false;
        }
    }
}
