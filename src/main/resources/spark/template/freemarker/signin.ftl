<!DOCTYPE html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"></meta>
    <title>DisClones | Knock-Off Discogs</title>
    <link rel="stylesheet" type="text/css" href="css/style.css">
</head>
<body>
<div class="page">

    <h1>Disc Clones Sign-In</h1>

    <div class="navigation">
        <a href="/">home</a>
    </div>

    <div>

        <#if message??>
            <div class = "message ${messageType}">${message}</div>
        </#if>

        <form action="/signin" method="POST">
            <br/>
            <label for="username">Username:</label>
            <br/>
            <input id="username" type="text" name="username" />
            <br/>
            <label for="password">Password:</label>
            <br/>
            <input id="password" type="password" name="password" />
            <br/>
            <button type="submit">Sign In</button>
        </form>

        <p>Need an account? Sign up <a href="./signup">here.</a></p>
    </div>

</div>
</body>
</html>
