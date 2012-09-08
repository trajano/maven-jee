package net.trajano.servicebus.master.test;

import net.trajano.servicebus.master.ActorProvider;
import net.trajano.servicebus.master.internal.ActorDeregistration;
import net.trajano.servicebus.master.internal.ActorRegistration;
import net.trajano.servicebus.master.internal.AkkaServiceBus;
import net.trajano.servicebus.master.internal.MasterActor;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import akka.testkit.TestProbe;

public class ActorSystemTest {
	private ActorSystem system;
	private TestProbe testProbe;

	@Before
	public void setUp() {
		system = ActorSystem.create(getClass().getSimpleName());
		testProbe = TestProbe.apply(system);
	}

	@After
	public void tearDown() throws Exception {
		system.shutdown();
		system.awaitTermination();
	}

	@Test
	public void testExceptionProvider() throws Exception {
		system.eventStream().stopDefaultLoggers();
		final AkkaServiceBus serviceBus = new AkkaServiceBus(system);
		final ActorProvider provider = new ExceptionalActorProvider();
		serviceBus.registerActorProvider(provider);
		serviceBus.tell("hello");
		serviceBus.deregisterActorProvider(provider);
		testProbe.expectNoMsg();
	}

	/**
	 * Tests using a simple provider.
	 * 
	 * @throws Exception
	 */
	@Test
	public void testSimpleProvider() throws Exception {
		final ActorRef master = system.actorOf(new Props(MasterActor.class),
				"master");
		final JavaActorProvider provider = new JavaActorProvider();
		master.tell(new ActorRegistration(provider));
		master.tell(new JavaActorProvider.Message("hello"));
		master.tell(new ActorDeregistration(provider));
	}

	/**
	 * Test using a bundle activator.
	 * 
	 * @throws Exception
	 */
	@Test
	public void testWithActivator() throws Exception {
		final AkkaServiceBus serviceBus = new AkkaServiceBus(system);
		final JavaActorProvider provider = new JavaActorProvider();
		serviceBus.registerActorProvider(provider);
		serviceBus.tell(new JavaActorProvider.Message("hello"));
		serviceBus.deregisterActorProvider(provider);
	}

	/**
	 * Test when there are no providers. Should still work.
	 * 
	 * @throws Exception
	 */
	@Test
	public void testWithNoProvider() throws Exception {
		final ActorRef master = system.actorOf(new Props(MasterActor.class),
				"master");
		master.tell(new JavaActorProvider.Message("hello"));
	}

}
