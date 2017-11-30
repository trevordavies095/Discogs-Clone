package com.DiscogsApp.ui;

import com.DiscogsApp.appl.SQLManager;
import com.sun.org.apache.regexp.internal.RE;
import spark.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class PostAdminRoute implements Route
{
    // Class constants
    private final TemplateEngine templateEngine;
    private final SQLManager sqlManager;

    // Class variables

    public PostAdminRoute(TemplateEngine templateEngine, SQLManager sqlManager)
    {
        // Local constants

        // Local variables

        /****** start PostAdminRoute() ******/

        Objects.requireNonNull(templateEngine, "Template engine must not be null");
        Objects.requireNonNull(sqlManager, "sqlManager must not be null");
        this.sqlManager = sqlManager;
        this.templateEngine = templateEngine;
    }

    private int insertSong(Request request)
    {
        // Local constants

        // Local variables
        String title = request.queryParams("sTitle");
        String length = request.queryParams("sLength");
        String albumBC = request.queryParams("albumBC");
        String genre = request.queryParams("genre");
        String releaseYear = request.queryParams("sReleaseYear");
        boolean explicit = Boolean.getBoolean(request.queryParams("explicit"));

        /****** start insertSong() ******/

        if(title.equals("") || length.equals("") || albumBC.equals(""))
            return 2;
        if(releaseYear.equals(""))
            releaseYear = "0";

        return sqlManager.addSong(title, length, genre, explicit, releaseYear, albumBC);
    }

    private int insertArtist(Request request)
    {
        // Local constants

        // Local variables
        String title = request.queryParams("arTitle");
        String realName = request.queryParams("realName");
        String labelName = request.queryParams("arLabelName");
        String dbYear = request.queryParams("arDebutYear");

        /****** start insertArtist() ******/

        if(title.equals("") || labelName.equals(""))
            return 2;
        if(dbYear.equals(""))
            dbYear = "0";

        return sqlManager.addArtist(title, realName, labelName, dbYear);
    }

    private int insertAlbum(Request request)
    {
        // Local constants

        // Local variables
        String barcode = request.queryParams("barcode");
        String style = request.queryParams("style");
        String genre = request.queryParams("abGenre");
        String title = request.queryParams("abTitle");
        String artistID = request.queryParams("artistID");

        /****** start insertAlbum() ******/

        if(barcode.equals("") || title.equals("") || artistID.equals(""))
            return 2;

        return sqlManager.addAlbum(barcode, style, genre, title, artistID);
    }

    private int insertLabel(Request request)
    {
        // Local constants

        // Local variables
        String name = request.queryParams("labelName");
        String formYear = request.queryParams("formYear");
        String netWorth = request.queryParams("netWorth");

        /****** start insertLabel() ******/

        if(name.equals(""))
            return 2;

        return sqlManager.addLabel(name, formYear, netWorth);
    }

    private int deleteSong(Request request)
    {
        // Local constants

        // Local variables
        String title = request.queryParams("rmSongTitle");
        String songID = request.queryParams("rmSongID");

        /****** start deleteSong() ******/

        if(title.equals("") && songID.equals(""))
            return 2;

        return sqlManager.removeSong(title, songID);
    }

    private int deleteAlbum(Request request)
    {
        // Local constants

        // Local variables
        String barcode = request.queryParams("rmAlbumBC");
        String title = request.queryParams("rmAlbumTitle");

        /****** start deleteAlbum() ******/

        if(title.equals("") && barcode.equals(""))
            return 2;

        return sqlManager.removeAlbum(title, barcode);
    }

    private int deleteArtist(Request request)
    {
        // Local constants

        // Local variables
        String name = request.queryParams("rmArtistName");
        String artistID = request.queryParams("rmArtistID");

        /****** start deleteArtist() ******/

        if(name.equals("") && artistID.equals(""))
            return 2;

        return sqlManager.removeArtist(name, artistID);
    }

    private int deleteLabel(Request request)
    {
        // Local constants

        // Local variables
        String name = request.queryParams("rmLabelName");

        /****** start deleteLabel() ******/

        if(name.equals(""))
            return 2;

        return sqlManager.removeLabel(name);
    }

    private void makeDeleteStatusMsg(int status, Map<String, Object> vm, String tbl)
    {
        // Local constants

        // Local variables

        /****** start makeDeleteStatusMsg() ******/

        if(status == 0)
        {
            vm.put(FTLKeys.MESSAGE, "Failed to remove " + tbl + ": unknown SQL error. Ensure you aren't " +
                    "attempting to delete a tuple which does not exist.");
            vm.put(FTLKeys.MSG_TYPE, FTLKeys.MSG_TYPE_ERR);
        }

        else if(status == 2)
        {
            vm.put(FTLKeys.MESSAGE, "Failed to remove " + tbl + ": please enter values for at least one attribute.");
            vm.put(FTLKeys.MSG_TYPE, FTLKeys.MSG_TYPE_ERR);
        }

        else
        {
            vm.put(FTLKeys.MESSAGE, "Successfully removed " + tbl + " from database.");
            vm.put(FTLKeys.MSG_TYPE, FTLKeys.MSG_TYPE_INFO);
        }
    }

    private void makeInsertStatusMsg(int status, Map<String, Object> vm, String tbl)
    {
        // Local constants

        // Local variables

        /****** start makeInsertStatusMsg() ******/

        if(status == 0)
        {
            vm.put(FTLKeys.MESSAGE, "Failed to add " + tbl + ": unknown SQL error. Ensure you aren't" +
                    "violating a unique attribute restriction or attempting to add a non-existent" +
                    "foreign key.");
            vm.put(FTLKeys.MSG_TYPE, FTLKeys.MSG_TYPE_ERR);
        }

        else if(status == 2)
        {
            vm.put(FTLKeys.MESSAGE, "Failed to add " + tbl + ": please enter values for all REQUIRED attributes.");
            vm.put(FTLKeys.MSG_TYPE, FTLKeys.MSG_TYPE_ERR);
        }

        else
        {
            vm.put(FTLKeys.MESSAGE, "Successfully added " + tbl + " to database.");
            vm.put(FTLKeys.MSG_TYPE, FTLKeys.MSG_TYPE_INFO);
        }
    }

    public String handle(Request request, Response response)
    {
        // Local constants
        final Session httpSession = request.session();
        final Map<String, Object> vm = new HashMap<>();

        // Local variables
        int success;
        String action;

        /****** start handle() ******/

        if(httpSession.isNew())
        {
            response.redirect(Routes.HOME_URL);
            return null;
        }

        action = request.queryParams("action");

        switch(action)
        {
            case "addSong":
                vm.put(FTLKeys.MESSAGE, "Entered Add Song Tool.");
                vm.put(FTLKeys.MSG_TYPE, FTLKeys.MSG_TYPE_INFO);
                vm.put(FTLKeys.SPECIFIC, action);
                break;
            case "addAlbum":
                vm.put(FTLKeys.MESSAGE, "Entered Add Album Tool.");
                vm.put(FTLKeys.MSG_TYPE, FTLKeys.MSG_TYPE_INFO);
                vm.put(FTLKeys.SPECIFIC, action);
                break;
            case "addArtist":
                vm.put(FTLKeys.MESSAGE, "Entered Add Artist Tool.");
                vm.put(FTLKeys.MSG_TYPE, FTLKeys.MSG_TYPE_INFO);
                vm.put(FTLKeys.SPECIFIC, action);
                break;
            case "addLabel":
                vm.put(FTLKeys.MESSAGE, "Entered Add Label Tool.");
                vm.put(FTLKeys.MSG_TYPE, FTLKeys.MSG_TYPE_INFO);
                vm.put(FTLKeys.SPECIFIC, action);
                break;
            case "removeSong":
                vm.put(FTLKeys.MESSAGE, "Entered Remove Song Tool.");
                vm.put(FTLKeys.MSG_TYPE, FTLKeys.MSG_TYPE_INFO);
                vm.put(FTLKeys.SPECIFIC, action);
                break;
            case "removeAlbum":
                vm.put(FTLKeys.MESSAGE, "Entered Remove Album Tool.");
                vm.put(FTLKeys.MSG_TYPE, FTLKeys.MSG_TYPE_INFO);
                vm.put(FTLKeys.SPECIFIC, action);
                break;
            case "removeArtist":
                vm.put(FTLKeys.MESSAGE, "Entered Remove Artist Tool.");
                vm.put(FTLKeys.MSG_TYPE, FTLKeys.MSG_TYPE_INFO);
                vm.put(FTLKeys.SPECIFIC, action);
                break;
            case "removeLabel":
                vm.put(FTLKeys.MESSAGE, "Entered Remove Label Tool.");
                vm.put(FTLKeys.MSG_TYPE, FTLKeys.MSG_TYPE_INFO);
                vm.put(FTLKeys.SPECIFIC, action);
                break;
            case "addSongAct":
                success = insertSong(request);
                vm.put(FTLKeys.SPECIFIC, "addSong");
                makeInsertStatusMsg(success, vm, "song");
                break;
            case "addArtistAct":
                success = insertArtist(request);
                vm.put(FTLKeys.SPECIFIC, "addArtist");
                makeInsertStatusMsg(success, vm, "artist");
                break;
            case "addAlbumAct":
                success = insertAlbum(request);
                vm.put(FTLKeys.SPECIFIC, "addAlbum");
                makeInsertStatusMsg(success, vm, "album");
                break;
            case "addLabelAct":
                success = insertLabel(request);
                vm.put(FTLKeys.SPECIFIC, "addLabel");
                makeInsertStatusMsg(success, vm, "label");
                break;
            case "removeSongAct":
                success = deleteSong(request);
                vm.put(FTLKeys.SPECIFIC, "removeSong");
                makeDeleteStatusMsg(success, vm, "song");
                break;
            case "removeAlbumAct":
                success = deleteAlbum(request);
                vm.put(FTLKeys.SPECIFIC, "removeAlbum");
                makeDeleteStatusMsg(success, vm, "album");
                break;
            case "removeArtistAct":
                success = deleteArtist(request);
                vm.put(FTLKeys.SPECIFIC, "removeArtist");
                makeDeleteStatusMsg(success, vm, "artist");
                break;
            case "removeLabelAct":
                success = deleteLabel(request);
                vm.put(FTLKeys.SPECIFIC, "removeLabel");
                makeDeleteStatusMsg(success, vm, "label");
                break;
        }

        vm.put(FTLKeys.SIGNED_IN, httpSession.attribute(FTLKeys.SIGNED_IN));
        vm.put(FTLKeys.USER, httpSession.attribute(FTLKeys.USER));
        vm.put(FTLKeys.ADMIN, httpSession.attribute(FTLKeys.ADMIN));
        vm.put(FTLKeys.TOOLS, false);
        return templateEngine.render(new ModelAndView(vm, FTLKeys.ADMIN_VIEW));
    }
}
