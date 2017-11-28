package com.DiscogsApp.ui;

import com.DiscogsApp.appl.SQLManager;
import com.DiscogsApp.appl.SearchCache;
import spark.*;
import java.sql.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class PostSigninRoute implements Route
{
    // Class constants
    private final TemplateEngine templateEngine;
    private final SQLManager sqlManager;
    private final SearchCache searchCache;
    private static final String WRONG_PASSWORD = "Incorrect password for username: ";

    // Class variables


    private static String makeBadUsernameMessage(String username)
    {
        // Local constants

        // Local variables

        /****** start makeBadUsernameMessage() ******/

        return "Username " + username + " does not exist.";
    }

    PostSigninRoute(final TemplateEngine templateEngine, final SQLManager sqlManager, final SearchCache searchCache)
    {
        // Local constants

        // Local variables

        /****** start PostSigninRoute() ******/
        Objects.requireNonNull(templateEngine, "templateEngine must not be null");
        Objects.requireNonNull(sqlManager, "SQLManager must not be null");
        Objects.requireNonNull(searchCache, "searchCache must not be null");
        this.templateEngine = templateEngine;
        this.sqlManager = sqlManager;
        this.searchCache = searchCache;
    }

    public String handle(Request request, Response response)
    {
        // Local constants
        final Session httpSession = request.session();
        final Map<String, Object> vm = new HashMap<>();

        // Local variables
        String username = request.queryParams(FTLKeys.USER);
        String password = request.queryParams(FTLKeys.PASS);
        boolean userExists = sqlManager.validateUsername(username);
        boolean goodPass;

        if(httpSession.isNew()){
            response.redirect(Routes.HOME_URL);
            return null;
        }

        /****** start handle() ******/

        if(userExists)
        {
            goodPass = sqlManager.validatePassword(username, password);

            if (goodPass)
            {
                httpSession.attribute(FTLKeys.USER, username);
                httpSession.attribute(FTLKeys.SIGNED_IN, true);
                searchCache.addUser(username);
                httpSession.attribute(FTLKeys.ADMIN, sqlManager.validateAdministrator(username));
                response.redirect(Routes.HOME_URL);
                return null;
            }

            else
            {
                vm.put(FTLKeys.MSG_TYPE, FTLKeys.MSG_TYPE_ERR);
                vm.put(FTLKeys.MESSAGE, WRONG_PASSWORD + username);
                return templateEngine.render(new ModelAndView(vm, FTLKeys.SIGNIN_VIEW));
            }
        }

        else
        {
            vm.put(FTLKeys.MSG_TYPE, FTLKeys.MSG_TYPE_ERR);
            vm.put(FTLKeys.MESSAGE, makeBadUsernameMessage(username));
            return templateEngine.render(new ModelAndView(vm, FTLKeys.SIGNIN_VIEW));
        }
    }
}
