package com.DiscogsApp.ui;

import com.DiscogsApp.appl.SQLManager;
import spark.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class GetSearchRoute implements Route
{
    // Class constants
    private final TemplateEngine templateEngine;
    private final SQLManager sqlManager;

    // Class variables

    GetSearchRoute(TemplateEngine templateEngine, SQLManager sqlManager)
    {
        // Local constants

        // Local variables

        /****** start GetSearchRoute() ******/

        Objects.requireNonNull(templateEngine, "templateEngine must not be null");
        Objects.requireNonNull(sqlManager, "SQLManager must not be null");
        this.templateEngine = templateEngine;
        this.sqlManager = sqlManager;
    }

    public String handle(Request request, Response response)
    {
        // Local constants
        final Session httpSession = request.session();
        final Map<String, Object> vm = new HashMap<>();

        // Local variables


        /****** start handle() ******/

        if(httpSession.isNew())
        {
            response.redirect(Routes.HOME_URL);
            return null;
        }

        vm.put(FTLKeys.PRESEARCH, true);
        vm.put(FTLKeys.POSTSEARCH, false);
        vm.put(FTLKeys.SIGNED_IN, httpSession.attribute(FTLKeys.SIGNED_IN));
        vm.put(FTLKeys.USER, httpSession.attribute(FTLKeys.USER));
        vm.put(FTLKeys.ADMIN, httpSession.attribute(FTLKeys.ADMIN));

        return templateEngine.render(new ModelAndView(vm, FTLKeys.SEARCH_VIEW));
    }
}
