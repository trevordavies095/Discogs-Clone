package com.DiscogsApp.ui;

import com.DiscogsApp.appl.SQLManager;
import com.DiscogsApp.appl.SearchCache;
import spark.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class PostUpdateAccountRoute implements Route
{
    // Class constants
    private final TemplateEngine templateEngine;
    private final SQLManager sqlManager;
    private final SearchCache searchCache;

    // Class variables

    public PostUpdateAccountRoute(TemplateEngine templateEngine,
                                 SQLManager sqlManager, SearchCache searchCache)
    {
        // Local constants

        // Local variables

        /****** start PostUpdateAccountRoute() ******/

        Objects.requireNonNull(templateEngine, "templateEngine must not be null");
        Objects.requireNonNull(sqlManager, "SQLManager must not be null");
        Objects.requireNonNull(searchCache, "searchCache must not be null");
        this.searchCache = searchCache;
        this.sqlManager = sqlManager;
        this.templateEngine = templateEngine;
    }

    public String handle(Request request, Response response)
    {
        // Local constants
        final Session httpSession = request.session();
        final Map<String, Object> vm = new HashMap<>();

        // Local variables
        String newUsername = request.queryParams("updateUName");
        String newFirstname =  request.queryParams("updateFName");
        String newLastname = request.queryParams("updateLName");

        /****** start handle() ******/

        if(httpSession.isNew()){
            response.redirect(Routes.HOME_URL);
            return null;
        }

        vm.put(FTLKeys.SIGNED_IN, httpSession.attribute(FTLKeys.SIGNED_IN));
        vm.put(FTLKeys.USER, httpSession.attribute(FTLKeys.USER));
        vm.put(FTLKeys.ADMIN, httpSession.attribute(FTLKeys.ADMIN));

        if(!newFirstname.equals(""))
            sqlManager.updateFirstname(
                httpSession.attribute(FTLKeys.USER), newFirstname);
        if(!newLastname.equals(""))
            sqlManager.updateLastname(
                httpSession.attribute(FTLKeys.USER), newLastname);
        if(!newUsername.equals("")){
            sqlManager.updateUsername(httpSession.attribute(FTLKeys.USER), newUsername);
            httpSession.attribute(FTLKeys.USER, newUsername);
        }

        response.redirect(Routes.ACCOUNT_URL);
        return null;
    }
}
