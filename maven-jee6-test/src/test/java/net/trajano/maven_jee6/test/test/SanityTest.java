package net.trajano.maven_jee6.test.test;

import static org.easymock.EasyMock.anyObject;
import static org.easymock.EasyMock.createNiceMock;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.replay;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.sql.Connection;

import org.junit.Test;

/**
 * This is a do-nothing test case to ensure that the test framework and basic
 * tooling works.
 * 
 * @author trajano
 * 
 */
public class SanityTest {
	@Test
	public void doNothing() {
		assertTrue(true);
	}

	@Test
	public void easyMock() throws Exception {
		final Connection connection = createNiceMock(Connection.class);
		expect(connection.getClientInfo(anyObject(String.class))).andReturn(
				"hello");
		replay(connection);
		assertEquals("hello", connection.getClientInfo("foo"));
	}
}
