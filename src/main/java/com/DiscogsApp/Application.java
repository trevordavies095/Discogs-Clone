package com.DiscogsApp;

import java.util.Objects;

import com.DiscogsApp.appl.SQLManager;
import com.DiscogsApp.appl.SearchCache;
import spark.TemplateEngine;
import spark.template.freemarker.FreeMarkerEngine;

import com.DiscogsApp.ui.WebServer;

public final class Application
{
    // Class constants

    // Class variables
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
        webServer.initialize();
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

        app.initialize();
    }
}
