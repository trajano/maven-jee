package net.trajano.nosql.hbase.test;

import org.apache.hadoop.hbase.HBaseTestingUtility;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * This ensures that the setUp and tearDown methods function properly.
 */
public class FrameworkTest {
	private static HBaseTestingUtility utility;

	@BeforeClass
	public static void setUp() throws Exception {
		utility = new HBaseTestingUtility();
		utility.startMiniCluster();
	}

	@AfterClass
	public static void tearDown() throws Exception {
		utility.shutdownMiniCluster();
	}

	@Test
	public void testFramework() throws Exception {
	}
}
