package net.trajano.servicebus.master;

import scala.concurrent.Await;
import scala.concurrent.Future;
import akka.pattern.Patterns;
import akka.util.Timeout;

public interface ServiceBus {
	/**
	 * Asks the service bus to provide a Future that can be waited on when the
	 * message is sent to the master. Use {@link Await} to wait for the results.
	 * This method was added to prevent having to expose the master actor that
	 * is required by {@link Patterns#ask(akka.actor.ActorRef, Object, long)}.
	 * 
	 * @param messageClass
	 * @return
	 */
	<T> Future<T> ask(Class<T> messageClass, Timeout timeout);

	void tell(Object message);

}
