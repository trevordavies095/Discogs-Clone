package com.DiscogsApp.ui;

import com.DiscogsApp.appl.SQLManager;
import com.DiscogsApp.appl.SearchCache;
import com.DiscogsApp.model.DiscEvent;
import spark.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class GetEventsRoute implements Route {

    private final TemplateEngine templateEngine;

    private final SQLManager sqlManager;

    private final SearchCache searchCache;

    public GetEventsRoute(TemplateEngine templateEngine, SQLManager sqlManager, SearchCache searchCache) {
        // Local constants

        // Local variables

        /****** start PostSearchRoute() ******/
        Objects.requireNonNull(templateEngine, "templateEngine must not be null");
        Objects.requireNonNull(sqlManager, "SQLManager must not be null");
        Objects.requireNonNull(searchCache, "searchCache must not be null");
        this.templateEngine = templateEngine;
        this.sqlManager = sqlManager;
        this.searchCache = searchCache;
    }

    public String handle(Request request, Response response){
        // Local constants
        final Session httpSession = request.session();
        final Map<String, Object> vm = new HashMap<>();

        // Local variables

        if(httpSession.isNew()){
            response.redirect(Routes.HOME_URL);
            return null;
        }

        /****** start handle() ******/
        vm.put(FTLKeys.SIGNED_IN, httpSession.attribute(FTLKeys.SIGNED_IN));
        vm.put(FTLKeys.USER, httpSession.attribute(FTLKeys.USER));
        vm.put(FTLKeys.ADMIN, httpSession.attribute(FTLKeys.ADMIN));
        String cEvent = request.queryParams("chosen");
        DiscEvent devent = sqlManager.getSingleEvent(cEvent);
        vm.put("eventLoc", devent.getEventLocation());
        vm.put("eventTime", devent.getEventTime());
        vm.put("eventName", devent.getEventName());
        vm.put("eventArtist", devent.getEventArtist());
        vm.put("eventID", devent.getEventID());

        return templateEngine.render(new ModelAndView(vm, FTLKeys.EVENTS_VIEW));
    }
}
