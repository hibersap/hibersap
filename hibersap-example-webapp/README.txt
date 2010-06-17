* Make sure Maven2 is installed on your computer.
  * If not, download from http://maven.apache.org and install.
* Make sure you have the SAP Java Connector (JCo) Version 3.x.
  * If not, get JCo from http://service.sap.com/connectors (need to log in with a SAP customer account).
  * Unzip the JCo distribution.
* Install the JCo jar to your local Maven repository (replacing the version number and path):
  > mvn install:install-file -DgroupId=com.sap -DartifactId=sap-jco -Dversion=3.0.x -Dpackaging=jar -Dfile=/path/to/sapjco.jar
* Enter the path to the folder containing the JCo native lib in: pom.xml - <properties> - <jco.native.lib.path>.
* Open a command line, go to the project root folder (containing the pom.xml) and start the application with:
  > mvn install jetty:run
* Open the URL http://localhost:8080/flightcustomers