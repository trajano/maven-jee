package net.trajano.servicebus.master.internal;

import net.trajano.servicebus.master.MapReduceActorProvider;
import akka.actor.UntypedActor;

public class MapReduceWorkerActor extends UntypedActor {
	private final MapReduceActorProvider provider;

	public MapReduceWorkerActor(final MapReduceActorProvider provider) {
		this.provider = provider;
	}

	@Override
	public void onReceive(final Object message) throws Exception {
		getContext().parent().tell(
				new MapReduceIntermediateResult(provider.process(message)));
	}

}
