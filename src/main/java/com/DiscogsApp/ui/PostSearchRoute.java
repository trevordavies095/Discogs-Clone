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

    PostSearchRoute(TemplateEngine templateEngine, SQLManager sqlManager, SearchCache searchCache)
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

    private HashMap<SearchEnum, ArrayList<String>> format(UserSearch input)
    {
        // Local constants

        // Local variables
        HashMap<SearchEnum, ArrayList<String>> rtn = new HashMap<>();
        ArrayList<SearchObject> data;
        ArrayList<String> songs = new ArrayList<>();
        ArrayList<String> albums = new ArrayList<>();
        ArrayList<String> artists = new ArrayList<>();
        ArrayList<String> labels =  new ArrayList<>();

        /****** start format() ******/

        if(!input.getLabels().isEmpty())
        {
            data = input.getLabels();
            for(SearchObject obj : data)
                labels.add(obj.toString());
        }

        if(!input.getArtists().isEmpty())
        {
            data = input.getArtists();
            for(SearchObject obj : data)
                artists.add(obj.toString());
        }

        if(!input.getAlbums().isEmpty())
        {
            data = input.getAlbums();
            for(SearchObject obj : data)
                albums.add(obj.toString());
        }

        if(!input.getSongs().isEmpty())
        {
            data = input.getSongs();
            for(SearchObject obj : data)
                songs.add(obj.toString());
        }

        if(songs.isEmpty() && albums.isEmpty() && artists.isEmpty() && labels.isEmpty())
            return null;

        rtn.put(SearchEnum.LABEL, labels);
        rtn.put(SearchEnum.ARTIST, artists);
        rtn.put(SearchEnum.ALBUM, albums);
        rtn.put(SearchEnum.SONG, songs);
        return rtn;
    }

    public String handle(Request request, Response response)
    {
        // Local constants
        final Session httpSession = request.session();
        final Map<String, Object> vm = new HashMap<>();

        // Local variables
        String song = request.queryParams("song");
        String artist =  request.queryParams("artist");
        String album = request.queryParams("album");
        String label =  request.queryParams("label");
        UserSearch results;
        HashMap<SearchEnum, ArrayList<String>> printResults;

        /****** start handle() ******/

        if(httpSession.isNew()){
            response.redirect(Routes.HOME_URL);
            return null;
        }

        vm.put(FTLKeys.PRESEARCH, true);
        vm.put(FTLKeys.POSTSEARCH, false);
        vm.put(FTLKeys.SIGNED_IN, httpSession.attribute(FTLKeys.SIGNED_IN));
        vm.put(FTLKeys.USER, httpSession.attribute(FTLKeys.USER));
        vm.put(FTLKeys.ADMIN, httpSession.attribute(FTLKeys.ADMIN));

        if(song.equals("") && artist.equals("") && album.equals("") && label.equals(""))
        {
            vm.put(FTLKeys.MSG_TYPE, FTLKeys.MSG_TYPE_ERR);
            vm.put(FTLKeys.MESSAGE, "Please enter at least one value before searching.");
            return templateEngine.render(new ModelAndView(vm, FTLKeys.SEARCH_VIEW));
        }

        results = sqlManager.parseSearch(song, album, artist, label);
        if (results == null)
        {
            vm.put(FTLKeys.MSG_TYPE, FTLKeys.MSG_TYPE_ERR);
            vm.put(FTLKeys.MESSAGE, "Error executing SQL query.");
            return templateEngine.render(new ModelAndView(vm, FTLKeys.SEARCH_VIEW));
        }

        searchCache.updateCache(httpSession.attribute(FTLKeys.USER), results);
        printResults = format(results);
        if (printResults == null)
        {
            vm.put(FTLKeys.MSG_TYPE, FTLKeys.MSG_TYPE_ERR);
            vm.put(FTLKeys.MESSAGE, "No results found for your search.");
            return templateEngine.render(new ModelAndView(vm, FTLKeys.SEARCH_VIEW));
        }
        if(!printResults.get(SearchEnum.SONG).isEmpty())
            vm.put("songs", printResults.get(SearchEnum.SONG));
        if(!printResults.get(SearchEnum.ARTIST).isEmpty())
            vm.put("artists", printResults.get(SearchEnum.ARTIST));
        if(!printResults.get(SearchEnum.ALBUM).isEmpty())
            vm.put("albums", printResults.get(SearchEnum.ALBUM));
        if(!printResults.get(SearchEnum.LABEL).isEmpty())
            vm.put("labels", printResults.get(SearchEnum.LABEL));
        vm.put(FTLKeys.PRESEARCH, false);
        vm.put(FTLKeys.POSTSEARCH, true);
        return templateEngine.render(new ModelAndView(vm, FTLKeys.SEARCH_VIEW));
    }
}
