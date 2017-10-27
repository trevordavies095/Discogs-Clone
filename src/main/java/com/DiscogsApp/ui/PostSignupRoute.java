package com.DiscogsApp.ui;

import com.DiscogsApp.appl.SQLManager;
import spark.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class PostSignupRoute implements Route {

    private final String UNK_ERROR = "Unknown error occurred while adding new user to database.";

    private final String NAME_TAKEN = "The username you input is already in use. Please choose another.";

    private final SQLManager sqlManager;

    private final TemplateEngine templateEngine;

    PostSignupRoute(TemplateEngine templateEngine, SQLManager sqlManager){
        Objects.requireNonNull(templateEngine, "templateEngine must not be null");
        Objects.requireNonNull(sqlManager, "sqlManager must not be null");

        this.sqlManager = sqlManager;

        this.templateEngine = templateEngine;
    }

    public String handle(Request request, Response response){
        final Session httpSession = request.session();

        final Map<String, Object> vm = new HashMap<>();

        String username = request.queryParams("username");
        String password = request.queryParams("password");
        String firstname = request.queryParams("firstname");
        String lastname = request.queryParams("lastname");
        int status = sqlManager.addUser(username, password, firstname, lastname);
        if(status == 0){
            httpSession.attribute(FTLKeys.USER, username);
            httpSession.attribute(FTLKeys.SIGNED_IN, true);
            vm.put(FTLKeys.TITLE, FTLKeys.WELCOME);
            vm.put(FTLKeys.USER, httpSession.attribute(FTLKeys.USER));
            vm.put(FTLKeys.SIGNED_IN, httpSession.attribute(FTLKeys.SIGNED_IN));
            return templateEngine.render(new ModelAndView(vm, FTLKeys.HOME_VIEW));
        }else if(status == 1){
            vm.put(FTLKeys.MSG_TYPE, FTLKeys.MSG_TYPE_ERR);
            vm.put(FTLKeys.MESSAGE, NAME_TAKEN);
        }else if(status == 2){
            vm.put(FTLKeys.MSG_TYPE, FTLKeys.MSG_TYPE_ERR);
            vm.put(FTLKeys.MESSAGE, UNK_ERROR);
        }
        return templateEngine.render(new ModelAndView(vm, FTLKeys.SIGNUP_VIEW));
    }
}
