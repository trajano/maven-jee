package net.trajano.servicebus.master;

import scala.concurrent.Future;
import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.util.Timeout;

/**
 * This is a service bus that provides Scala futures that can be used for
 * efficiency. It also exposes the actor system for use with other Akka actor
 * processing and the Master actor.
 */
public interface AkkaActorServiceBus extends ServiceBus {
	<T> Future<T> ask(Class<T> messageClass, Timeout timeout);

	ActorSystem getActorSystem();

	ActorRef getMaster();
}
