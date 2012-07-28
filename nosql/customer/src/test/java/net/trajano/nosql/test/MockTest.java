package net.trajano.nosql.test;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertTrue;
import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.expectLastCall;
import static org.easymock.EasyMock.replay;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import net.trajano.nosql.Customer;
import net.trajano.nosql.Customers;

import org.junit.Test;

public class MockTest {
	@Test
	public void mock() {
		final Customer customer = new Customer();
		customer.setName("archie");
		final Customers customers = createMock(Customers.class);
		customers.add(customer);
		expectLastCall();
		expect(customers.find("foo")).andReturn(new ArrayList<Customer>());
		expect(customers.find("rch")).andReturn(
				Collections.singletonList(customer));
		replay(customers);

		customers.add(customer);
		{
			final List<Customer> list = customers.find("foo");
			assertTrue(list.isEmpty());
		}
		{
			final List<Customer> list = customers.find("rch");
			assertEquals(1, list.size());
			assertEquals(customer, list.get(0));
		}
	}
}
