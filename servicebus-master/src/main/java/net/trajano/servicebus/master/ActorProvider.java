package net.trajano.servicebus.master;

import akka.actor.ActorContext;
import akka.actor.ActorRef;

public interface ActorProvider {
	/**
	 * Provides an array of message classes that are handled by the provider.
	 * 
	 * @return message classes
	 */
	Class<?>[] messageClassesHandled();

	/**
	 * Creates a new actor in the given context.
	 * 
	 * @param context
	 * @return
	 */
	ActorRef newActor(ActorContext context);
}
