package net.trajano.servicebus.master.test;

import net.trajano.servicebus.master.ActorRegistration;
import net.trajano.servicebus.master.MasterActor;

import org.junit.Test;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;

public class ActorSystemTest {
	@Test
	public void runSystem() throws Exception {
		final ActorSystem system = ActorSystem.create("PiSystem");
		final ActorRef master = system.actorOf(new Props(MasterActor.class),
				"master");
		master.tell(new ActorRegistration(new JavaActorProvider()));
	}
}
