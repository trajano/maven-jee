package net.trajano.servicebus.master.internal;

import net.trajano.servicebus.master.MapReduceActorProvider;
import net.trajano.servicebus.master.MapReduceWork;
import akka.actor.UntypedActor;

public class MapReduceActor extends UntypedActor {

	/**
	 * Accumulator.
	 */
	private Object accumulator;
	private int mappedCount;
	private final MapReduceActorProvider provider;

	public MapReduceActor(final MapReduceActorProvider provider) {
		this.provider = provider;
	}

	private boolean isProviderHandleMessage(final Object message) {
		for (final Class<?> messageClass : provider.messageClassesHandled()) {
			if (messageClass.isInstance(message)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * There are three supporting classes for this one: a mapper, a processor
	 * and a reducer.
	 */
	@Override
	public void onReceive(final Object message) throws Exception {
		if (isProviderHandleMessage(message)) {
			accumulator = provider.initializeAccumulator(message);
			mappedCount = provider.map(message, getSelf());
			if (mappedCount == 0) {
				getContext().parent().tell(accumulator);
			}
			// if the message is for requesting a future then...
		} else if (message instanceof MapReduceWork) {
			getSelf().tell(
					new MapReduceIntermediateResult(provider
							.process(((MapReduceWork) message).getMessage())));
		} else if (message instanceof MapReduceIntermediateResult) {
			provider.reduce(accumulator,
					((MapReduceIntermediateResult) message).getResult());
			--mappedCount;
			if (mappedCount == 0) {
				getContext().parent().tell(accumulator);
				getContext().stop(getSelf());
			}
		} else {
			unhandled(message);
		}
	}
}
