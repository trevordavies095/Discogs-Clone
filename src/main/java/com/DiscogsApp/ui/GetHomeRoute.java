package com.DiscogsApp.ui;

import static spark.Spark.halt;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import com.DiscogsApp.ui.WebServer;


import spark.*;

/**
 * The {@code GET /} route handler; aka the Home page.
 * This is the page where the user starts (no Game yet)
 * but is also the landing page after a game ends.
 *
 * @author <a href='mailto:bdbvse@rit.edu'>Bryan Basham</a>
 * @author <a href='mailto:jrv@se.rit.edu'>Jim Vallino</a>
 */
public class GetHomeRoute implements Route {

    // Values used in the view-model map for rendering the home view.
    static final String TITLE_ATTR = "title";
    static final String GAME_STATS_MSG_ATTR = "gameStatsMessage";
    static final String PLYR_STATS_MSG_ATTR = "userStatsMessage";
    static final String NEW_PLAYER_ATTR = "newPlayer";
    private static final String TITLE = "Welcome to the Guessing Game";
    static final String VIEW_NAME = "home.ftl";

    // Key in the session attribute map for the player who started the session
    private final TemplateEngine templateEngine;


    /**
     * The constructor for the {@code POST /guess} route handler.
     *
     * @param templateEngine
     *    The {@link TemplateEngine} for the application to use when rendering HTML responses.
     *
     * @throws NullPointerException
     *    when the {@code gameCenter} or {@code templateEngine} parameter is null
     */
    GetHomeRoute(final TemplateEngine templateEngine) {
        Objects.requireNonNull(templateEngine, "templateEngine must not be null");

        this.templateEngine = templateEngine;
    }

  /**
   * {@inheritDoc}
   */
  @Override
  public String handle(Request request, Response response) {
      final Session httpSession = request.session();

      final Map<String, Object> vm = new HashMap<>();

      return templateEngine.render(new ModelAndView(vm, FTLKeys.HOME_VIEW));
  }
}
