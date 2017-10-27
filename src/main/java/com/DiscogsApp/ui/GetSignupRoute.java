package com.DiscogsApp.ui;

import com.DiscogsApp.appl.SQLManager;
import spark.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class GetSignupRoute implements Route {

    private final SQLManager sqlManager;

    private final TemplateEngine templateEngine;

    GetSignupRoute(TemplateEngine templateEngine, SQLManager sqlManager){
        Objects.requireNonNull(templateEngine, "templateEngine must not be null");
        Objects.requireNonNull(sqlManager, "sqlManager must not be null");

        this.sqlManager = sqlManager;

        this.templateEngine = templateEngine;
    }

    public String handle(Request request, Response response){
        final Session httpSession = request.session();

        final Map<String, Object> vm = new HashMap<>();
        vm.put(FTLKeys.TITLE, "Create Account");

        return templateEngine.render(new ModelAndView(vm, FTLKeys.SIGNUP_VIEW));
    }
}
