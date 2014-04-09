The SAP Java Connector (SAP JCo) is a toolkit that allows a Java application to communicate with any SAP system.
It combines an easy to use API with unprecedented flexibility and performance. The package supports both, Java
to SAP System as well as SAP System to Java calls.

-   All SAP Connectors are licensed without additional license fees as part of the respective solution or component license.
    However, please note that each connector may be used only for connecting external (non-SAP) applications to SAP Systems
    SAP Solutions. Scenarios, in which two external (non-SAP) applications are integrated via an SAP Connector, are not allowed.
-   The redistribution of any connector is not allowed.
-   All SAP users accessing application functionality through the relevant connector are required to be licensed under a
    respective solution or component license.

To use the SAP JCo with the Hibersap project, you need to either install the SAP JCo jar downloaded from SAP to your local
Maven repository (variant a) or deploy it to e.g. an enterprise Maven repository like Nexus or Artifactory (variant b):

(a) mvn install:install-file -DgroupId=org.hibersap -DartifactId=sapjco3 -Dversion=3.0 -Dpackaging=jar -Dfile=path/to/sapjco3.jar
(b) mvn deploy:deploy-file -DrepositoryId=[your.repo.id] -DgroupId=org.hibersap -DartifactId=sapjco3 -Dversion=3.0 -Dpackaging=jar -Dfile=path/to/sapjco3.jar
