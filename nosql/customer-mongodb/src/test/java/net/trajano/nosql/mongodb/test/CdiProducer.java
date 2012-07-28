package net.trajano.nosql.mongodb.test;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.UUID;

import javax.enterprise.inject.Disposes;
import javax.enterprise.inject.Produces;
import javax.inject.Singleton;

import com.mongodb.DB;
import com.mongodb.Mongo;

import de.flapdoodle.embedmongo.MongoDBRuntime;
import de.flapdoodle.embedmongo.MongodExecutable;
import de.flapdoodle.embedmongo.MongodProcess;
import de.flapdoodle.embedmongo.config.MongodConfig;
import de.flapdoodle.embedmongo.distribution.Version;
import de.flapdoodle.embedmongo.runtime.Network;

/**
 * Created with IntelliJ IDEA. User: trajano Date: 12-05-28 Time: 11:05 PM To
 * change this template use File | Settings | File Templates.
 */
public class CdiProducer {
	@Produces
	@Singleton
	public Mongo createMongo(final MongodProcess process,
			@EmbeddedMongo final int port) throws Exception {
		return new Mongo("localhost", port);
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

	@Produces
	@Singleton
	public MongodExecutable createMongodExecutable(@EmbeddedMongo final int port)
			throws IOException {
		final MongoDBRuntime runtime = MongoDBRuntime.getDefaultInstance();
		return runtime.prepare(new MongodConfig(Version.V2_0_5, port, Network
				.localhostIsIPv6()));
	}

	@Produces
	@Singleton
	public MongodProcess createMongodProcess(
			final MongodExecutable mongodExecutable) throws IOException {
		return mongodExecutable.start();
	}

	public void disposeDB(@Disposes final DB db) {
		db.dropDatabase();
	}

	public void disposeMongo(@Disposes final Mongo mongo) {
		mongo.close();
	}

	public void disposeMongodExecutable(
			@Disposes final MongodExecutable mongodExecutable) {
		mongodExecutable.cleanup();
	}

	public void disposeMongodProcess(@Disposes final MongodProcess mongodProcess) {
		System.out.println("stopping process");
		mongodProcess.stop();
		System.out.println("stopped process");
	}

	@Produces
	@EmbeddedMongo
	@Singleton
	public int getOpenPort() throws IOException {
		final ServerSocket socket = new ServerSocket(0);
		final int port = socket.getLocalPort();
		socket.close();
		return port;
	}
}
