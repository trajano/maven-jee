package net.trajano.servicebus.master;

public class MapReduceResult {
	private final Object result;

	public MapReduceResult(final Object result) {
		System.out.println(result);
		this.result = result;
	}

	public Object getResult() {
		return result;
	}
}
