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

    <div class="navigation">
    <#if signedIn>
        <a href="/search">Search</a>
        <a href="/account">My Account</a>
        <a href="/signout">Sign Out[${username}]</a>
    <#else>
        <a href="/signin">Sign In</a>
        <a href="/search">Search</a>
    </#if>
    </div>

    <p> Welcome to DisClones.</p>

    <#if message??>
        <div class="message ${messageType}">${message}</div>
    </#if>

</div>
</body>
</html>