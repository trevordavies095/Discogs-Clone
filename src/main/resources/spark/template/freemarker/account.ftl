<!DOCTYPE html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"></meta>
    <title>DisClones | Knock-Off Discogs</title>
    <link rel="stylesheet" type="text/css" href="css/style.css">
</head>
<body>
<div class = "page">

    <h1>My Account: ${username}</h1>

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

    <#if revalidated>
        <a href="/updateAccount">Update Account Information</a>
        <p>First Name: ${firstname}</p>
        <p>Last Name: ${lastname}</p>
        <p>Username: ${username}</p>
        <p>Number of Songs Rated: ${songsRated}</p>
        <p>Your Top Rated Songs:</p>
        <ul>
            <#list songs as song>
                <li>${song}</li>
            </#list>
        </ul>
    <#elseif !revalidated && !attempted>
        <form action="/account" method="POST">
            <label for="password">Password:</label>
            <br/>
            <input id="password" type="password" name="password" />
            <br/>
            <button type="submit">Sign In</button>
        </form>
    <#else>
        <h2>Incorrect credentials; account access denied.</h2>
    </#if>

</div>
</body>
</html>