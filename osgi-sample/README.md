This is an OSGi project that demonstrates multiple use cases for features
provided by OSGi.

The key technologies are:

* OSGi Blueprint
* OSGi Bundle Repository
* OSGi Configuration Admin (TODO)

The Apache Felix maven-bundle-plugin was used to develop the bundles.
This was chosen as it gave the best flexibilty in terms of retaining
existing Maven project structures.

IntelliJ and Eclipse are targetted as IDEs to use this.

Install verification tests uses Apache Karaf as the OSGi test container.
However, JUnit tests use PaxExam.
