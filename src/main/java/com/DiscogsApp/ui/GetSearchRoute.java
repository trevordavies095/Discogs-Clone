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

        return templateEngine.render(new ModelAndView(vm, FTLKeys.SEARCH_VIEW));
    }
}
