package com.DiscogsApp;

import java.io.InputStream;
import java.util.Objects;
import java.util.logging.LogManager;
import java.util.logging.Logger;

import com.DiscogsApp.appl.SQLManager;
import com.DiscogsApp.appl.SearchCache;
import spark.TemplateEngine;
import spark.template.freemarker.FreeMarkerEngine;

import com.DiscogsApp.ui.WebServer;

/**
 *
 *
 */
public final class Application
{
    // Class constants

    // Class variables
    private static final Logger LOG = Logger.getLogger(Application.class.getName());
    private final WebServer webServer;

    private Application(final WebServer webServer)
    {
        // Constructor constants

        // Constructor variables

        /****** start Application() ******/
        Objects.requireNonNull(webServer, "webServer must not be null");
        this.webServer = webServer;
    }

    private void initialize()
    {
        // Local constants

        // Local variables

        /****** start initialize() ******/
        LOG.config("Application is initializing.");
        webServer.initialize();
        LOG.config("Application initialization complete.");
    }

    /**
     * Entry point for the DisClones web application.
     * @param args: Command line arguments; none expected.
     */
    public static void main(String[] args)
    {
        // Local constants

        // Local variables
        final TemplateEngine templateEngine = new FreeMarkerEngine();
        final SQLManager sqlManager = new SQLManager();
        final SearchCache searchCache = new SearchCache();
        final WebServer webServer = new WebServer(templateEngine, sqlManager, searchCache);
        final Application app = new Application(webServer);

        /****** start main() ******/

        /*try {
            ClassLoader classLoader = Application.class.getClassLoader();
            final InputStream logConfig = classLoader.getResourceAsStream("log.properties");
            LogManager.getLogManager().readConfiguration(logConfig);
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("Could not initialize log manager because: " + e.getMessage());
        }*/

        app.initialize();
    }
}
