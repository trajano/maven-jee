An example of using MongoDB and Cassandra for NOSQL storage,
and GSON for JSON parsing.  Scala test is also used to perform
some tests.

This is built as an OSGi bundle that exposes its services using
OSGi Blueprint.

A high-level API was chosen for Cassandra as the web site also
recommended a high-level API should be used.

Hector was chosen as the Cassandra high-level API as it provides
richer functionality such as connection pooling and JMX.  It also
had the added bonus that it was already included as part of the
cassandra-unit test framework.
