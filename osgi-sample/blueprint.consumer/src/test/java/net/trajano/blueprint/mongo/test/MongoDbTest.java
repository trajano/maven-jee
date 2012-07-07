package net.trajano.blueprint.mongo.test;

import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.replay;
import static org.junit.Assert.assertEquals;

import java.net.ServerSocket;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Executor;

import net.trajano.blueprint.consumer.internal.ServiceUserBean;
import net.trajano.hello.osgi.IHello;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.data.mongodb.MongoDbFactory;
import org.springframework.data.mongodb.core.SimpleMongoDbFactory;

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
	private MongodExecutable mongodExecutable;

	private MongodProcess mongoProcess;
	private int port;

	@Before
	public void setUpMongoDB() throws Exception {
		// Get open port
		final ServerSocket socket = new ServerSocket(0);
		port = socket.getLocalPort();
		socket.close();

		// create runtime
		final MongoDBRuntime runtime = MongoDBRuntime.getDefaultInstance();
		mongodExecutable = runtime.prepare(new MongodConfig(Version.V2_0_4,
				port, Network.localhostIsIPv6()));
		mongoProcess = mongodExecutable.start();

	}

	@After
	public void tearDownMongoDB() throws Exception {
		// cleanup
		mongoProcess.stop();
		mongodExecutable.cleanup();
	}

	@Test
	public void testMock() throws Exception {
		final Executor executor = createMock(Executor.class);
		final IHello hello = createMock(IHello.class);
		final MongoDbFactory mongoDbFactory = new SimpleMongoDbFactory(
				new Mongo("localhost", port), "database");
		final BlockingQueue<String> queue = new ArrayBlockingQueue<String>(20);
		expect(hello.echo("hello")).andReturn("hello");
		replay(hello);
		final ServiceUserBean bean = new ServiceUserBean(executor, hello,
				mongoDbFactory, queue);
		assertEquals("olleh", bean.reverse("hello"));
	}

	@Test
	public void testUsingDBFactory() throws Exception {
		final MongoDbFactory mongoDbFactory = new SimpleMongoDbFactory(
				new Mongo("localhost", port), "database");
		final DB db = mongoDbFactory.getDb();
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
}
