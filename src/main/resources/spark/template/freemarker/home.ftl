<!DOCTYPE html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"></meta>
    <title>DisClones | Knock-Off Discogs</title>
    <link rel="stylesheet" type="text/css" href="css/style.css">
</head>
<body>
<div class="page">

    <h1>
            ${title}${username}
    </h1>

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

    <p> Welcome to DisClones.</p>

    <#if message??>
        <div class="message ${messageType}">${message}</div>
    </#if>

</div>
</body>
</html>