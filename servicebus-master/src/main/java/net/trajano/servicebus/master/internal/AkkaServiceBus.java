package net.trajano.servicebus.master.internal;

import net.trajano.servicebus.master.ActorProvider;
import net.trajano.servicebus.master.ServiceBus;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

import scala.concurrent.Future;
import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import akka.osgi.ActorSystemActivator;
import akka.pattern.Patterns;
import akka.util.Timeout;

public class AkkaServiceBus extends ActorSystemActivator implements ServiceBus,
		BundleActivator {

	private ActorRef master;

	@SuppressWarnings("unchecked")
	@Override
	public <T> Future<T> ask(final Class<T> messageClass, final Timeout timeout) {
		return (Future<T>) Patterns.ask(master, new Asked(messageClass),
				timeout);
	}

	/**
	 * Ask system given this message, tell me the result.
	 */

	@Override
	public void configure(final BundleContext bundleContext,
			final ActorSystem actorSystem) {
		master = actorSystem.actorOf(new Props(MasterActor.class));
		registerService(bundleContext, actorSystem);
	}

	public void deregisterActorProvider(final ActorProvider provider) {
		master.tell(new ActorDeregistration(provider));
	}

	public ActorRef getMaster() {
		return master;
	}

	public void registerActorProvider(final ActorProvider provider) {
		master.tell(new ActorRegistration(provider));
	}

	@Override
	public void tell(final Object message) {
		master.tell(message);
	}

}
