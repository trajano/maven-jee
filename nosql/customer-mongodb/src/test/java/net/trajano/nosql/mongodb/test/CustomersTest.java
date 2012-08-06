package net.trajano.nosql.mongodb.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.io.IOException;
import java.io.InputStream;
import java.util.Date;
import java.util.List;
import java.util.Scanner;
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

import com.mongodb.DBCollection;
import com.mongodb.DBObject;
import com.mongodb.util.JSON;

@RunWith(Arquillian.class)
public class CustomersTest {
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

	private void importCollection(final DBCollection collection,
			final InputStream jsonStream) {
		@SuppressWarnings("unchecked")
		final List<DBObject> list = (List<DBObject>) JSON.parse(new Scanner(
				jsonStream).useDelimiter("\\A").next());
		collection.insert(list);
	}

	@Test
	public void testAddThenSearchCustomer() throws Exception {
		final Customer customer = new Customer();
		customer.setName("Archimedes Trajano");
		customer.setLastRecallTimestamp(new Date());
		customer.setUuid(UUID.randomUUID());

		customers.add(customer);

		final List<Customer> customerList = customers.find("arch");
		assertEquals(1, customerList.size());
		assertEquals("Archimedes Trajano", customerList.get(0).getName());
	}

	@Test
	public void testImportThenSearchCustomer() throws Exception {
		importCollection(((MongoDbCustomers) customers).getDBCollection(),
				Thread.currentThread().getContextClassLoader()
						.getResourceAsStream("customers.json"));
		final List<Customer> customerList = customers.find("arch");
		assertEquals(1, customerList.size());
		assertEquals("Archimedes Trajano", customerList.get(0).getName());
	}

	@Test
	public void testInjection() {
		assertNotNull(customers);
	}
}
