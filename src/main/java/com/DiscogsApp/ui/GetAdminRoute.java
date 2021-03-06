package com.DiscogsApp.ui;

import com.DiscogsApp.appl.SQLManager;
import com.DiscogsApp.appl.SearchCache;
import spark.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class GetAdminRoute implements Route {

    // Class constants
    private final TemplateEngine templateEngine;
    private final SQLManager sqlManager;
    private final SearchCache searchCache;
    private final String NOT_ADMIN_MSG = "Warning: User Not Classified as Administrator.\n Page Access Denied.";
    private final String ADMIN_MSG = "Welcome, Administrator: ";

    // Class variables

    public GetAdminRoute(TemplateEngine templateEngine, SQLManager sqlManager, SearchCache searchCache)
    {
        // Local constants

        // Local variables

        /****** start GetAdminRoute() ******/

        Objects.requireNonNull(templateEngine, "templateEngine must not be null");
        Objects.requireNonNull(sqlManager, "SQLManager must not be null");
        Objects.requireNonNull(searchCache, "searchCache must not be null");
        this.sqlManager = sqlManager;
        this.templateEngine = templateEngine;
        this.searchCache = searchCache;
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

        vm.put(FTLKeys.SIGNED_IN, httpSession.attribute(FTLKeys.SIGNED_IN));
        vm.put(FTLKeys.USER, httpSession.attribute(FTLKeys.USER));

        if(!(boolean)(httpSession.attribute(FTLKeys.ADMIN)))
        {
            vm.put(FTLKeys.ADMIN, false);
            vm.put(FTLKeys.MESSAGE, NOT_ADMIN_MSG);
            return templateEngine.render(new ModelAndView(vm, FTLKeys.ADMIN_VIEW));
        }

        vm.put(FTLKeys.TOOLS, true);
        vm.put(FTLKeys.ADMIN, httpSession.attribute(FTLKeys.ADMIN));
        vm.put(FTLKeys.MESSAGE, ADMIN_MSG + httpSession.attribute(FTLKeys.USER));
        vm.put(FTLKeys.SPECIFIC, false);
        return  templateEngine.render(new ModelAndView(vm, FTLKeys.ADMIN_VIEW));
    }
}
