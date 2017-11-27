<!DOCTYPE html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"></meta>
    <title>DisClones | Knock-Off Discogs</title>
    <link rel="stylesheet" type="text/css" href="css/style.css">
</head>
<body>
<div class="page">

    <h1>Search</h1>

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

    <#if message??>
        <div class = "message ${messageType}">${message}</div>
    </#if>

    <#if presearch>
        <form action="/search" method="POST">
            <br/>
            <label for="artist">Artist:</label>
            <br/>
            <input id="artist" type="text" name="artist" />
            <br/>
            <label for="song">Song Title:</label>
            <br/>
            <input id="song" type="text" name="song" />
            <br/>
            <label for="album">Album Title:</label>
            <br/>
            <input id="album" type="text" name="album" />
            <br/>
            <label for="label">Record Label:</label>
            <br/>
            <input id="label" type="text" name="label" />
            <br/>
            <button type="submit">Search</button>
        </form>
    </#if>

    <#if postsearch>
    <h2>Search Results by Category</h2>
    <#if songs??>
        <h3>Songs:</h3>
        <ul>
            <#list songs as song>
                <li><a href="/result?song=${song}">${song}</a></li>
            </#list>
        </ul>
    </#if>
    <#if albums??>
        <h3>Albums:</h3>
        <ul>
            <#list albums as album>
                <li><a href="/result?album=${album}">${album}</a></li>
            </#list>
        </ul>
    </#if>
    <#if artists??>
        <h3>Artists:</h3>
        <ul>
            <#list artists as artist>
                <li><a href="/result?artist=${artist}">${artist}</a></li>
            </#list>
        </ul>
    </#if>
    <#if labels??>
        <h3>Labels:</h3>

        <ul>
            <#list labels as label>
                <li><a href="/result?label=${label}">${label}</a></li>
            </#list>
        </ul>
    </#if>
    </#if>

</div>
</body>
</html>