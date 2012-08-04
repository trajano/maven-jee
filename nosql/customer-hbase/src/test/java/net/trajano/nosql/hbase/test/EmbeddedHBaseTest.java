package net.trajano.nosql.hbase.test;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

import org.apache.hadoop.hbase.HBaseTestingUtility;
import org.apache.hadoop.hbase.client.Get;
import org.apache.hadoop.hbase.client.HTable;
import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.util.Bytes;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

public class EmbeddedHBaseTest {
	private static final byte[] CF = Bytes.toBytes("myColumnFamily");
	private static final byte[] CF_COL1 = Bytes.toBytes("myColumn1");
	private static final byte[] CF_COL2 = Bytes.toBytes("myColumn2");
	private static final byte[] TABLE = Bytes.toBytes("myTable");

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
	public void test() throws Exception {
		utility.createTable(TABLE, CF);

		{
			HTable table = null;
			try {
				table = new HTable(utility.getConfiguration(), TABLE);
				{
					final Put put = new Put(Bytes.toBytes("key1"));
					put.add(CF, CF_COL1, Bytes.toBytes("hello"));
					put.add(CF, CF_COL2, Bytes.toBytes("world"));
					table.put(put);
				}
				{
					final Put put = new Put(Bytes.toBytes("key2"));
					put.add(CF, CF_COL1, Bytes.toBytes("hello2"));
					table.put(put);
				}
				{
					final Put put = new Put(Bytes.toBytes("key1"));
					put.add(CF, CF_COL1, Bytes.toBytes("hello1"));
					table.put(put);
				}
			} finally {
				if (table != null) {
					table.close();
				}
			}
		}
		{
			HTable table = null;
			try {
				table = new HTable(utility.getConfiguration(), TABLE);
				{
					final Get get = new Get(Bytes.toBytes("key1"));
					get.addColumn(CF, CF_COL1);
					get.addColumn(CF, CF_COL2);
					final Result result = table.get(get);
					assertArrayEquals(Bytes.toBytes("hello1"),
							result.getValue(CF, CF_COL1));
					assertArrayEquals(Bytes.toBytes("world"),
							result.getValue(CF, CF_COL2));
				}
				{
					final Get get = new Get(Bytes.toBytes("key2"));
					get.addColumn(CF, CF_COL1);
					get.addColumn(CF, CF_COL2);
					final Result result = table.get(get);
					assertArrayEquals(Bytes.toBytes("hello2"),
							result.getValue(CF, CF_COL1));
					assertEquals(null, result.getValue(CF, CF_COL2));
				}
			} finally {
				if (table != null) {
					table.close();
				}
			}
		}
	}
}
