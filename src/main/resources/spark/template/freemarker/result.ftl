<!DOCTYPE html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"></meta>
    <title>DisClones | Knock-Off Discogs</title>
    <link rel="stylesheet" type="text/css" href="css/style.css">
</head>
<body>
<div class = "page">

    <h1>Result Specifics</h1>

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

    <#if song??>
        <h2>Information on Song: ${song}</h2>
        <p>Song Title: ${song}</p>
        <p>Length: ${length}</p>
        <#if rated>
            <p>Rating: ${rating}</p>
        <#else>
            <form action="/result?song=${song}" method="POST">
                <label for="rateSong">Rate this Song (Enter a number between 1 and 5):</label>
                <br/>
                <input id="rateSong" type="text" name="rateSong" />
                <br/>
                <button type="submit">Submit Rating</button>
            </form>
        </#if>
        <p>Genre: ${genre}</p>
        <p>Explicit: ${explicit}</p>
        <p>Release Year: ${ryear}</p>
        <p>Album: <a href="/result?album=${salbum}">${salbum}</a></p>
        <p>Artist: <a href="/result?artist=${sartist}">${sartist}</a></p>
    <#elseif album??>
        <h2>Information on Album: ${album}</h2>
        <p>Album Title: ${album}</p>
        <p>Barcode: ${albumBC}</p>
        <p>Style: ${style}</p>
        <p>Rating: ${rating}</p>
        <p>Genre: ${genre}</p>
        <p>Songs on Album:</p>
        <ul>
            <#list asongs as sng>
                <li><a href="/result?song=${sng}">${sng}</a></li>
            </#list>
        </ul>
        <p>Artist: <a href="/result?artist=${aartist}">${aartist}</a></p>
    <#elseif artist??>
        <h2>Information on Artist: ${artist}</h2>
        <p>Group Name: ${artist}
        <p>Real Name(s): ${rname}</p>
        <p>Debut Year: ${dyear}</p>
        <p>Artist Albums:</p>
        <ul>
            <#list aalbums as albm>
                <li><a href="/result?album=${albm}">${albm}</a></li>
            </#list>
        </ul>
        <p>Label Name: <a href="/result?label=${label}">${label}</a></p>
    <#elseif label??>
        <h2>Information on Label: ${label}</h2>
        <p>Label Name: ${label}</p>
        <p>Net Worth: ${nworth}</p>
        <p>Formation Year: ${fyear}</p>
        <p>Label Artists:</p>
        <ul>
            <#list lartists as arts>
                <li><a href="/result?artist=${arts}">${arts}</a></li>
            </#list>
        </ul>
    <#else>
        <h1>Something went wrong.</h1>
    </#if>

</div>
</body>
</html>