package com.DiscogsApp.ui;

import static spark.Spark.get;
import static spark.Spark.post;
import static spark.SparkBase.staticFileLocation;

import java.util.Objects;

import com.DiscogsApp.appl.SQLManager;
import spark.TemplateEngine;


/**
 * The server that initializes the set of HTTP request handlers.
 *
 * @author Patrick Ehrenreich (pxe1833@rit.edu)
 * @since October 21, 2017
 */
public class WebServer
{
    // Class constants
    private final TemplateEngine templateEngine;
    private final SQLManager sqlManager;

    // Class variables


    /**
     * The constructor for the Web Server.
     *
     * @param templateEngine: The TemplateEngine for rendering HTML responses.
     *
     * @param sqlManager: the SQLManager for the application instance
     */
    public WebServer(final TemplateEngine templateEngine, final SQLManager sqlManager)
    {
        // Local constants

        // Local variables

        /****** start WebServer() ******/

        Objects.requireNonNull(templateEngine, "templateEngine must not be null");
        this.templateEngine = templateEngine;
        this.sqlManager = sqlManager;
    }

    /**
    * Initialize all of the HTTP routes that make up this web application.
    */
    public void initialize()
    {
        // Local constants

        // Local variables

        /****** start initialize() ******/

        staticFileLocation("/public");

        get(Routes.HOME_URL, new GetHomeRoute(templateEngine, sqlManager));
        get(Routes.SIGNIN_URL, new GetSigninRoute(templateEngine));
        post(Routes.SIGNIN_URL, new PostSigninRoute(templateEngine, sqlManager));
        get(Routes.SIGNUP_URL, new GetSignupRoute(templateEngine, sqlManager));
        post(Routes.SIGNUP_URL, new PostSignupRoute(templateEngine, sqlManager));
        get(Routes.SIGNOUT_URL, new GetSignoutRoute(templateEngine, sqlManager));
        get(Routes.SEARCH_URL, new GetSearchRoute(templateEngine, sqlManager));
        post(Routes.SEARCH_URL, new PostSearchRoute(templateEngine, sqlManager));

    }
}
