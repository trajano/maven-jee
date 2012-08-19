package net.trajano.clustering.internal;

import net.trajano.clustering.PiCalculator;
import net.trajano.clustering.Result;
import net.trajano.clustering.Work;
import akka.actor.UntypedActor;

public class AkkaWorker extends UntypedActor {
	@Override
	public void onReceive(final Object message) {
		if (message instanceof Work) {
			final Work work = (Work) message;
			final double result = PiCalculator.calculatePiFor(work.getStart(),
					work.getNumberOfElements());
			getSender().tell(new Result(result), getSelf());
		} else {
			unhandled(message);
		}
	}
}