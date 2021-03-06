package com.DiscogsApp.ui;

import com.DiscogsApp.appl.SQLManager;
import com.DiscogsApp.appl.SearchCache;
import com.DiscogsApp.model.UserAccount;
import spark.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class PostMyAccountRoute implements Route
{
    // Class constants
    private final TemplateEngine templateEngine;
    private final SQLManager sqlManager;
    private final SearchCache searchCache;

    // Class variables

    public PostMyAccountRoute(TemplateEngine templateEngine, SQLManager sqlManager, SearchCache searchCache)
    {
        // Local constants

        // Local variables

        /****** start PostSearchRoute() ******/

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
        String password;
        boolean valid;
        UserAccount usr;

        /****** start handle() ******/

        if(httpSession.isNew())
        {
            response.redirect(Routes.HOME_URL);
            return null;
        }

        password = request.queryParams(FTLKeys.PASS);
        valid = sqlManager.validatePassword(httpSession.attribute(FTLKeys.USER), password);

        if(valid)
        {
            vm.put(FTLKeys.SIGNED_IN, httpSession.attribute(FTLKeys.SIGNED_IN));
            vm.put(FTLKeys.USER, httpSession.attribute(FTLKeys.USER));
            vm.put(FTLKeys.ADMIN, httpSession.attribute(FTLKeys.ADMIN));
            vm.put("revalidated", true);
            usr = sqlManager.getUserData(httpSession.attribute(FTLKeys.USER));
            vm.put("firstname", usr.getFirstname());
            vm.put("lastname", usr.getLastname());
            vm.put("username", usr.getUsername());

            if(usr.getSongsRated().size() >= 5)
                vm.put("songs", new ArrayList<>(usr.getSongsRated().subList(0,5)));
            else
                vm.put("songs", new ArrayList<>(usr.getSongsRated()));

            vm.put("songsRated", usr.getSongsRated().size());
            vm.put("editmode", false);
        }

        else
            {
            response.redirect(Routes.SIGNOUT_URL);
            return null;
        }

        return templateEngine.render(new ModelAndView(vm, FTLKeys.ACCOUNT_VIEW));
    }
}
