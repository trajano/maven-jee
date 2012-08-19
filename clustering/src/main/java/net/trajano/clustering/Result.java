package net.trajano.clustering;

import java.io.Serializable;

public class Result implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private final double value;

	public Result(final double value) {
		this.value = value;
	}

	public double getValue() {
		return value;
	}
}
