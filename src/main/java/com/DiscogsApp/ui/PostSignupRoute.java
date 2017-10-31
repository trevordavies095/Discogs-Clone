package com.DiscogsApp.ui;

import com.DiscogsApp.appl.SQLManager;
import org.apache.commons.lang3.ObjectUtils;
import spark.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class PostSignupRoute implements Route {
    // Class constants
    private final String UNK_ERROR = "Unknown error occurred while adding new user to database.";
    private final String NAME_TAKEN = "The username you input is already in use. Please choose another.";
    private final int MAX_USR = 20;
    private final int MAX_PASS = 63;
    private final SQLManager sqlManager;
    private final TemplateEngine templateEngine;

    // Class variables

    private String invalidMessage(String attr){
        return "Your entry for " + attr + " is invalid.";
    }


    PostSignupRoute(TemplateEngine templateEngine, SQLManager sqlManager) {
        // Local constants

        // Local variables

        /****** start PostSignupRoute() ******/
        Objects.requireNonNull(templateEngine, "templateEngine must not be null");
        Objects.requireNonNull(sqlManager, "sqlManager must not be null");
        this.sqlManager = sqlManager;
        this.templateEngine = templateEngine;
    }

    private String tooLongMessage(String attr, int max) {
        // Local constants

        // Local variables

        /****** start tooLongMessage() ******/

        return "Your entry for " + attr + " is too long. Limit entry in field " +
                attr + " to " + max + " characters.";
    }

    public String handle(Request request, Response response) {
        // Local constants
        final Session httpSession = request.session();
        final Map<String, Object> vm = new HashMap<>();

        // Local variables
        String username = request.queryParams("username");
        String password = request.queryParams("password");
        String firstname = request.queryParams("firstname");
        String lastname = request.queryParams("lastname");
        int status;

        /****** start handle() ******/

        if(username.length() > 20) {
            vm.put(FTLKeys.MSG_TYPE, FTLKeys.MSG_TYPE_ERR);
            vm.put(FTLKeys.MESSAGE, tooLongMessage("username", MAX_USR));

            return templateEngine.render(new ModelAndView(vm, FTLKeys.SIGNUP_VIEW));
        }else if(firstname != null && firstname.length() > 20) {
            vm.put(FTLKeys.MSG_TYPE, FTLKeys.MSG_TYPE_ERR);
            vm.put(FTLKeys.MESSAGE, tooLongMessage("firstname", MAX_USR));

            return templateEngine.render(new ModelAndView(vm, FTLKeys.SIGNUP_VIEW));
        }else if(lastname != null && lastname.length() > 20) {
            vm.put(FTLKeys.MSG_TYPE, FTLKeys.MSG_TYPE_ERR);
            vm.put(FTLKeys.MESSAGE, tooLongMessage("lastname", MAX_USR));

            return templateEngine.render(new ModelAndView(vm, FTLKeys.SIGNUP_VIEW));
        }else if(password.length() > 63) {
            vm.put(FTLKeys.MSG_TYPE, FTLKeys.MSG_TYPE_ERR);
            vm.put(FTLKeys.MESSAGE, tooLongMessage("password", MAX_PASS));

            return templateEngine.render(new ModelAndView(vm, FTLKeys.SIGNUP_VIEW));
        }else if(password.equals("") || password.contains("\"")){
            vm.put(FTLKeys.MSG_TYPE, FTLKeys.MSG_TYPE_ERR);
            vm.put(FTLKeys.MESSAGE, invalidMessage("password"));
            return templateEngine.render(new ModelAndView(vm, FTLKeys.SIGNUP_VIEW));
        }else if(username.equals("") || password.contains("\"")){
            vm.put(FTLKeys.MSG_TYPE, FTLKeys.MSG_TYPE_ERR);
            vm.put(FTLKeys.MESSAGE, invalidMessage("username"));
            return templateEngine.render(new ModelAndView(vm, FTLKeys.SIGNUP_VIEW));
        }

        status = sqlManager.addUser(username, password, firstname, lastname);

        if(status == 0) {
            httpSession.attribute(FTLKeys.USER, username);
            httpSession.attribute(FTLKeys.SIGNED_IN, true);

            vm.put(FTLKeys.TITLE, FTLKeys.WELCOME);
            vm.put(FTLKeys.USER, httpSession.attribute(FTLKeys.USER));
            vm.put(FTLKeys.SIGNED_IN, httpSession.attribute(FTLKeys.SIGNED_IN));

            return templateEngine.render(new ModelAndView(vm, FTLKeys.HOME_VIEW));
        }else if(status == 1) {
            vm.put(FTLKeys.MSG_TYPE, FTLKeys.MSG_TYPE_ERR);
            vm.put(FTLKeys.MESSAGE, NAME_TAKEN);
        }else if(status == 2) {
            vm.put(FTLKeys.MSG_TYPE, FTLKeys.MSG_TYPE_ERR);
            vm.put(FTLKeys.MESSAGE, UNK_ERROR);
        }

        return templateEngine.render(new ModelAndView(vm, FTLKeys.SIGNUP_VIEW));
    }
}
