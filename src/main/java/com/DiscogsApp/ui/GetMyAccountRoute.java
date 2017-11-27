package com.DiscogsApp.ui;

import com.DiscogsApp.appl.SQLManager;
import com.DiscogsApp.appl.SearchCache;
import spark.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class GetMyAccountRoute implements Route {

    private final TemplateEngine templateEngine;

    private final SQLManager sqlManager;

    private final SearchCache searchCache;

    public GetMyAccountRoute(TemplateEngine templateEngine, SQLManager sqlManager, SearchCache searchCache) {
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

        vm.put(FTLKeys.SIGNED_IN, httpSession.attribute(FTLKeys.SIGNED_IN));
        vm.put(FTLKeys.USER, httpSession.attribute(FTLKeys.USER));
        vm.put(FTLKeys.ADMIN, httpSession.attribute(FTLKeys.ADMIN));
        vm.put("revalidated", false);
        vm.put("attempted", false);

        return templateEngine.render(new ModelAndView(vm, FTLKeys.ACCOUNT_VIEW));
    }
}
