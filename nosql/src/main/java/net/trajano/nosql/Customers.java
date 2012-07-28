package net.trajano.nosql;

import java.util.List;

/**
 * Represents customers DAO service.
 */
public interface Customers {

	/**
	 * Add the Customer to the database.
	 * 
	 * @param customer
	 *            customer
	 */
	void add(Customer customer);

	/**
	 * Find a customer using a search key. The implementation determines what
	 * kind of search it is capable of (e.g. regular expression for MongoDB,
	 * exact case-insensitive string for Cassandara).
	 * 
	 * @param query
	 *            search query
	 * @return list of customers that satisfy the query.
	 */
	List<Customer> find(final String query);
}
