import psycopg2 as psy
import xml.etree.ElementTree as ET

tree = ET.parse('XMLresources/artists1017.xml')
root = tree.getroot()
artists = []
artistnames = []

for artist in root.findall('artist'):
    at = artist.find('name')
    atn = artist.find('realname')
    if at is not None:
        artists.append(at.text)
    if atn is not None:
        artistnames.append(atn.text)


try:
    conn = psy.connect("dbname='postgres' user='p32004e' host='reddwarf.cs.rit.edu' password='ievip6se0pha1sahchuD'")
    cur = conn.cursor()
    print("Connected successfully")
    for i in range(1000):
        if len(artists[i]) > 100 or len(artistnames[i]) > 100:
            print("true")
        """cur.execute("INSERT INTO artist (name, artist_id, realname) VALUES (%s, %s, %s)", (artists[i], i, artistnames[i]))"""
        """conn.commit()"""
    conn.commit()
    cur.close()
    conn.close()
    print("Connected!")
except:
    print("Error!")

