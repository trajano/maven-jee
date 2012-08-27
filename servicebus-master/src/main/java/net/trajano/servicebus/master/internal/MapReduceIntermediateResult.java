package net.trajano.servicebus.master.internal;

public class MapReduceIntermediateResult {
	private final Object result;

	public MapReduceIntermediateResult(final Object result) {
		this.result = result;
	}

	public Object getResult() {
		return result;
	}

}
