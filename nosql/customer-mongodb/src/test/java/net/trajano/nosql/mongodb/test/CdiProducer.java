package net.trajano.nosql.mongodb.test;

import java.io.IOException;
import java.util.UUID;

import javax.enterprise.inject.Disposes;
import javax.enterprise.inject.Produces;
import javax.inject.Singleton;

import com.mongodb.DB;
import com.mongodb.Mongo;

import de.flapdoodle.embedmongo.distribution.Version;
import de.flapdoodle.embedmongo.tests.MongodForTestsFactory;

/**
 * Produces embedded MongoDB objects for CDI.
 */
public class CdiProducer {
	@Produces
	@Singleton
	public Mongo createMongo(final MongodForTestsFactory testFactory)
			throws Exception {
		return testFactory.newMongo();
	}

	/**
	 * Produces a new Mongo database using a random name.
	 * 
	 * @param mongo
	 *            mongo instance.
	 * @return database instance
	 */
	@Produces
	public DB createMongoDB(final Mongo mongo) {
		return mongo.getDB(UUID.randomUUID().toString());
	}

	/**
	 * Produces a Mongod test factory that uses the current release version of
	 * MongoDB. It is disposed by
	 * {@link #disposeMongodForTestsFactory(MongodForTestsFactory)}.
	 * 
	 * @return a test factory
	 * @throws IOException
	 */
	@Produces
	@Singleton
	public MongodForTestsFactory createMongodForTestsFactory()
			throws IOException {
		return MongodForTestsFactory.with(Version.Main.V2_0);
	}

	public void disposeDB(@Disposes final DB db) {
		db.dropDatabase();
	}

	public void disposeMongo(@Disposes final Mongo mongo) {
		mongo.close();
	}

	public void disposeMongodForTestsFactory(
			@Disposes final MongodForTestsFactory testFactory) {
		testFactory.shutdown();
	}
}
