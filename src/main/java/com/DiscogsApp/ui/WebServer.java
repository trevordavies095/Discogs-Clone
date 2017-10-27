package com.DiscogsApp.ui;

import static spark.Spark.get;
import static spark.Spark.post;
import static spark.SparkBase.staticFileLocation;

import java.util.Objects;
import java.util.logging.Logger;

import com.DiscogsApp.appl.SQLManager;
import spark.TemplateEngine;


/**
 * The server that initializes the set of HTTP request handlers.
 *
 * @author Patrick Ehrenreich (pxe1833@rit.edu)
 * @since October 21, 2017
 */
public class WebServer {
    private static final Logger LOG = Logger.getLogger(WebServer.class.getName());


    private final TemplateEngine templateEngine;

    private final SQLManager sqlManager;

    /**
     * The constructor for the Web Server.
     *
     * @param templateEngine: The TemplateEngine for rendering HTML responses.
     *
     * @param sqlManager: the SQLManager for the application instance
     */
    public WebServer(final TemplateEngine templateEngine, final SQLManager sqlManager) {
        Objects.requireNonNull(templateEngine, "templateEngine must not be null");

        this.templateEngine = templateEngine;

        this.sqlManager = sqlManager;
    }


    /**
    * Initialize all of the HTTP routes that make up this web application.
    */
    public void initialize() {
        staticFileLocation("/public");

        // Shows Home page.
        get(FTLKeys.HOME_URL, new GetHomeRoute(templateEngine, sqlManager));
        get(FTLKeys.SIGNIN_URL, new GetSigninRoute(templateEngine));
        post(FTLKeys.SIGNIN_URL, new PostSigninRoute(templateEngine, sqlManager));
        get(FTLKeys.SIGNUP_URL, new GetSignupRoute(templateEngine, sqlManager));
        post(FTLKeys.SIGNUP_URL, new PostSignupRoute(templateEngine, sqlManager));
        get(FTLKeys.SIGNOUT_URL, new GetSignoutRoute(templateEngine, sqlManager));

        LOG.config("WebServer is initialized.");
    }

}
