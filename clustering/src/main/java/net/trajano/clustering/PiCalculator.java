package net.trajano.clustering;

public class PiCalculator {
	public static double calculatePiFor(final int start,
			final int numberOfElements) {
		double accumulator = 0.0;
		for (int i = start * numberOfElements; i <= (start + 1)
				* numberOfElements - 1; i++) {
			accumulator += 4.0 * (1 - i % 2 * 2) / (2 * i + 1);
		}
		return accumulator;
	}

	/**
	 * Prevent instantiation of utility class.
	 */
	private PiCalculator() {

	}
}
