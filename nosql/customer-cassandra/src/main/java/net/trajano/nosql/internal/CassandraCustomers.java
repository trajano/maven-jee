package net.trajano.nosql.internal;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import javax.inject.Inject;

import me.prettyprint.cassandra.serializers.StringSerializer;
import me.prettyprint.cassandra.serializers.UUIDSerializer;
import me.prettyprint.cassandra.service.template.ColumnFamilyResult;
import me.prettyprint.cassandra.service.template.ColumnFamilyTemplate;
import me.prettyprint.cassandra.service.template.ColumnFamilyUpdater;
import me.prettyprint.cassandra.service.template.SuperCfResult;
import me.prettyprint.cassandra.service.template.SuperCfTemplate;
import me.prettyprint.cassandra.service.template.SuperCfUpdater;
import me.prettyprint.cassandra.service.template.ThriftColumnFamilyTemplate;
import me.prettyprint.cassandra.service.template.ThriftSuperCfTemplate;
import me.prettyprint.hector.api.Keyspace;
import net.trajano.nosql.Customer;
import net.trajano.nosql.Customers;

/**
 * Represents customers DAO implemented using Apache Cassandra.
 */
public class CassandraCustomers implements Customers {

	/**
	 * "CustomerNames" {@link ColumnFamilyTemplate} from the injected
	 * {@link Keyspace}. The key is actually stored in lowercase. This column
	 * family contains all the possible starts-with permutations for a given
	 * name.
	 */
	private final SuperCfTemplate<String, String, String> customerNameTemplate;

	/**
	 * "Customers" {@link ColumnFamilyTemplate} from the injected
	 * {@link Keyspace}.
	 */
	private final ColumnFamilyTemplate<UUID, String> customerTemplate;

	/**
	 * Constructs the DAO.
	 * 
	 * @param keyspace
	 *            Cassandra keyspace.
	 */
	@Inject
	public CassandraCustomers(final Keyspace keyspace) {
		customerTemplate = new ThriftColumnFamilyTemplate<UUID, String>(
				keyspace, "customer", UUIDSerializer.get(),
				StringSerializer.get());
		customerNameTemplate = new ThriftSuperCfTemplate<String, String, String>(
				keyspace, "customerName", StringSerializer.get(),
				StringSerializer.get(), StringSerializer.get());
	}

	/**
	 * Add the Customer to the database.
	 * 
	 * @param customer
	 *            customer
	 */
	@Override
	public void add(final Customer customer) {
		final UUID uuid;
		if (customer.getUuid() == null) {
			uuid = UUID.randomUUID();
		} else {
			uuid = customer.getUuid();
		}
		final ColumnFamilyUpdater<UUID, String> updater = customerTemplate
				.createUpdater(uuid);
		updater.setUUID("uuid", uuid);
		updater.setString("name", customer.getName());
		updater.setDate("lastRecallTimestamp",
				customer.getLastRecallTimestamp());
		customerTemplate.update(updater);

		for (int i = 1; i < customer.getName().length() + 1; ++i) {
			final String key = customer.getName().substring(0, i).toLowerCase();
			final SuperCfUpdater<String, String, String> customerNameUpdater = customerNameTemplate
					.createUpdater(key, customer.getName());
			customerNameUpdater.setUUID("uuid", uuid);
			customerNameTemplate.update(customerNameUpdater);
		}
	}

	/**
	 * This will query using case-insensitive starts-with match. Unfortunately
	 * there's no rich query capability with Cassandra to allow regular
	 * expression or even substrings.
	 * 
	 * @param query
	 *            key
	 * @return list of customers that satisfy the regular expression.
	 */
	@Override
	public List<Customer> find(final String query) {
		final SuperCfResult<String, String, String> queryColumns = customerNameTemplate
				.querySuperColumns(query.toLowerCase());
		if (queryColumns == null) {
			return Collections.emptyList();
		}

		final List<Customer> ret = new ArrayList<Customer>();
		for (final String columnName : queryColumns.getSuperColumns()) {
			final UUID uuid = queryColumns.getUUID(columnName, "uuid");
			if (uuid == null) {
				System.out.println(String.format("null uuid at [%s][%s]",
						query, columnName));
				continue;
			}
			final ColumnFamilyResult<UUID, String> columns = customerTemplate
					.queryColumns(uuid);
			final Customer customer = new Customer();
			customer.setUuid(columns.getUUID("uuid"));
			customer.setName(columns.getString("name"));
			customer.setLastRecallTimestamp(columns
					.getDate("lastRecallTimestamp"));
			ret.add(customer);
		}
		return ret;
	}
}
