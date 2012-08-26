package net.trajano.servicebus.master.test;

import static org.mockito.Mockito.mock;
import net.trajano.servicebus.master.internal.ActorDeregistration;
import net.trajano.servicebus.master.internal.ActorRegistration;
import net.trajano.servicebus.master.internal.AkkaServiceBus;
import net.trajano.servicebus.master.internal.MasterActor;

import org.junit.Test;
import org.osgi.framework.BundleContext;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;

public class ActorSystemTest {
	@Test
	public void failrunSystem() throws Exception {
		final ActorSystem system = ActorSystem.create("PiSystem");
		final ActorRef master = system.actorOf(new Props(MasterActor.class),
				"master");
		final JavaActorProvider provider = new JavaActorProvider();
		master.tell(new ActorRegistration(provider));
		master.tell(new JavaActorProvider.Message("hello"));
		master.tell(new ActorDeregistration(provider));
	}

	@Test
	public void runSystem() throws Exception {
		final ActorSystem system = ActorSystem.create("PiSystem");
		final ActorRef master = system.actorOf(new Props(MasterActor.class),
				"master");
		master.tell(new JavaActorProvider.Message("hello"));
	}

	@Test
	public void useActivator() throws Exception {
		final ActorSystem system = ActorSystem.create("PiSystem");
		final AkkaServiceBus serviceBus = new AkkaServiceBus();
		serviceBus.configure(mock(BundleContext.class), system);
		final JavaActorProvider provider = new JavaActorProvider();
		serviceBus.registerActorProvider(provider);
		serviceBus.tell(new JavaActorProvider.Message("hello"));
		serviceBus.deregisterActorProvider(provider);
	}
}
