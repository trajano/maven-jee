package net.trajano.nosql.mongodb.test;

import java.io.IOException;
import java.util.UUID;

import javax.enterprise.inject.Disposes;
import javax.enterprise.inject.Produces;
import javax.inject.Singleton;

import com.mongodb.DB;
import com.mongodb.Mongo;

import de.flapdoodle.embed.mongo.distribution.Version;
import de.flapdoodle.embed.mongo.tests.MongodForTestsFactory;

/**
 * Created with IntelliJ IDEA. User: trajano Date: 12-05-28 Time: 11:05 PM To
 * change this template use File | Settings | File Templates.
 */
public class CdiProducer {
	@Produces
	@Singleton
	public Mongo createMongo(final MongodForTestsFactory testFactory)
			throws Exception {
		return testFactory.newMongo();
	}

	/**
	 * Produces a new mongo database using a random name.
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
	 * This produces an embedded MongoDB test factory used to create MongoDB
	 * objects in a test environment.
	 * 
	 * @return an embedded MongoDB test factory.
	 * @throws IOException
	 */
	@Produces
	@Singleton
	public MongodForTestsFactory createMongodExecutable() throws IOException {
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
