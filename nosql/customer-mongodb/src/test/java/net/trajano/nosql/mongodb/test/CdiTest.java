package net.trajano.nosql.mongodb.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import javax.inject.Inject;

import net.trajano.maven_jee6.test.LogUtil;
import net.trajano.nosql.Customer;
import net.trajano.nosql.Customers;
import net.trajano.nosql.internal.MongoDbCustomers;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;
import com.mongodb.util.JSON;

@RunWith(Arquillian.class)
public class CdiTest {
	@Deployment
	public static JavaArchive createDeployment() {
		return ShrinkWrap.create(JavaArchive.class).addClass(CdiProducer.class)
				.addClass(Customers.class).addClass(MongoDbCustomers.class)
				.addAsManifestResource(EmptyAsset.INSTANCE, "beans.xml");
	}

	@BeforeClass
	public static void setLoggingConfiguration() throws IOException {
		LogUtil.loadConfiguration();
	}

	@Inject
	private Customers customers;

	@Inject
	private DB db;

	@Test
	public void testInjection() {
		assertNotNull(db);
		assertNotNull(customers);
	}

	@Test
	public void testMongoDbJsonQueries() {
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
			final DBObject query = (DBObject) JSON
					.parse("{ type : 'database' }");
			final DBObject myDoc = coll.findOne(query);
			assertNotNull(myDoc);
			assertEquals("MongoDB", myDoc.get("name"));
			assertEquals(203, ((BasicDBObject) myDoc.get("info")).get("x"));
		}
		{
			final DBObject query = (DBObject) JSON
					.parse("{ 'info.x' : { $gt : 1 } }");
			final DBObject myDoc = coll.findOne(query);
			assertNotNull(myDoc);
			assertEquals("MongoDB", myDoc.get("name"));
			assertEquals(203, ((BasicDBObject) myDoc.get("info")).get("x"));
		}
		{
			final DBObject query = (DBObject) JSON
					.parse("{ 'info.x' : { $gt : 1 } }");
			final DBObject fields = (DBObject) JSON.parse("{ type : 1 }");
			final DBObject myDoc = coll.findOne(query, fields);
			assertNotNull(myDoc);
			assertNull(myDoc.get("name"));
			assertEquals("database", myDoc.get("type"));
		}
	}

	/**
	 * This is an example based on
	 * http://www.mongodb.org/display/DOCS/Java+Tutorial to see if things work.
	 */
	@Test
	public void testMongoDbTutorialSample() {
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

	@Test
	public void testWithSerializedObject() throws Exception {
		final Customer customer = new Customer();
		customer.setName("Archimedes Trajano");
		customer.setLastRecallTimestamp(new Date());
		customer.setUuid(UUID.randomUUID());

		customers.add(customer);

		final List<Customer> customerList = customers.find("arch");
		assertEquals(1, customerList.size());
		assertEquals("Archimedes Trajano", customerList.get(0).getName());
	}
}
