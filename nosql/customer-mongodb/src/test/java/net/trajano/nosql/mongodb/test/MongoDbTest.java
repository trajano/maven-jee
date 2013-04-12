package net.trajano.nosql.mongodb.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.io.IOException;

import net.trajano.maven_jee6.test.LogUtil;

import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;
import com.mongodb.Mongo;

import de.flapdoodle.embed.mongo.distribution.Version;
import de.flapdoodle.embed.mongo.tests.MongodForTestsFactory;

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
	}

	@BeforeClass
	public static void setMongoDB() throws IOException {
		testsFactory = MongodForTestsFactory.with(Version.Main.PRODUCTION);
	}

	@AfterClass
	public static void tearDownMongoDB() throws Exception {
		testsFactory.shutdown();
	}

	private DB db;

	@Before
	public void setUpMongoDB() throws Exception {
		final Mongo mongo = testsFactory.newMongo();
		db = testsFactory.newDB(mongo);
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
