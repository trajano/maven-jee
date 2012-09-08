package net.trajano.servicebus.wordcounter;

import java.util.HashMap;
import java.util.Map;

public class MultiAccumulator {
	private int count = 0;
	private final Map<String, Integer> resourceCountMap = new HashMap<>();

	public void add(final String resourceName, final int n) {
		resourceCountMap.put(resourceName, n);
		count += n;
	}

	public int getCount() {
		return count;
	}

	public int getCount(final String resource) {
		return resourceCountMap.get(resource);
	}

	@Override
	public String toString() {
		return String.valueOf(count);
	}
}