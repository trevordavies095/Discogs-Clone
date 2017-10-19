import psycopg2 as psy

try:
	conn = psy.connect("dbname='postgres' user='p32004e' host='reddwarf.cs.rit.edu' password='ievip6se0pha1sahchuD'")
	print("Connected!")
except:
	print("Error!")
