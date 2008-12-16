Quickstart Guide
----------------

* Copy src/site/jboss/server/default/deploy/A12-ds.xml to your JBoss deploy directory and configure it for your SAP system
* Copy sapjco3.jar to JBoss/server/default/lib
* Copy sapjco3.dll to JBoss/bin

* Run Maven:
  mvn -DskipTests install
* Copy resulting EAR from hibersap-ear/target/ to JBoss deploy folder
* Start JBoss

* Run Maven with tests:
  mvn test 