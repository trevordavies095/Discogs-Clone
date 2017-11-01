package com.DiscogsApp.ui;

import com.DiscogsApp.appl.SQLManager;
import com.DiscogsApp.model.Label;
import com.DiscogsApp.model.SearchEnum;
import com.DiscogsApp.model.SearchObject;
import spark.*;

import java.util.*;

public class PostSearchRoute implements Route
{
    // Class constants
    private final TemplateEngine templateEngine;
    private final SQLManager sqlManager;

    // Class variables

    // Private Methods
    private ArrayList<String> format(HashMap<SearchEnum, ArrayList<SearchObject>> input){
        ArrayList<String> rtn = new ArrayList<>();
        ArrayList<SearchObject> data;
        if(input.containsKey(SearchEnum.LABEL)){
            data = input.get(SearchEnum.LABEL);
            for(SearchObject obj : data){
                rtn.add(obj.toString());
            }
        }else if(input.containsKey(SearchEnum.ARTIST)){
            data = input.get(SearchEnum.ARTIST);
            for(SearchObject obj : data){
                rtn.add(obj.toString());
            }
        }else if(input.containsKey(SearchEnum.ALBUM)){
            data = input.get(SearchEnum.ALBUM);
            for(SearchObject obj : data){
                rtn.add(obj.toString());
            }
        }else if(input.containsKey(SearchEnum.SONG)){
            data = input.get(SearchEnum.SONG);
            for(SearchObject obj : data){
                rtn.add(obj.toString());
            }
        }else{
            return null;
        }
        return rtn;
    }


    PostSearchRoute(TemplateEngine templateEngine, SQLManager sqlManager) {
        // Local constants

        // Local variables

        /****** start PostSearchRoute() ******/

        Objects.requireNonNull(templateEngine, "templateEngine must not be null");
        this.templateEngine = templateEngine;
        this.sqlManager = sqlManager;
    }

    public String handle(Request request, Response response) {
        // Local constants
        final Session httpSession = request.session();
        final Map<String, Object> vm = new HashMap<>();

        // Local variables

        /****** start handle() ******/

        vm.put(FTLKeys.PRESEARCH, true);
        vm.put(FTLKeys.POSTSEARCH, false);
        vm.put(FTLKeys.SIGNED_IN, httpSession.attribute(FTLKeys.SIGNED_IN));
        vm.put(FTLKeys.USER, httpSession.attribute(FTLKeys.USER));
        vm.put(FTLKeys.ADMIN, httpSession.attribute(FTLKeys.ADMIN));
        String song = request.queryParams("song");
        String artist =  request.queryParams("artist");
        String album = request.queryParams("album");
        String label =  request.queryParams("label");
        HashMap<SearchEnum, ArrayList<SearchObject>> results;
        results = sqlManager.parseSearch(song, album, artist, label);
        if (results == null) {
            vm.put(FTLKeys.MSG_TYPE, FTLKeys.MSG_TYPE_ERR);
            vm.put(FTLKeys.MESSAGE, "Error executing SQL query.");
            return templateEngine.render(new ModelAndView(vm, FTLKeys.SEARCH_VIEW));
        }
        Collection<String> printResults = format(results);
        if (printResults == null) {
            vm.put(FTLKeys.MSG_TYPE, FTLKeys.MSG_TYPE_ERR);
            vm.put(FTLKeys.MESSAGE, "Error parsing SQL query results.");
            return templateEngine.render(new ModelAndView(vm, FTLKeys.SEARCH_VIEW));
        }
        vm.put("song", song);
        vm.put("artist", artist);
        vm.put("album", album);
        vm.put("label", label);
        vm.put(FTLKeys.PRESEARCH, false);
        vm.put(FTLKeys.POSTSEARCH, true);
        vm.put(FTLKeys.SEARCH_RESULTS, printResults);

        return templateEngine.render(new ModelAndView(vm, FTLKeys.SEARCH_VIEW));
    }
}
