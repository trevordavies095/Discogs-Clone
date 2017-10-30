package com.DiscogsApp.ui;

import com.DiscogsApp.appl.SQLManager;
import spark.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class PostSearchRoute implements Route {

    private final TemplateEngine templateEngine;

    private final SQLManager sqlManager;

    PostSearchRoute(TemplateEngine templateEngine, SQLManager sqlManager){
        Objects.requireNonNull(templateEngine, "templateEngine must not be null");

        this.templateEngine = templateEngine;

        this.sqlManager = sqlManager;
    }

    public String handle(Request request, Response response){
        final Session httpSession = request.session();

        final Map<String, Object> vm = new HashMap<>();
        String song = request.queryParams("song");
        String artist =  request.queryParams("artist");
        String album = request.queryParams("artist");
        String label =  request.queryParams("label");

        return templateEngine.render(new ModelAndView(vm, FTLKeys.SEARCH_VIEW));
    }
}
