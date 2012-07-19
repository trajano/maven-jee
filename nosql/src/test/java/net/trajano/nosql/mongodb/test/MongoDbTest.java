package net.trajano.nosql.mongodb.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.net.ServerSocket;
import java.util.UUID;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;
import com.mongodb.Mongo;

import de.flapdoodle.embedmongo.MongoDBRuntime;
import de.flapdoodle.embedmongo.MongodExecutable;
import de.flapdoodle.embedmongo.MongodProcess;
import de.flapdoodle.embedmongo.config.MongodConfig;
import de.flapdoodle.embedmongo.distribution.Version;
import de.flapdoodle.embedmongo.runtime.Network;

/**
 * Shows how to test using MongoDB without CDI. This is useful to show the steps
 * necessary to use the embedded version of MongoDB for testing. This is based
 * on http://stackoverflow.com/a/9830861/242042.
 */
public class MongoDbTest {
	private DB db;

	private MongodExecutable mongodExecutable;
	private MongodProcess mongoProcess;

	@Before
	public void setUpMongoDB() throws Exception {
		// Get open port
		final ServerSocket socket = new ServerSocket(0);
		final int port = socket.getLocalPort();
		socket.close();

		// create runtime
		final MongoDBRuntime runtime = MongoDBRuntime.getDefaultInstance();
		mongodExecutable = runtime.prepare(new MongodConfig(Version.V2_0_4,
				port, Network.localhostIsIPv6()));
		mongoProcess = mongodExecutable.start();

		// create database
		final Mongo mongo = new Mongo("localhost", port);
		db = mongo.getDB(UUID.randomUUID().toString());
	}

	@After
	public void tearDownMongoDB() throws Exception {
		// cleanup
		mongoProcess.stop();
		mongodExecutable.cleanup();
	}

	/**
	 * This is an example based on
	 * http://www.mongodb.org/display/DOCS/Java+Tutorial to see if things work.
	 */
	@Test
	public void testCreateRuntime() throws Exception {

		// perform operations
		final DBCollection coll = db.getCollection("testCollection");
		{
			final BasicDBObject doc = new BasicDBObject();

			doc.put("name", "MongoDB");
			doc.put("type", "database");
			doc.put("count", 1);

			final BasicDBObject info = new BasicDBObject();

			info.put("x", 203);
			info.put("y", 102);

			doc.put("info", info);

			coll.insert(doc);
		}
		{
			final DBObject myDoc = coll.findOne();
			assertEquals("MongoDB", myDoc.get("name"));
			assertEquals(203, ((BasicDBObject) myDoc.get("info")).get("x"));
		}
	}

	public void testDatabaseCreated() {
		assertNotNull(db);
	}
}
