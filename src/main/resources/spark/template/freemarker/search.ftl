<!DOCTYPE html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"></meta>
    <title>DisClones | Knock-Off Discogs</title>
    <link rel="stylesheet" type="text/css" href="css/style.css">
</head>
<body>
<div class="page">

    <h1>Search</h1>

    <#if message??>
        <div class = "message ${messageType}">${message}</div>
    </#if>

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

</div>
</body>
</html>