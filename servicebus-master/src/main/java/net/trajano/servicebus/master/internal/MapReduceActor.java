package net.trajano.servicebus.master.internal;

import net.trajano.servicebus.master.MapReduceActorProvider;
import akka.actor.Actor;
import akka.actor.ActorRef;
import akka.actor.Props;
import akka.actor.UntypedActor;
import akka.actor.UntypedActorFactory;

public class MapReduceActor<A, D, R> extends UntypedActor {

	/**
	 * Accumulator.
	 */
	private A accumulator;
	private int mappedCount;
	private final MapReduceActorProvider<A, D, R> provider;

	public MapReduceActor(final MapReduceActorProvider<A, D, R> provider) {
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
	@SuppressWarnings("unchecked")
	@Override
	public void onReceive(final Object message) throws Exception {
		if (isProviderHandleMessage(message)) {
			accumulator = provider.initializeAccumulator(message);
			final ActorRef worker = getContext().actorOf(
					new Props(new UntypedActorFactory() {
						private static final long serialVersionUID = 1L;

						@Override
						public Actor create() {
							return new MapReduceWorkerActor<A, D, R>(provider);
						}
					}));
			mappedCount = provider.map(message, worker);
			if (mappedCount == 0) {
				getContext().parent().tell(accumulator);
				getContext().stop(getSelf());
			}
		} else if (message instanceof MapReduceIntermediateResult) {
			provider.reduce(accumulator,
					(R) ((MapReduceIntermediateResult) message).getResult());
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
