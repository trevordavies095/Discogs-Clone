<?php
	$db = pg_connect("host=reddwarf.cs.rit.edu port=5432 dbname=postgres user=p32004e password=ievip6se0pha1sahchuD");
	$query = "INSERT INTO artist VALUES ('$_POST[name]','$_POST[artist_id]','$_POST[realname]')";
	$result = pg_query($query);
	header('Location: index.php');
?>