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

    <form action="/updateAccount" method="POST">
        <br/>
        <label for="updateFName">New First Name:</label>
        <br/>
        <input id="updateFName" type="text" name="updateFName" />
        <br/>
        <label for="updateLName">New Last Name:</label>
        <br/>
        <input id="updateLName" type="text" name="updateLName" />
        <br/>
        <label for="updateUName">New Username:</label>
        <br/>
        <input id="updateUName" type="text" name="updateUName" />
        <br/>
        <button name="updateAccount" type="submit">Update Account</button>
    </form>

</div>
</body>
</html>