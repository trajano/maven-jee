package net.trajano.maven_jee6.derby_database;

import org.junit.Test;

public class DerbyDatabaseTest {
	@Test
	public void testStartStop() throws Exception {
		final DerbyDatabase derbyDatabase = new DerbyDatabase();
		derbyDatabase.init();
		derbyDatabase.destroy();
	}
}
