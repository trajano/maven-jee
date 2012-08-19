package net.trajano.clustering.internal;

import net.trajano.clustering.Calculate;
import net.trajano.clustering.FinalResult;
import net.trajano.clustering.PiApproximation;
import net.trajano.clustering.Result;
import net.trajano.clustering.Work;
import akka.actor.ActorRef;
import akka.actor.Props;
import akka.actor.UntypedActor;
import akka.routing.RoundRobinRouter;

public class AkkaMaster extends UntypedActor {
	private ActorRef listener;
	private final int nrOfElements;

	private final int nrOfMessages;
	private int nrOfResults;
	private double pi;

	private final ActorRef workerRouter;

	public AkkaMaster(final int nrOfWorkers, final int nrOfMessages,
			final int nrOfElements) {
		this.nrOfMessages = nrOfMessages;
		this.nrOfElements = nrOfElements;

		workerRouter = getContext().actorOf(
				new Props(AkkaWorker.class).withRouter(new RoundRobinRouter(
						nrOfWorkers)), "workerRouter");
	}

	@Override
	public void onReceive(final Object message) {
		if (message instanceof Calculate) {
			for (int start = 0; start < nrOfMessages; ++start) {
				workerRouter.tell(new Work(start, nrOfElements), getSelf());
			}
		} else if (message instanceof Result) {
			final Result result = (Result) message;
			pi += result.getValue();
			nrOfResults += 1;
			if (nrOfResults == nrOfMessages) {
				listener.tell(new PiApproximation(pi), getSelf());
				getContext().stop(getSelf());
			}
		} else if (message instanceof FinalResult) {
			listener = getSender();
		} else {
			unhandled(message);
		}
	}
}
