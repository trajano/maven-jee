An example of using MongoDB and Cassandra for NOSQL storage,
and GSON for JSON parsing.  Scala test is also used to perform
some tests.

Each implementation is its own module to prevent dependency
issues as each NOSQL implementation has its own dependency
versions and may not by the latest ones.

A simple domain module "customer" is provided to show the
separation between implementation and API.

A high-level API was chosen for Cassandra as the web site also
recommended a high-level API should be used.

Hector was chosen as the Cassandra high-level API as it provides
richer functionality such as connection pooling and JMX.  It also
had the added bonus that it was already included as part of the
cassandra-unit test framework.
