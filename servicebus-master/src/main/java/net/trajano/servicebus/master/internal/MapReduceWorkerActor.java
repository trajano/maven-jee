package net.trajano.servicebus.master.internal;

import net.trajano.servicebus.master.MapReduceActorProvider;
import akka.actor.UntypedActor;

public class MapReduceWorkerActor<A, D, R> extends UntypedActor {
	private final MapReduceActorProvider<A, D, R> provider;

	public MapReduceWorkerActor(final MapReduceActorProvider<A, D, R> provider) {
		this.provider = provider;
	}

	@SuppressWarnings("unchecked")
	@Override
	public void onReceive(final Object message) throws Exception {
		getContext().parent().tell(
				new MapReduceIntermediateResult(provider.process((D) message)));
	}

}
