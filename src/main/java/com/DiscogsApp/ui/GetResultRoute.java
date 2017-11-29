package com.DiscogsApp.ui;

import com.DiscogsApp.appl.SQLManager;
import com.DiscogsApp.appl.SearchCache;
import com.DiscogsApp.model.Album;
import com.DiscogsApp.model.Artist;
import com.DiscogsApp.model.Label;
import com.DiscogsApp.model.Song;
import spark.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class GetResultRoute implements Route {

    private final TemplateEngine templateEngine;

    private final SQLManager sqlManager;

    private final SearchCache searchCache;

    public GetResultRoute(TemplateEngine templateEngine, SQLManager sqlManager, SearchCache searchCache){

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

        /****** start handle() ******/

        if(httpSession.isNew()){
            response.redirect(Routes.HOME_URL);
            return null;
        }

        String song = request.queryParams("song");
        String album = request.queryParams("album");
        String artist = request.queryParams("artist");
        String label = request.queryParams("label");
        String newRating = request.queryParams("rateSong");

        vm.put(FTLKeys.SIGNED_IN, httpSession.attribute(FTLKeys.SIGNED_IN));
        vm.put(FTLKeys.USER, httpSession.attribute(FTLKeys.USER));
        vm.put(FTLKeys.ADMIN, httpSession.attribute(FTLKeys.ADMIN));
        if(song != null) {
            Song rSong = searchCache.getSong(httpSession.attribute(FTLKeys.USER), song);
            if (rSong == null) {
                searchCache.updateCache(httpSession.attribute(FTLKeys.USER),
                        sqlManager.parseSearch(song, "", "", ""));
                rSong = searchCache.getSong(httpSession.attribute(FTLKeys.USER), song);
            }
            boolean usrRating = sqlManager.hasUserRated(httpSession.attribute(FTLKeys.USER),
                    Integer.toString(rSong.getID()));
            if(newRating != null && !newRating.equals("") && !usrRating){
                sqlManager.updateRating(httpSession.attribute(FTLKeys.USER),
                        Integer.toString(rSong.getID()), Integer.parseInt(newRating));
                searchCache.updateCache(httpSession.attribute(FTLKeys.USER),
                        sqlManager.parseSearch(song, "", "", ""));
                rSong = searchCache.getSong(httpSession.attribute(FTLKeys.USER), song);
            }
            usrRating = sqlManager.hasUserRated(httpSession.attribute(FTLKeys.USER),
                    Integer.toString(rSong.getID()));
            vm.put("song", rSong.getTitle());
            vm.put("length", rSong.getLength());
            vm.put("rating", rSong.getRating());
            vm.put("rated", usrRating);
            vm.put("genre", rSong.getGenre());
            vm.put("explicit", rSong.isExplicit());
            vm.put("ryear", Integer.toString(rSong.getReleaseYear()));
            vm.put("salbum", rSong.getAlbum().getTitle());
            vm.put("sartist", rSong.getAlbum().getArtist().getName());
        }else if(album != null){
            Album rAlbum = searchCache.getAlbum(httpSession.attribute(FTLKeys.USER), album);
            if(rAlbum == null){
                searchCache.updateCache(httpSession.attribute(FTLKeys.USER),
                        sqlManager.parseSearch("", album,"",""));
                rAlbum = searchCache.getAlbum(httpSession.attribute(FTLKeys.USER), album);
            }
            ArrayList<String> aSongs = sqlManager.getAlbumSongs(rAlbum);
            vm.put("album", rAlbum.getTitle());
            vm.put("albumBC", rAlbum.getBarcode());
            vm.put("rating", rAlbum.getRating());
            vm.put("genre", rAlbum.getGenre());
            vm.put("style", rAlbum.getStyle());
            vm.put("asongs", aSongs);
            vm.put("aartist", rAlbum.getArtist().getName());
        }else if(artist != null){
            Artist rArtist = searchCache.getArtist(httpSession.attribute(FTLKeys.USER), artist);
            if(rArtist == null){
                searchCache.updateCache(httpSession.attribute(FTLKeys.USER),
                        sqlManager.parseSearch("", "", artist, ""));
                rArtist = searchCache.getArtist(httpSession.attribute(FTLKeys.USER), artist);
            }
            ArrayList<String> aAlbums = sqlManager.getArtistAlbums(rArtist);
            vm.put("artist", rArtist.getName());
            vm.put("rname", rArtist.getRealName());
            vm.put("dyear", rArtist.getDebutYear());
            vm.put("label", rArtist.getLabel().getName());
            vm.put("aalbums", aAlbums);
            ArrayList<String> artistEvents = sqlManager.getEvents(rArtist.getArtistID());
            if(artistEvents.size() > 0) {
                vm.put("aevents", artistEvents);
            }
        }else if(label != null){
            Label rLabel = searchCache.getLabel(httpSession.attribute(FTLKeys.USER), label);
            if(rLabel == null){
                searchCache.updateCache(httpSession.attribute(FTLKeys.USER),
                        sqlManager.parseSearch("", "","",label));
                rLabel = searchCache.getLabel(httpSession.attribute(FTLKeys.USER), label);
            }
            ArrayList<String> lArtists = sqlManager.getLabelArtists(rLabel);
            vm.put("label", rLabel.getName());
            vm.put("nworth", rLabel.getNetWorth());
            vm.put("fyear", Integer.toString(rLabel.getFormed()));
            vm.put("lartists", lArtists);
        }

        return templateEngine.render(new ModelAndView(vm, FTLKeys.RESULT_VIEW));
    }

}
