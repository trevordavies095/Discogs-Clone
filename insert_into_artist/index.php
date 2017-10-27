<html>
	<head>
		<title>INSERT INTO artist</title>
		<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
		<style>
			li {listt-style: none;}
		</style>
	</head>
	<body>
		<h2>Enter information regarding the artist</h2>
		
		<form name="insert" action="insert.php" method="POST" >
			<ul>
				<li>name:</li><input type="text" name="name" />
				<li>artist_id:</li><input type="text" name="artist_id" />
				<li>realname:</li><input type="text" name="realname" />
			</ul>
			<input type="submit" />
		</form>
	</body>
</html>