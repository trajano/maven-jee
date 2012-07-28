package net.trajano.nosql.cassandra.test;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;
import me.prettyprint.cassandra.model.CqlQuery;
import me.prettyprint.cassandra.model.CqlRows;
import me.prettyprint.cassandra.serializers.LongSerializer;
import me.prettyprint.cassandra.serializers.StringSerializer;
import me.prettyprint.hector.api.query.QueryResult;

import org.cassandraunit.AbstractCassandraUnit4TestCase;
import org.cassandraunit.dataset.DataSet;
import org.cassandraunit.dataset.json.ClassPathJsonDataSet;
import org.cassandraunit.utils.EmbeddedCassandraServerHelper;
import org.junit.After;
import org.junit.Test;

/**
 * Embedding Cassandra test. Although Hector has a BaseEmbededServerSetupTest,
 * CassandraUnit has extra functions like dataset loading that make testing a
 * bit easier.
 * 
 * @see <a
 *      href="https://github.com/jsevellec/cassandra-unit/wiki/How-to-integrate-it-in-your-project">cassandra-unit</a>
 * @author Archimedes Trajano
 * 
 */
public class EmbeddedCassandraTest extends AbstractCassandraUnit4TestCase {

	@Override
	public DataSet getDataSet() {
		return new ClassPathJsonDataSet("extendedDataSet.json");
	}

	@After
	public void tearDown() throws Exception {
		EmbeddedCassandraServerHelper.cleanEmbeddedCassandra();
	}

	/**
	 * Use Hector as the client library but use CQL queries for readability.
	 * 
	 * @throws Exception
	 */
	@Test
	public void testCql() throws Exception {
		final CqlQuery<String, String, Long> cqlQuery = new CqlQuery<String, String, Long>(
				getKeyspace(), StringSerializer.get(), StringSerializer.get(),
				LongSerializer.get());
		cqlQuery.setQuery("select * from beautifulColumnFamilyName");

		final QueryResult<CqlRows<String, String, Long>> result = cqlQuery
				.execute();

		final CqlRows<String, String, Long> rows = result.get();
		assertEquals(2, rows.getCount());
		assertEquals(2, rows.getList().size());

	}

	/**
	 * Assert data structure as seen from JSON.
	 * 
	 * @throws Exception
	 */
	@Test
	public void testJSONData() throws Exception {
		assertEquals(5, getDataSet().getColumnFamilies().size());
		assertEquals("beautifulColumnFamilyName", getDataSet()
				.getColumnFamilies().get(0).getName());
		assertEquals(2, getDataSet().getColumnFamilies().get(0).getRows()
				.size());
	}

	/**
	 * Verify connectivity.
	 * 
	 * @throws Exception
	 */
	@Test
	public void testKeyspace() throws Exception {
		assertNotNull(getKeyspace());
		assertEquals("otherKeyspaceName", getKeyspace().getKeyspaceName());
	}
}
