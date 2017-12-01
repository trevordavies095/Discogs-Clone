<!DOCTYPE html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"></meta>
    <title>DisClones | Knock-Off Discogs</title>
    <link rel="stylesheet" type="text/css" href="css/style.css">
</head>
<body>
    <div class = "page">

        <h1>Administrator Tools</h1>

        <nav>
        <#if signedIn>
            <ul id = "navigation">
                <li><a href="/">Home</a></li>
                <li><a href="/search">Search</a></li>
                <li><a href="/account">My Account</a></li>
                <li><a id="signOut" href="/signout">Sign Out[${username}]</a></li>
                <#if admin>
                    <li><a href="/admin">Manage Database</a></li>
                </#if>
            </ul>
        <#else>
            <ul id = "navigation">
                <li><a href="/">Home</a></li>
                <li><a href="/signin">Sign In</a></li>
                <li><a href="/search">Search</a></li>
            </ul>
        </#if>
        </nav>

        <#if !admin>
            <h2>${message}</h2>
        <#elseif tools>
            <h2>${message}</h2>
            <h2>Tools:</h2>
            </br>
            <form action="/admin" method="POST">
                <select id="action" name="action">
                    <option value="addSong">Add Song</option>
                    <option value="addAlbum">Add Album</option>
                    <option value="addArtist">Add Artist</option>
                    <option value="addLabel">Add Label</option>
                    <option value="addEdition">Add Edition</option>
                    <option value="addLocation">Add Location</option>
                    <option value="removeSong">Remove Song</option>
                    <option value="removeAlbum">Remove Album</option>
                    <option value="removeArtist">Remove Artist</option>
                    <option value="removeLabel">Remove Label</option>
                    <option value="removeEdition">Remove Edition</option>
                    <option value="removeEvent">Remove Event</option>
                    <option value="removeLocation">Remove Location</option>
                </select>
                <br/>
                <button type="submit">Submit</button>

            </form>
        <#elseif specific??>
            <#if message??>
                <div class="message ${messageType}">${message}</div>
            </#if>

            <form action="/admin" method="POST">
            <#if specific == "addSong">
                <br/>
                <label for="sTitle">Song Title (REQUIRED):</label>
                <br/>
                <input id="sTitle" type="text" name="sTitle" />
                <br/>
                <label for="sLength">Song Length (REQUIRED):</label>
                <br/>
                <input id="sLength" type="text" name="sLength"  />
                <br/>
                <label for="albumBC">Album Barcode (REQUIRED):</label>
                <br/>
                <input id="albumBC" type="text" name="albumBC" />
                <br/>
                <label for="genre">Song Genre:</label>
                <br/>
                <input id="genre" type="text" name="genre" />
                <br/>
                <label for="explicit">Explicit (enter true or false):</label>
                <br/>
                <input id="explicit" type="text" name="explicit">
                <br/>
                <label for="sReleaseYear">Release Year:</label>
                <br/>
                <input id="sReleaseYear" type="text" name="sReleaseYear">
                <br/>
                <button value="addSongAct" name="action" type="submit">Add Song</button>
            <#elseif specific == "addArtist">
                <br/>
                <label for="arTitle">Artist Name (REQUIRED):</label>
                <br/>
                <input id="arTitle" type="text" name="arTitle" />
                <br/>
                <label for="arDebutYear">Debut Year:</label>
                <br/>
                <input id="arDebutYear" type="text" name="arDebutYear"  />
                <br/>
                <label for="realName">Real Name:</label>
                <br/>
                <input id="realName" type="text" name="realName" />
                <br/>
                <label for="arLabelName">Label Name (REQUIRED):</label>
                <br/>
                <input id="arLabelName" type="text" name="arLabelName" />
                <br/>
                <button value="addArtistAct" name="action" type="submit">Add Artist</button>
            <#elseif specific == "addAlbum">
                <br/>
                <label for="abTitle">Album Title (REQUIRED):</label>
                <br/>
                <input id="abTitle" type="text" name="abTitle" />
                <br/>
                <label for="barcode">Barcode (REQUIRED):</label>
                <br/>
                <input id="barcode" type="text" name="barcode"  />
                <br/>
                <label for="style">Style:</label>
                <br/>
                <input id="style" type="text" name="style" />
                <br/>
                <label for="abGenre">Genre:</label>
                <br/>
                <input id="abGenre" type="text" name="abGenre" />
                <br/>
                <label for="artistID">Artist ID (REQUIRED):</label>
                <br/>
                <input id="artistID" type="text" name="artistID" />
                <br/>
                <button value="addAlbumAct" name="action" type="submit">Add Album</button>
            <#elseif specific == "addLabel">
                <br/>
                <label for="labelName">Label Name (REQUIRED):</label>
                <br/>
                <input id="labelName" type="text" name="labelName" />
                <br/>
                <label for="formYear">Formation Year:</label>
                <br/>
                <input id="formYear" type="text" name="formYear" />
                <br/>
                <label for="netWorth">Net Worth:</label>
                <br/>
                <input id="netWorth" type="text" name="netWorth" />
                <br/>
                <button value="addLabelAct" name="action" type="submit">Add Label</button>
            <#elseif specific == "addEdition">
                <br/>
                <label for="editionTitle">Edition Title (REQUIRED):</label>
                <br/>
                <input id="editionTitle" type="text" name="editionTitle" />
                <br/>
                <label for="editionBarcode">Edition Barcode (REQUIRED):</label>
                <br/>
                <input id="editionBarcode" type="text" name="editionBarcode" />
                <br/>
                <label for="baseBarcode">Base Edition Barcode (REQUIRED):</label>
                <br/>
                <input id="baseBarcode" type="text" name="baseBarcode" />
                <br/>
                <label for="editionStyle">Edition Style:</label>
                <br/>
                <input id="editionStyle" type="text" name="editionStyle" />
                <br/>
                <button value="addEditionAct" name="action" type="submit">Add Edition</button>
            <#elseif specific == "addLocation">
                <br/>
                <label for="locationName">Location Name (REQUIRED):</label>
                <br/>
                <input id="locationName" type="text" name="locationName" />
                <br/>
                <label for="locationCap">Location Capacity (REQUIRED):</label>
                <br/>
                <input id="locationCap" type="text" name="locationCap" />
                <br/>
                <label for="locationCity">Location City (REQUIRED):</label>
                <br/>
                <input id="locationCity" type="text" name="locationCity" />
                <br/>
                <label for="locationState">Location State (REQUIRED, Enter 2 Character Abbreviation):</label>
                <br/>
                <input id="locationState" type="text" name="locationState" />
                <br/>
                <button value="addLocationAct" name="action" type="submit">Add Location</button>
            <#elseif specific == "removeSong">
                <br/>
                <label for="rmSongTitle">Song Title:</label>
                <br/>
                <input id="rmSongTitle" type="text" name="rmSongTitle" />
                <br/>
                <label for="rmSongID">Song ID:</label>
                <br/>
                <input id="rmSongID" type="text" name="rmSongID" />
                <br/>
                <button value="removeSongAct" name="action" type="submit">Remove Song</button>
            <#elseif specific == "removeArtist">
                <br/>
                <label for="rmArtistName">Artist Name:</label>
                <br/>
                <input id="rmArtistName" type="text" name="rmArtistName" />
                <br/>
                <label for="rmArtistID">Artist ID:</label>
                <br/>
                <input id="rmArtistID" type="text" name="rmArtistID" />
                <br/>
                <button value="removeArtistAct" name="action" type="submit">Remove Artist</button>
            <#elseif specific == "removeAlbum">
                <br/>
                <label for="rmAlbumTitle">Album Title:</label>
                <br/>
                <input id="rmAlbumTitle" type="text" name="rmAlbumTitle" />
                <br/>
                <br/>
                <label for="rmAlbumBC">Album Barcode:</label>
                <br/>
                <input id="rmAlbumBC" type="text" name="rmAlbumBC" />
                <br/>
                <button value="removeAlbumAct" name="action" type="submit">Remove Album</button>
            <#elseif specific == "removeLabel">
                <br/>
                <label for="rmLabelName">Label Name (REQUIRED):</label>
                <br/>
                <input id="rmLabelName" type="text" name="rmLabelName" />
                <br/>
                <button value="removeLabelAct" name="action" type="submit">Remove Label</button>
            <#elseif specific == "removeLocation">
                <br/>
                <label for="rmLocationName">Location Name (REQUIRED):</label>
                <br/>
                <input id="rmLocationName" type="text" name="rmLocationName" />
                <br/>
                <button value="removeLocationAct" name="action" type="submit">Remove Location</button>
            <#elseif specific == "removeEdition">
                <br/>
                <label for="rmEditionBC">Edition Barcode (REQUIRED):</label>
                <br/>
                <input id="rmEditionBC" type="text" name="rmEditionBC" />
                <br/>
                <button value="removeEditionAct" name="action" type="submit">Remove Edition</button>
            <#elseif specific == "removeEvent">
                <br/>
                <label for="rmEventID">Event ID (REQUIRED):</label>
                <br/>
                <input id="rmEventID" type="text" name="rmEventID" />
                <br/>
                <button value="removeEventAct" name="action" type="submit">Remove Event</button>
            </#if>
            </form>
        </#if>

    </div>
</body>
</html>