package net.trajano.nosql.internal;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import javax.inject.Inject;

import net.trajano.nosql.Customer;
import net.trajano.nosql.Customers;

import com.google.gson.Gson;
import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;
import com.mongodb.util.JSON;

/**
 * Represents customers DAO implemented using MongoDB.
 */
public class MongoDbCustomers implements Customers {
	/**
	 * "Customers" {@link DBCollection} from the injected {@link DB}.
	 */
	private final DBCollection collection;

	/**
	 * Constructs the DAO. Sets the collection and ensures the indices are set
	 * on the collection.
	 * 
	 * @param db
	 *            MongoDB instance.
	 */
	@Inject
	public MongoDbCustomers(final DB db) {
		collection = db.getCollection("Customers");
		collection.ensureIndex("name");
		collection.ensureIndex("uuid");
	}

	/**
	 * Add the Customer to the database.
	 * 
	 * @param customer
	 *            customer
	 */
	@Override
	public void add(final Customer customer) {
		final Gson gson = new Gson();
		collection.insert((DBObject) JSON.parse(gson.toJson(customer)));
	}

	/**
	 * Find a customer using a regular expression.
	 * 
	 * @param customerRegexp
	 *            regular expression
	 * @return list of customers that satisfy the regular expression.
	 */
	@Override
	public List<Customer> find(final String customerRegexp) {
		final Gson gson = new Gson();
		final BasicDBObject query = new BasicDBObject();
		query.append("name",
				Pattern.compile(customerRegexp, Pattern.CASE_INSENSITIVE));
		final List<Customer> ret = new ArrayList<Customer>();
		for (final DBObject dbObject : collection.find(query)) {
			ret.add(gson.fromJson(dbObject.toString(), Customer.class));
		}
		return ret;
	}

	/**
	 * Provides the collection storing the data. This is used for testing.
	 * 
	 * @return collection.
	 */
	public DBCollection getDBCollection() {
		return collection;
	}

}
