package com.DiscogsApp.ui;

import static spark.Spark.halt;
import static spark.SparkBase.secure;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import com.DiscogsApp.appl.SQLManager;
import com.DiscogsApp.ui.WebServer;


import spark.*;

/**
 * The {@code GET /} route handler; aka the Home page.
 * This is the page where the user starts (no Game yet)
 * but is also the landing page after a game ends.
 *
 * @author Patrick Ehrenreich (pxe1833@rit.edu)
 * @since October 21st, 2017
 */
public class GetHomeRoute implements Route
{
    // Class constants
    private final TemplateEngine templateEngine;
    private final SQLManager sqlManager;

    // Class variables


    /**
     * The constructor for the GET '/' route handler.
     *
     * @param templateEngine: The TemplateEngine for rendering HTML responses.
     * @throws NullPointerException
     *    when the templateEngine parameter or sqlManager parameter is null
     */
    GetHomeRoute(final TemplateEngine templateEngine, final SQLManager sqlManager)
    {
        // Local constants

        // Local variables

        /******start GetHomeRoute() ******/

        Objects.requireNonNull(templateEngine, "templateEngine must not be null");
        this.templateEngine = templateEngine;
        this.sqlManager = sqlManager;
    }

    /**
    * {@inheritDoc}
    */
    @Override
    public String handle(Request request, Response response)
    {
        // Local constants
        final Session httpSession = request.session();
        final Map<String, Object> vm = new HashMap<>();

        // Local variables

        /****** start handle() ******/

        if(httpSession.isNew())
        {
            httpSession.attribute(FTLKeys.USER, "Guest");
            httpSession.attribute(FTLKeys.SIGNED_IN, false);
        }

        vm.put(FTLKeys.TITLE, FTLKeys.WELCOME);
        vm.put(FTLKeys.USER, httpSession.attribute(FTLKeys.USER));
        vm.put(FTLKeys.SIGNED_IN, httpSession.attribute(FTLKeys.SIGNED_IN));

        return templateEngine.render(new ModelAndView(vm, FTLKeys.HOME_VIEW));
    }
}
