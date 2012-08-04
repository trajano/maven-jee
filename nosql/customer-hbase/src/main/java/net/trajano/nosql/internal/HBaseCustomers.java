package net.trajano.nosql.internal;

import java.io.IOException;
import java.util.Collections;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

import javax.inject.Inject;

import net.trajano.nosql.Customer;
import net.trajano.nosql.Customers;

import org.apache.hadoop.hbase.client.Get;
import org.apache.hadoop.hbase.client.HTable;
import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.util.Bytes;

public class HBaseCustomers implements Customers {
	/**
	 * Column family for data.
	 */
	private static final byte[] CF_CUSTOMER = Bytes.toBytes("customer");

	/**
	 * "lastRecallTimestamp" field for "customer" family.
	 */
	private static final byte[] CF_CUSTOMER_FIELD_LAST_RECALL_TIMESTAMP = Bytes
			.toBytes("lastRecallTimestamp");

	/**
	 * "name" field for "customer" family.
	 */
	private static final byte[] CF_CUSTOMER_FIELD_NAME = Bytes.toBytes("name");

	/**
	 * Column family for name searches.
	 */
	private static final byte[] CF_CUSTOMER_NAME = Bytes
			.toBytes("customerName");

	/**
	 * "uuid" field of customer name family.
	 */
	private static final byte[] CF_CUSTOMER_NAME_FIELD_UUID = Bytes
			.toBytes("uuid");

	/**
	 * Injected table connection.
	 */
	private final HTable table;

	/**
	 * Constructs the DAO.
	 * 
	 * @param table
	 *            HBase table connection.
	 */
	@Inject
	public HBaseCustomers(final HTable table) {
		this.table = table;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void add(final Customer customer) {
		if (customer.getUuid() == null) {
			customer.setUuid(UUID.randomUUID());
		}
		final List<Put> puts = new LinkedList<Put>();
		final byte[] uuidBytes = Bytes.add(
				Bytes.toBytes(customer.getUuid().getMostSignificantBits()),
				Bytes.toBytes(customer.getUuid().getLeastSignificantBits()));
		{
			final Put dataPut = new Put(uuidBytes);
			dataPut.add(CF_CUSTOMER, CF_CUSTOMER_FIELD_NAME,
					Bytes.toBytes(customer.getName()));
			dataPut.add(CF_CUSTOMER, CF_CUSTOMER_FIELD_LAST_RECALL_TIMESTAMP,
					Bytes.toBytes(customer.getLastRecallTimestamp().getTime()));
			puts.add(dataPut);
		}
		for (int i = 1; i < customer.getName().length() + 1; ++i) {
			final String key = customer.getName().substring(0, i).toLowerCase();
			final Put namePut = new Put(Bytes.toBytes(key));
			namePut.add(CF_CUSTOMER_NAME, CF_CUSTOMER_NAME_FIELD_UUID,
					uuidBytes);
			puts.add(namePut);
		}

		try {
			table.put(puts);
		} catch (final IOException e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * This will query using case-insensitive exact match.
	 * 
	 * @param query
	 *            key
	 * @return list of customers that satisfy the regular expression.
	 */
	@Override
	public List<Customer> find(final String query) {
		final Get nameGet = new Get(Bytes.toBytes(query.toLowerCase()));
		nameGet.addColumn(CF_CUSTOMER_NAME, CF_CUSTOMER_NAME_FIELD_UUID);
		try {
			final Result result = table.get(nameGet);
			if (result.isEmpty()) {
				return Collections.emptyList();
			}

			final byte[] uuidBytes = result.getValue(CF_CUSTOMER_NAME,
					CF_CUSTOMER_NAME_FIELD_UUID);
			final UUID uuid = new UUID(Bytes.toLong(uuidBytes, 0,
					Bytes.SIZEOF_LONG), Bytes.toLong(uuidBytes,
					Bytes.SIZEOF_LONG, Bytes.SIZEOF_LONG));
			final Get recordGet = new Get(uuidBytes);
			recordGet.addColumn(CF_CUSTOMER, CF_CUSTOMER_FIELD_NAME);
			recordGet.addColumn(CF_CUSTOMER,
					CF_CUSTOMER_FIELD_LAST_RECALL_TIMESTAMP);
			final Result recordResult = table.get(recordGet);
			if (recordResult.isEmpty()) {
				return Collections.emptyList();
			}

			final Customer customer = new Customer();
			customer.setUuid(uuid);
			customer.setName(Bytes.toString(recordResult.getValue(CF_CUSTOMER,
					CF_CUSTOMER_FIELD_NAME)));
			customer.setLastRecallTimestamp(new Date(Bytes.toLong(recordResult
					.getValue(CF_CUSTOMER,
							CF_CUSTOMER_FIELD_LAST_RECALL_TIMESTAMP))));
			return Collections.singletonList(customer);
		} catch (final IOException e) {
			throw new RuntimeException(e);
		}
	}
}
