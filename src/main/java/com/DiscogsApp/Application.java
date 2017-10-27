package com.DiscogsApp;

import java.io.InputStream;
import java.util.Objects;
import java.util.logging.LogManager;
import java.util.logging.Logger;

import com.DiscogsApp.appl.SQLManager;
import spark.TemplateEngine;
import spark.template.freemarker.FreeMarkerEngine;

import com.DiscogsApp.ui.WebServer;

/**
 *
 *
 */
public final class Application {
    private static final Logger LOG = Logger.getLogger(Application.class.getName());

    /**
    * Entry point for the DisClones web application.
    * @param args: Command line arguments; none expected.
    */
    public static void main(String[] args) {

        /*try {
            ClassLoader classLoader = Application.class.getClassLoader();
            final InputStream logConfig = classLoader.getResourceAsStream("log.properties");
            LogManager.getLogManager().readConfiguration(logConfig);
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("Could not initialize log manager because: " + e.getMessage());
        }*/


        final TemplateEngine templateEngine = new FreeMarkerEngine();

        final SQLManager sqlManager = new SQLManager();

        final WebServer webServer = new WebServer(templateEngine, sqlManager);

        final Application app = new Application(webServer);


        app.initialize();
    }

    private final WebServer webServer;

    private Application(final WebServer webServer) {

          Objects.requireNonNull(webServer, "webServer must not be null");

          this.webServer = webServer;
    }

    private void initialize() {
        LOG.config("Application is initializing.");


        webServer.initialize();



        LOG.config("Application initialization complete.");
    }

}
