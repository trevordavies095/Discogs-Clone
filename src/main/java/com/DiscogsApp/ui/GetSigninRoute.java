package com.DiscogsApp.ui;

import spark.*;


import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class GetSigninRoute implements Route{

    private final TemplateEngine templateEngine;

    GetSigninRoute(TemplateEngine templateEngine){
        Objects.requireNonNull(templateEngine, "templateEngine must not be null");

        this.templateEngine = templateEngine;
    }

    public String handle(Request request, Response response){
        final Session httpSession = request.session();

        final Map<String, Object> vm = new HashMap<>();
        vm.put(FTLKeys.TITLE, "Sign In");

        return templateEngine.render(new ModelAndView(vm, FTLKeys.SIGNIN_VIEW));
    }
}
