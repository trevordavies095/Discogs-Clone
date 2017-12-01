package com.DiscogsApp.ui;

import com.DiscogsApp.appl.SQLManager;
import com.DiscogsApp.appl.SearchCache;
import com.DiscogsApp.model.DiscEvent;
import spark.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class GetEventsRoute implements Route
{
    // Class constants
    private final TemplateEngine templateEngine;
    private final SQLManager sqlManager;
    private final SearchCache searchCache;

    // Class variables

    public GetEventsRoute(TemplateEngine templateEngine, SQLManager sqlManager, SearchCache searchCache)
    {
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

    public String handle(Request request, Response response)
    {
        // Local constants
        final Session httpSession = request.session();
        final Map<String, Object> vm = new HashMap<>();

        // Local variables
        String cEvent;
        String rawID;
        String att;
        boolean attending;
        Integer prevID;
        DiscEvent devent;

        /****** start handle() ******/

        if(httpSession.isNew())
        {
            response.redirect(Routes.HOME_URL);
            return null;
        }

        vm.put(FTLKeys.SIGNED_IN, httpSession.attribute(FTLKeys.SIGNED_IN));
        vm.put(FTLKeys.USER, httpSession.attribute(FTLKeys.USER));
        vm.put(FTLKeys.ADMIN, httpSession.attribute(FTLKeys.ADMIN));

        cEvent = request.queryParams("chosen");
        rawID = request.queryParams("prevID");
        att = request.queryParams("att_declaration");
        if(cEvent != null) {
            devent = sqlManager.getSingleEvent(cEvent);
        } else if(rawID != null){
            prevID = Integer.parseInt(rawID);
            devent = sqlManager.getSingleEvent(prevID);
        } else {
            response.redirect(Routes.HOME_URL);
            return null;
        }

        if(att != null){
            switch(att){
                case "yes":
                    attending = true;
                    break;
                default:
                    attending = false;
                    break;
            }
            sqlManager.addAttendee(devent.getEventID(), httpSession.attribute(FTLKeys.USER), attending);
            if(cEvent != null) {
                devent = sqlManager.getSingleEvent(cEvent);
            } else if(rawID != null){
                prevID = Integer.parseInt(rawID);
                devent = sqlManager.getSingleEvent(prevID);
            }
        }

        vm.put("eventLoc", devent.getEventLocation());
        vm.put("eventTime", devent.getEventTime());
        vm.put("eventName", devent.getEventName());
        vm.put("eventArtist", devent.getEventArtist());
        vm.put("eventID", devent.getEventID());
        vm.put("attendees", devent.getAttendees());
        vm.put("declared", sqlManager.getUserAttending(devent, httpSession.attribute(FTLKeys.USER)));

        return templateEngine.render(new ModelAndView(vm, FTLKeys.EVENTS_VIEW));
    }
}
