package com.DiscogsApp.ui;

import com.DiscogsApp.appl.SQLManager;
import com.DiscogsApp.appl.SearchCache;
import com.DiscogsApp.model.Label;
import com.DiscogsApp.model.SearchEnum;
import com.DiscogsApp.model.SearchObject;
import com.DiscogsApp.model.UserSearch;
import spark.*;

import java.util.*;

public class PostSearchRoute implements Route
{
    // Class constants
    private final TemplateEngine templateEngine;
    private final SQLManager sqlManager;
    private final SearchCache searchCache;

    // Class variables

    // Private Methods
    private HashMap<SearchEnum, ArrayList<String>> format(UserSearch input){
        HashMap<SearchEnum, ArrayList<String>> rtn = new HashMap<>();
        ArrayList<SearchObject> data;
        ArrayList<String> songs = new ArrayList<>();
        ArrayList<String> albums = new ArrayList<>();
        ArrayList<String> artists = new ArrayList<>();
        ArrayList<String> labels =  new ArrayList<>();
        if(!input.getLabels().isEmpty()){
            data = input.getLabels();
            for(SearchObject obj : data){
                labels.add(obj.toString());
            }
        }
        if(!input.getArtists().isEmpty()){
            data = input.getArtists();
            for(SearchObject obj : data){
                artists.add(obj.toString());
            }
        }
        if(!input.getAlbums().isEmpty()){
            data = input.getAlbums();
            for(SearchObject obj : data){
                albums.add(obj.toString());
            }
        }
        if(!input.getSongs().isEmpty()){
            data = input.getSongs();
            for(SearchObject obj : data){
                songs.add(obj.toString());
            }
        }
        if(songs.isEmpty() && albums.isEmpty() && artists.isEmpty() && labels.isEmpty()) return null;
        rtn.put(SearchEnum.LABEL, labels);
        rtn.put(SearchEnum.ARTIST, artists);
        rtn.put(SearchEnum.ALBUM, albums);
        rtn.put(SearchEnum.SONG, songs);
        return rtn;
    }


    PostSearchRoute(TemplateEngine templateEngine, SQLManager sqlManager, SearchCache searchCache) {
        // Local constants

        // Local variables

        /****** start PostSearchRoute() ******/

        Objects.requireNonNull(templateEngine, "templateEngine must not be null");
        this.templateEngine = templateEngine;
        this.sqlManager = sqlManager;
        this.searchCache = searchCache;
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
        if(song.equals("") && artist.equals("") && album.equals("") && label.equals("")){
            vm.put(FTLKeys.MSG_TYPE, FTLKeys.MSG_TYPE_ERR);
            vm.put(FTLKeys.MESSAGE, "Please enter at least one value before searching.");
            return templateEngine.render(new ModelAndView(vm, FTLKeys.SEARCH_VIEW));
        }
        UserSearch results;
        results = sqlManager.parseSearch(song, album, artist, label);
        if (results == null) {
            vm.put(FTLKeys.MSG_TYPE, FTLKeys.MSG_TYPE_ERR);
            vm.put(FTLKeys.MESSAGE, "Error executing SQL query.");
            return templateEngine.render(new ModelAndView(vm, FTLKeys.SEARCH_VIEW));
        }
        searchCache.updateCache(httpSession.attribute(FTLKeys.USER), results);
        HashMap<SearchEnum, ArrayList<String>> printResults = format(results);
        if (printResults == null) {
            vm.put(FTLKeys.MSG_TYPE, FTLKeys.MSG_TYPE_ERR);
            vm.put(FTLKeys.MESSAGE, "No results found for your search.");
            return templateEngine.render(new ModelAndView(vm, FTLKeys.SEARCH_VIEW));
        }
        vm.put("songs", printResults.get(SearchEnum.SONG));
        vm.put("artists", printResults.get(SearchEnum.ARTIST));
        vm.put("albums", printResults.get(SearchEnum.ALBUM));
        vm.put("labels", printResults.get(SearchEnum.LABEL));
        vm.put(FTLKeys.PRESEARCH, false);
        vm.put(FTLKeys.POSTSEARCH, true);

        return templateEngine.render(new ModelAndView(vm, FTLKeys.SEARCH_VIEW));
    }
}
