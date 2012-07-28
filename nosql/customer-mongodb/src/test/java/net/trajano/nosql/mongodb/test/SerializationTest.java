package net.trajano.nosql.mongodb.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.Date;
import java.util.UUID;

import net.trajano.nosql.Customer;

import org.junit.Test;

import com.google.gson.Gson;

/**
 * Test to use GSON as the JSON serialization engine.
 */
public class SerializationTest {
	@Test
	public void testCustomer() {
		final Customer customer = new Customer();
		customer.setName("Archimedes Trajano");
		customer.setLastRecallTimestamp(new Date());
		customer.setUuid(UUID.randomUUID());
		final Gson gson = new Gson();
		final String jsonString = gson.toJson(customer);
		assertTrue(jsonString.contains("\"name\":\"Archimedes Trajano\""));
		assertTrue(jsonString.contains("\"uuid\":\"" + customer.getUuid()
				+ "\""));
		assertTrue(
				"missing timestamp in " + jsonString,
				jsonString.contains("\"lastRecallTimestamp\":"
						+ customer.getLastRecallTimestamp().getTime()));
		final Customer parsed = gson.fromJson(jsonString, Customer.class);
		assertEquals(jsonString, gson.toJson(parsed));
	}
}
