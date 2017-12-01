<!DOCTYPE html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"></meta>
    <title>DisClones | Knock-Off Discogs</title>
    <link rel="stylesheet" type="text/css" href="css/style.css">
</head>
<body>
<div class="page">

    <h1>Event Information for "${eventName}"</h1>

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

    <p>Event Location: ${eventLoc}</p>
    <p>Start Time: ${eventTime}</p>
    <p>Artist Name: <a href="/result?artist=${eventArtist}">${eventArtist}</a></p>
    <#if signedIn>
        <#if declared>
            <p>Number of Attendees: ${attendees}</p>
        <#else>
            <label for="att_declaration">Will you be attending?</label>
            <form action="/event?prevID=${eventID}" method="POST">
                <select id="att_declaration" name="att_declaration">
                    <option value="yes">Yes</option>
                    <option value="no">No</option>
                </select>
                <br/>
                <button type="submit">Submit Answer</button>
            </form>
        </#if>
    <#else>
        <p>Number of Attendees: ${attendees}</p>
    </#if>

</div>
</body>
</html>