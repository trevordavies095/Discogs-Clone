package com.DiscogsApp.ui;

import com.DiscogsApp.appl.SQLManager;
import spark.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class GetSignoutRoute implements Route
{
    // Class constants
    private final TemplateEngine templateEngine;
    private final SQLManager sqlManager;

    // Class variables


    GetSignoutRoute(final TemplateEngine templateEngine, final SQLManager sqlManager)
    {
        // Local constants

        // Local variables

        /****** start GetSignoutRoute() ******/

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

        httpSession.attribute(FTLKeys.SIGNED_IN, false);
        vm.put(FTLKeys.TITLE, FTLKeys.WELCOME);
        vm.put(FTLKeys.USER, "Guest");
        vm.put(FTLKeys.SIGNED_IN, httpSession.attribute(FTLKeys.SIGNED_IN));
        return templateEngine.render(new ModelAndView(vm, FTLKeys.HOME_VIEW));
    }
}
