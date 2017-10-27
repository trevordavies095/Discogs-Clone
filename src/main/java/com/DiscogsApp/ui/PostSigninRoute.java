package com.DiscogsApp.ui;

import com.DiscogsApp.appl.SQLManager;
import spark.*;
import java.sql.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class PostSigninRoute implements Route {

    private final TemplateEngine templateEngine;

    private final SQLManager sqlManager;

    private static final String WRONG_PASSWORD = "Incorrect password for username: ";

    private static String makeBadUsernameMessage(String username){
        return "Username " + username + " does not exist.";
    }

    PostSigninRoute(TemplateEngine templateEngine, SQLManager sqlManager){
        Objects.requireNonNull(templateEngine, "templateEngine must not be null");

        this.templateEngine = templateEngine;

        this.sqlManager = sqlManager;
    }

    public String handle(Request request, Response response){
        final Session httpSession = request.session();

        final Map<String, Object> vm = new HashMap<>();

        String username = request.queryParams(FTLKeys.USER);
        String password = request.queryParams(FTLKeys.PASS);
        try(
                Connection con = DriverManager.getConnection(
                        "jdbc:postgresql://reddwarf.cs.rit.edu:5432/p32004e", "p32004e",
                        "ievip6se0pha1sahchuD");
                Statement stmt = con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,
                        ResultSet.CONCUR_READ_ONLY);
        ) {
            boolean userExists = sqlManager.validateUsername(username);
            if(userExists) {
                boolean goodPass = sqlManager.validatePassword(username, password);
                if (goodPass) {
                    httpSession.attribute(FTLKeys.USER, username);
                    httpSession.attribute(FTLKeys.SIGNED_IN, true);
                    response.redirect(FTLKeys.HOME_URL);
                    return null;
                } else {
                    vm.put(FTLKeys.MSG_TYPE, FTLKeys.MSG_TYPE_ERR);
                    vm.put(FTLKeys.MESSAGE, WRONG_PASSWORD + username);
                    return templateEngine.render(new ModelAndView(vm, FTLKeys.SIGNIN_VIEW));
                }
            }else{
                vm.put(FTLKeys.MSG_TYPE, FTLKeys.MSG_TYPE_ERR);
                vm.put(FTLKeys.MESSAGE, makeBadUsernameMessage(username));
                return templateEngine.render(new ModelAndView(vm, FTLKeys.SIGNIN_VIEW));
            }
        }catch(SQLException ex){
            ex.printStackTrace();
        }
        vm.put(FTLKeys.TITLE, "Welcome");
        return templateEngine.render(new ModelAndView(vm, FTLKeys.HOME_VIEW));
    }
}
