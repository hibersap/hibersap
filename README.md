Hibersap
========

Hibersap helps developers of Java applications to call business logic in SAP backends. It defines a set of Java annotations to map SAP function modules to Java classes as well as a small, clean API to execute these function modules and handle transaction and security aspects.

Hibersap's programming model is quite similar to those of modern O/R mappers, significantly speeding up the development of SAP interfaces and making it much more fun to write the integration code.

Under the hood, Hibersap either uses the SAP Java Connector (JCo) or a JCA compatible resource adapter to communicate with the SAP backend. While retaining the benefits of JCo and JCA like transactions, security, connection pooling, etc., developers can focus on writing business logic because the need for boilerplate code is largely reduced.

For more information please visit the Hibersap home page: http://hibersap.org

## Building from Source

To build Hibersap from source, you will need the following:

* **Java Development Kit (JDK):** Version 8.
* **Apache Maven:** Version 3.0 or higher.
* **SAP Java Connector (JCo):** You will need to download the SAP JCo library and install it in your local Maven repository. Please see the instructions in `installSapJCoJarToLocalMavenRepo.sh` and `installSapJCoNativeLibToLocalMavenRepo.sh`.

```sh
mvn clean verify
```

### GPG Signing

The build process includes artifact signing using GPG with maven-gpg-plugin. To perform a full build including artifact signing, you must have GPG installed and configured.
If you want to skip GPG signing during the build (e.g., for local development), you can use:

```sh
mvn clean verify -Dgpg.skip=true
```

For release builds, ensure your GPG key is properly set up and available to Maven.

