package net.trajano.clustering;

import java.io.Serializable;

public class PiApproximation implements Serializable {
	/**
	 * Serial version UID.
	 */
	private static final long serialVersionUID = 1L;

	private final double pi;

	public PiApproximation(final double pi) {
		this.pi = pi;
	}

	public double getPi() {
		return pi;
	}
}
