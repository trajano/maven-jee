package net.trajano.servicebus.wordcounter;

public class Accumulator {
	private int count = 0;

	public void add(final int n) {
		count += n;
	}

	public int getCount() {
		return count;
	}

	@Override
	public String toString() {
		return String.valueOf(count);
	}
}