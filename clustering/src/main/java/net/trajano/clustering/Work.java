package net.trajano.clustering;

public class Work {
	private final int numberOfElements;
	private final int start;

	public Work(final int start, final int numberOfElements) {
		this.start = start;
		this.numberOfElements = numberOfElements;
	}

	public int getNumberOfElements() {
		return numberOfElements;
	}

	public int getStart() {
		return start;
	}
}
