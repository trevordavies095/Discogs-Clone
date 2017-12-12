Phase 3 - Group 23 (CSCI-320-03/04)
Patrick Ehrenreich, Loren Davies

-----------
Disc Clones
-----------

Run Application
	- Application runs with Maven and Spark as dependencies
	- Application also uses the postgreSQL driver to connect to the database
		- To install postgreSQL driver
			1. Open project in IntelliJ
			2. File > Project Structure
			3. Modules > Dependencies
			4. Click the “+” under the list of dependencies
			5. JARs or Directories…
			6. Choose postgresql-42.1.4.jar which is located in the src folder
	- Run mvn compile exec:java in the folder’s root (where pom.xml is)
	- Access application at localhost:4567 
	
Documentation
	- All documentation (including JavaDocs) can be found in "/src/docs"
	- UML Diagram is too large to submit as a single image with distinct dependencies; a package simplified
		version has been included as a PNG, with the full form included as a .uml file
