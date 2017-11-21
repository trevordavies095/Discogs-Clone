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

            <div class="wrapper">
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
                    <h2>Search Results</h2>
                    <ul>
                        <#list searchResults as rslt>
                            <li>${rslt}</li>
                        </#list>
                    </ul>
                </#if>
            </div>
        </div>
    </body>
</html>