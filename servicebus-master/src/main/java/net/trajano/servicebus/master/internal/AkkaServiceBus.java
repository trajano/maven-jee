package net.trajano.servicebus.master.internal;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeoutException;

import net.trajano.servicebus.master.ActorProvider;
import net.trajano.servicebus.master.AkkaActorServiceBus;
import scala.concurrent.Await;
import scala.concurrent.Future;
import scala.concurrent.util.Duration;
import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import akka.pattern.Patterns;
import akka.util.Timeout;

public class AkkaServiceBus implements AkkaActorServiceBus {

	private final ActorSystem actorSystem;
	private final ActorRef master;

	public AkkaServiceBus(final ActorSystem actorSystem) {
		this.actorSystem = actorSystem;
		master = actorSystem.actorOf(new Props(MasterActor.class));
	}

	@Override
	@SuppressWarnings("unchecked")
	public <T> java.util.concurrent.Future<T> ask(final Class<T> messageClass,
			final ExecutorService executor, final long timeout) {
		return executor.submit(new CallableScalaFuture<>((Future<T>) Patterns
				.ask(master, new Asked(messageClass), timeout)));
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T> Future<T> ask(final Class<T> messageClass, final Timeout timeout) {
		return (Future<T>) Patterns.ask(master, new Asked(messageClass),
				timeout);
	}

	public void deregisterActorProvider(final ActorProvider provider)
			throws TimeoutException {
		if (provider != null) {
			final Future<Object> future = Patterns.ask(master,
					new ActorDeregistration(provider), 1000);
			Await.ready(future, Duration.Inf());
		}
	}

	@Override
	public ActorSystem getActorSystem() {
		return actorSystem;
	}

	@Override
	public ActorRef getMaster() {
		return master;
	}

	public void registerActorProvider(final ActorProvider provider)
			throws TimeoutException {
		if (provider != null) {
			final Future<Object> future = Patterns.ask(master,
					new ActorRegistration(provider), 1000);
			Await.ready(future, Duration.Inf());
		}
	}

	@Override
	public void tell(final Object message) {
		master.tell(message);
	}
}
