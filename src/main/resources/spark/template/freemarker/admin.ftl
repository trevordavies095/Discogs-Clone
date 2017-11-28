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

            <div class="wrapper">
                <#if !admin>
                    <h2>${message}</h2>
                <#else>
                    <h2>${message}</h2>
                    <h4>Tools:</h4>
                    <button type="submit">Add Song</button>
                    <button type="submit">Add Album</button>
                    <button type="submit">Add Artist</button>
                    <button type="submit">Add Label</button>
                    <br/>
                    <button type="submit">Remove Song</button>
                    <button type="submit">Remove Album</button>
                    <button type="submit">Remove Artist</button>
                    <button type="submit">Remove Label</button>

                </#if>
            </div>
        </div>
    </body>
</html>