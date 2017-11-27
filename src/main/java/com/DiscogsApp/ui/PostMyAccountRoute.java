package com.DiscogsApp.ui;

import com.DiscogsApp.appl.SQLManager;
import com.DiscogsApp.appl.SearchCache;
import com.DiscogsApp.model.UserAccount;
import spark.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class PostMyAccountRoute implements Route {

    private final TemplateEngine templateEngine;

    private final SQLManager sqlManager;

    private final SearchCache searchCache;

    public PostMyAccountRoute(TemplateEngine templateEngine, SQLManager sqlManager, SearchCache searchCache) {
        // Local constants

        // Local variables

        /****** start PostSearchRoute() ******/

        Objects.requireNonNull(templateEngine, "templateEngine must not be null");
        this.templateEngine = templateEngine;
        this.sqlManager = sqlManager;
        this.searchCache = searchCache;
    }

    public String handle(Request request, Response response){
        // Local constants
        final Session httpSession = request.session();
        final Map<String, Object> vm = new HashMap<>();

        // Local variables

        /****** start handle() ******/

        String password = request.queryParams(FTLKeys.PASS);
        boolean valid = sqlManager.validatePassword(httpSession.attribute(FTLKeys.USER), password);
        if(valid){
            vm.put(FTLKeys.SIGNED_IN, httpSession.attribute(FTLKeys.SIGNED_IN));
            vm.put(FTLKeys.USER, httpSession.attribute(FTLKeys.USER));
            vm.put(FTLKeys.ADMIN, httpSession.attribute(FTLKeys.ADMIN));
            vm.put("revalidated", true);
            UserAccount usr = sqlManager.getUserData(httpSession.attribute(FTLKeys.USER));
            vm.put("firstname", usr.getFirstname());
            vm.put("lastname", usr.getLastname());
            vm.put("username", usr.getUsername());
            vm.put("songs", new ArrayList<>(usr.getSongsRated().subList(0,5)));
            vm.put("songsRated", usr.getSongsRated().size());
        } else {
            response.redirect(Routes.SIGNOUT_URL);
            return null;
        }

        return templateEngine.render(new ModelAndView(vm, FTLKeys.ACCOUNT_VIEW));
    }
}
