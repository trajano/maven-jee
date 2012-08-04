package net.trajano.nosql.hbase.test;

import static org.junit.Assert.assertEquals;

import java.util.Date;
import java.util.List;
import java.util.UUID;

import net.trajano.nosql.Customer;
import net.trajano.nosql.Customers;
import net.trajano.nosql.internal.HBaseCustomers;

import org.apache.hadoop.hbase.HBaseTestingUtility;
import org.apache.hadoop.hbase.client.HTable;
import org.apache.hadoop.hbase.util.Bytes;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

public class CustomerTest {
	private static Customers customers;
	private static HBaseTestingUtility utility;

	@BeforeClass
	public static void setUp() throws Exception {
		utility = new HBaseTestingUtility();
		utility.startMiniCluster();
		final HTable table = utility
				.createTable(
						Bytes.toBytes("customerTable"),
						Bytes.toByteArrays(new String[] { "customer",
								"customerName" }));

		customers = new HBaseCustomers(table);
	}

	@AfterClass
	public static void tearDown() throws Exception {
		utility.shutdownMiniCluster();
	}

	@Test
	public void testCustomerInsertSearch() {
		final Customer customer = new Customer();
		customer.setName("Archimedes Trajano");
		customer.setLastRecallTimestamp(new Date());
		customer.setUuid(UUID.randomUUID());

		customers.add(customer);

		{
			final List<Customer> customerList = customers
					.find("Archimedes Trajano");
			assertEquals(1, customerList.size());
			assertEquals("Archimedes Trajano", customerList.get(0).getName());
		}
		{
			final List<Customer> customerList = customers
					.find("archimedes trajano");
			assertEquals(1, customerList.size());
			assertEquals("Archimedes Trajano", customerList.get(0).getName());
		}
		{
			final List<Customer> customerList = customers
					.find("archimedes TRAJANO");
			assertEquals(1, customerList.size());
			assertEquals("Archimedes Trajano", customerList.get(0).getName());
		}
	}
}
