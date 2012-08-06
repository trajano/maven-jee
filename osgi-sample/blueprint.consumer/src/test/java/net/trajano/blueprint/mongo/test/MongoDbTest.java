package net.trajano.blueprint.mongo.test;

import static org.easymock.EasyMock.createStrictMock;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.replay;
import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Executor;

import net.trajano.blueprint.consumer.internal.ServiceUserBean;
import net.trajano.hello.osgi.IHello;
import net.trajano.maven_jee6.test.LogUtil;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.data.mongodb.MongoDbFactory;
import org.springframework.data.mongodb.core.SimpleMongoDbFactory;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;

import de.flapdoodle.embedmongo.distribution.Version;
import de.flapdoodle.embedmongo.tests.MongodForTestsFactory;

/**
 * Shows how to test using MongoDB without CDI. This is useful to show the steps
 * necessary to use the embedded version of MongoDB for testing. This is based
 * on http://stackoverflow.com/a/9830861/242042.
 */
public class MongoDbTest {
	private static MongodForTestsFactory testsFactory;

	@BeforeClass
	public static void setLoggingConfiguration() throws IOException {
		LogUtil.loadConfiguration();
		testsFactory = MongodForTestsFactory.with(Version.Main.V2_0);
	}

	@AfterClass
	public static void tearDownMongoDB() throws Exception {
		testsFactory.shutdown();
	}

	@Test
	public void testMock() throws Exception {
		final Executor executor = createStrictMock(Executor.class);
		final IHello hello = createStrictMock(IHello.class);
		final MongoDbFactory mongoDbFactory = new SimpleMongoDbFactory(
				testsFactory.newMongo(), "database");
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
				testsFactory.newMongo(), "database");
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
