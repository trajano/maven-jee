package net.trajano.nosql.internal;

import java.util.Collections;
import java.util.List;
import java.util.UUID;

import javax.inject.Inject;

import me.prettyprint.cassandra.serializers.StringSerializer;
import me.prettyprint.cassandra.serializers.UUIDSerializer;
import me.prettyprint.cassandra.service.template.ColumnFamilyResult;
import me.prettyprint.cassandra.service.template.ColumnFamilyTemplate;
import me.prettyprint.cassandra.service.template.ColumnFamilyUpdater;
import me.prettyprint.cassandra.service.template.ThriftColumnFamilyTemplate;
import me.prettyprint.hector.api.Keyspace;
import net.trajano.nosql.Customer;
import net.trajano.nosql.Customers;

/**
 * Represents customers DAO implemented using Apache Cassandra.
 */
public class CassandraCustomers implements Customers {

	/**
	 * "CustomerNames" {@link ColumnFamilyTemplate} from the injected
	 * {@link Keyspace}. The key is actually stored in lowercase.
	 */
	private final ColumnFamilyTemplate<String, String> customerNameTemplate;

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
		customerNameTemplate = new ThriftColumnFamilyTemplate<String, String>(
				keyspace, "customerName", StringSerializer.get(),
				StringSerializer.get());
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

		final ColumnFamilyUpdater<String, String> customerNameUpdater = customerNameTemplate
				.createUpdater(customer.getName().toLowerCase());
		customerNameUpdater.setUUID("uuid", uuid);
		customerNameTemplate.update(customerNameUpdater);
	}

	/**
	 * This will query using exact name match. Unfortunately there's no rich
	 * query capability with Cassandra to allow regular expression or even
	 * substrings. However, the search is case insensitive.
	 * 
	 * @param query
	 *            key
	 * @return list of customers that satisfy the regular expression.
	 */
	@Override
	public List<Customer> find(final String query) {
		final UUID uuid;
		final ColumnFamilyResult<String, String> queryColumns = customerNameTemplate
				.queryColumns(query.toLowerCase());
		if (queryColumns == null) {
			uuid = null;
		} else {
			uuid = queryColumns.getUUID("uuid");
		}

		if (uuid != null) {
			final ColumnFamilyResult<UUID, String> columns = customerTemplate
					.queryColumns(uuid);
			final Customer customer = new Customer();
			customer.setUuid(columns.getUUID("uuid"));
			customer.setName(columns.getString("name"));
			customer.setLastRecallTimestamp(columns
					.getDate("lastRecallTimestamp"));
			return Collections.singletonList(customer);
		} else {
			return Collections.emptyList();
		}
	}
}
