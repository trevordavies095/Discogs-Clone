package com.DiscogsApp.ui;

import com.DiscogsApp.appl.SQLManager;
import spark.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class GetAdminRoute implements Route {

    // Class constants
    private final TemplateEngine templateEngine;
    private final SQLManager sqlManager;
    private final String NOT_ADMIN_MSG = "Warning: User Not Classified as Administrator. Page Access Denied.";
    private final String ADMIN_MSG = "Welcome, Administrator: ";

    // Class variables

    public GetAdminRoute(TemplateEngine templateEngine, SQLManager sqlManager){

        Objects.requireNonNull(templateEngine, "templateEngine must not be null");
        Objects.requireNonNull(sqlManager, "sqlManager must not be null");
        this.sqlManager = sqlManager;
        this.templateEngine = templateEngine;
    }

    public String handle(Request request, Response response){

        final Session httpSession = request.session();
        final Map<String, Object> vm = new HashMap<>();

        vm.put(FTLKeys.SIGNED_IN, httpSession.attribute(FTLKeys.SIGNED_IN));
        vm.put(FTLKeys.USER, httpSession.attribute(FTLKeys.USER));

        if(!(boolean)(httpSession.attribute(FTLKeys.ADMIN))){
            vm.put(FTLKeys.ADMIN, false);
            vm.put(FTLKeys.MESSAGE, NOT_ADMIN_MSG);
            return templateEngine.render(new ModelAndView(vm, FTLKeys.ADMIN_VIEW));
        }

        vm.put(FTLKeys.ADMIN, true);
        vm.put(FTLKeys.MESSAGE, ADMIN_MSG + httpSession.attribute(FTLKeys.USER));
        return  templateEngine.render(new ModelAndView(vm, FTLKeys.ADMIN_VIEW));
    }
}
