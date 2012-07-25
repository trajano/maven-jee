package net.trajano.clustering.internal;

import java.io.Serializable;
import java.util.concurrent.Callable;

import net.trajano.clustering.PiCalculator;
import net.trajano.clustering.Result;

public class HazelcastWorker implements Callable<Result>, Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private final int nrOfElements;

	private final int start;

	public HazelcastWorker(final int start, final int nrOfElements) {
		this.start = start;
		this.nrOfElements = nrOfElements;
	}

	@Override
	public Result call() throws Exception {
		return new Result(PiCalculator.calculatePiFor(start, nrOfElements));
	}
}
