package com.DiscogsApp.ui;

import com.DiscogsApp.appl.SQLManager;
import spark.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class GetSignupRoute implements Route
{
    // Class constants
    private final SQLManager sqlManager;
    private final TemplateEngine templateEngine;

    // Class variables


    GetSignupRoute(TemplateEngine templateEngine, SQLManager sqlManager)
    {
        // Local constants

        // Local variables

        /****** start GetSignupRoute ******/

        Objects.requireNonNull(templateEngine, "templateEngine must not be null");
        Objects.requireNonNull(sqlManager, "sqlManager must not be null");
        this.sqlManager = sqlManager;
        this.templateEngine = templateEngine;
    }

    public String handle(Request request, Response response)
    {
        // Local constants
        final Session httpSession = request.session();
        final Map<String, Object> vm = new HashMap<>();

        // Local variables

        /****** start handle() ******/

        if(httpSession.isNew())
        {
            response.redirect(Routes.HOME_URL);
            return null;
        }

        vm.put(FTLKeys.TITLE, "Create Account");
        return templateEngine.render(new ModelAndView(vm, FTLKeys.SIGNUP_VIEW));
    }
}
