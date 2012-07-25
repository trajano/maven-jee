package net.trajano.clustering.test;

import net.trajano.clustering.Calculate;
import net.trajano.clustering.FinalResult;
import net.trajano.clustering.PiApproximation;
import net.trajano.clustering.internal.AkkaMaster;

import org.junit.Assert;
import org.junit.Test;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import akka.actor.UntypedActor;
import akka.actor.UntypedActorFactory;
import akka.dispatch.Await;
import akka.dispatch.Future;
import akka.pattern.Patterns;
import akka.util.Duration;
import akka.util.Timeout;

public class AkkaTest {
	@Test
	public void test() throws Exception {
		final ActorSystem system = ActorSystem.create("PiSystem");
		final ActorRef master = system.actorOf(new Props(
				new UntypedActorFactory() {
					/**
					 * 
					 */
					private static final long serialVersionUID = 1L;

					@Override
					public UntypedActor create() {
						return new AkkaMaster(4, 10000, 10000);
					}
				}), "master");
		final Timeout timeout = new Timeout(Duration.parse("5 seconds"));
		final Future<Object> future = Patterns.ask(master, new FinalResult(),
				timeout);
		master.tell(new Calculate());
		final PiApproximation result = (PiApproximation) Await.result(future,
				timeout.duration());
		Assert.assertEquals(Math.PI, result.getPi(), 0.0001);
		system.shutdown();
	}
}
