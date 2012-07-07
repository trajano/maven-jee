package net.trajano.nosql;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import javax.inject.Inject;

import com.google.gson.Gson;
import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;
import com.mongodb.util.JSON;

/**
 * Created with IntelliJ IDEA. User: trajano Date: 12-05-29 Time: 12:35 PM To
 * change this template use File | Settings | File Templates.
 */
public class Customers {
	/**
	 * "Customers" {@link DBCollection} from the injected {@link DB}.
	 */
	private final DBCollection collection;

	/**
	 * Constructs the DAO.
	 * 
	 * @param db
	 *            MongoDB instance.
	 */
	@Inject
	public Customers(final DB db) {
		collection = db.getCollection("Customers");
	}

	/**
	 * Add the Customer to the database.
	 * 
	 * @param customer
	 *            customer
	 */
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

}
