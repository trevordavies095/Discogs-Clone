<!DOCTYPE html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"></meta>
    <title>DisClones | Knock-Off Discogs</title>
    <link rel="stylesheet" type="text/css" href="css/style.css">
</head>
<body>
<div class="page">

    <h1>Disc Clones Account Creation</h1>

    <div class="navigation">
        <a href="/">home</a>
    </div>

    <div>

    <#if message??>
        <div class = "message ${messageType}">${message}</div>
    </#if>

        <form action="/signup" method="POST">
            <br/>
            <label for="username">Input a Unique Username (must be <20 characters):</label>
            <br/>
            <input id="username" type="text" name="username" />
            <br/>
            <label for="password">Input a Password (must be <63 characters):</label>
            <br/>
            <input id="password" type="password" name="password" />
            <br/>
            <label for="firstname">Input your First Name:</label>
            <br/>
            <input id="firstname" type="text" name="firstname" />
            <br/>
            <label for="lastname">Input your Last Name:</label>
            <br/>
            <input id="lastname" type="text" "name="lastname" />
            <br/>
            <button type="submit">Create Account</button>
        </form>
    </div>

</div>
</body>
</html>
