package net.trajano.nosql;

import java.util.Date;
import java.util.UUID;

/**
 * This is an object that would be stored in the MongoDB.
 */
public class Customer {
	/**
	 * This represents a timestamp. It is stored as a long in order to prevent
	 * DST and other timezone issues.
	 */
	private long lastRecallTimestamp;

	/**
	 * Name.
	 */
	private String name;

	/**
	 * UUID.
	 */
	private UUID uuid;

	/**
	 * Returns the last recall timestamp. The value is converted from a long
	 * value.
	 * 
	 * @return last recall timestamp.
	 */
	public Date getLastRecallTimestamp() {
		return new Date(lastRecallTimestamp);
	}

	public String getName() {
		return name;
	}

	public UUID getUuid() {
		return uuid;
	}

	public void setLastRecallTimestamp(final Date lastRecallTimestamp) {
		this.lastRecallTimestamp = lastRecallTimestamp.getTime();
	}

	public void setName(final String name) {
		this.name = name;
	}

	public void setUuid(final UUID uuid) {
		this.uuid = uuid;
	}
}
