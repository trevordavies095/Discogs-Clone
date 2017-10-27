package com.DiscogsApp.ui;

import static spark.Spark.get;
import static spark.Spark.post;
import static spark.SparkBase.staticFileLocation;

import java.util.Objects;
import java.util.logging.Logger;

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

    /**
    * The constructor for the Web Server.
    *
    * @param templateEngine
    *    The TemplateEngine for rendering HTML responses.
    */
    public WebServer(
        final TemplateEngine templateEngine) {
        Objects.requireNonNull(templateEngine, "templateEngine must not be null");

        this.templateEngine = templateEngine;
    }


    /**
    * Initialize all of the HTTP routes that make up this web application.
    */
    public void initialize() {
        staticFileLocation("/public");

        // Shows Home page.
        get(FTLKeys.HOME_URL, new GetHomeRoute(templateEngine));
        get(FTLKeys.SIGNIN_URL, new GetSigninRoute(templateEngine));
        post(FTLKeys.SIGNIN_URL, new PostSigninRoute(templateEngine));
        //post(FTLKeys.SIGNUP_URL, new GetSignupRoute(templateEngine));

        LOG.config("WebServer is initialized.");
    }

}
