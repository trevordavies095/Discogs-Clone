package com.DiscogsApp.appl;

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

    public int addUser(String username, String password, String firstname, String lastname){
        try {
            boolean nameTaken = validateUsername(username);
            if(!(nameTaken)) {
                Statement stmt = con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,
                        ResultSet.CONCUR_READ_ONLY);
                String qry = "INSERT INTO users VALUES ('"+username+"', '"+password+"'," +
                        " '"+firstname+"', '"+lastname+"')";
                stmt.executeUpdate(qry);
                boolean goodInsert = validateUsername(username);
                if(goodInsert) {
                    return 0;
                }else{
                    return 2;
                }
            }else{
                return 1;
            }
        }catch(SQLException ex){
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
