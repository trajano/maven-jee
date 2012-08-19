package net.trajano.clustering.internal;

import java.io.Serializable;
import java.util.UUID;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;

import net.trajano.clustering.PiApproximation;
import net.trajano.clustering.Result;

import com.hazelcast.core.Hazelcast;

public class HazelcastMaster implements Callable<PiApproximation>, Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private final int nrOfElements;
	private final int nrOfMessages;

	private double pi = 0.0;

	public HazelcastMaster(final int nrOfMessages, final int nrOfElements) {
		this.nrOfMessages = nrOfMessages;
		this.nrOfElements = nrOfElements;
	}

	@Override
	public PiApproximation call() throws Exception {
		final ExecutorService executor = Hazelcast.getExecutorService(UUID
				.randomUUID().toString());
		@SuppressWarnings("unchecked")
		final Future<Result>[] results = new Future[nrOfMessages];
		for (int i = 0; i < nrOfMessages; ++i) {
			results[i] = executor.submit(new HazelcastWorker(i, nrOfElements));
		}

		for (int i = 0; i < nrOfMessages; ++i) {
			final Result result = results[i].get();
			pi += result.getValue();
		}
		return new PiApproximation(pi);
	}
}
