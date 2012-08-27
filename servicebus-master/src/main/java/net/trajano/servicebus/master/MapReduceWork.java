package net.trajano.servicebus.master;

public class MapReduceWork {
	private final Object message;

	public MapReduceWork(final Object message) {
		this.message = message;
	}

	public Object getMessage() {
		return message;
	}
}
