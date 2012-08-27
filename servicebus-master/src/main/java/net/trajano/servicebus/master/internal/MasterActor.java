package net.trajano.servicebus.master.internal;

import java.util.HashMap;
import java.util.Map;

import net.trajano.servicebus.master.ActorProvider;
import akka.actor.ActorRef;
import akka.actor.UntypedActor;

import com.google.common.collect.LinkedListMultimap;
import com.google.common.collect.ListMultimap;
import com.google.common.collect.Multimaps;

public class MasterActor extends UntypedActor {

	/**
	 * I want to map a class that is being asked to a listener. However, there
	 * could be more than one listener. This maps the {@link Asked} message
	 * class to a reference count.
	 */
	private final ListMultimap<Class<?>, ActorRef> askedActors = Multimaps
			.synchronizedListMultimap(LinkedListMultimap
					.<Class<?>, ActorRef> create());

	/**
	 * Map from class being handled to the {@link ActorProvider}.
	 */
	private final Map<Class<?>, ActorProvider> providers = new HashMap<>();

	/**
	 * This throws an {@link IllegalArgumentException} if the class has a
	 * handler registered already.
	 * 
	 * @param messageClass
	 */
	private void assertMessageClassNotHandled(final Class<?> messageClass) {
		if (providers.containsKey(messageClass)) {
			throw new IllegalArgumentException("handler for  " + messageClass
					+ " already exists.");
		}
	}

	@Override
	public void onReceive(final Object message) throws Exception {
		if (message instanceof ActorRegistration) {
			final ActorProvider provider = ((ActorRegistration) message)
					.getProvider();
			for (final Class<?> messageClass : provider.messageClassesHandled()) {
				assertMessageClassNotHandled(messageClass);
				providers.put(messageClass, provider);
			}
			return;
		} else if (message instanceof ActorDeregistration) {
			final ActorProvider provider = ((ActorDeregistration) message)
					.getProvider();
			for (final Class<?> messageClass : provider.messageClassesHandled()) {
				providers.remove(messageClass);
			}
			return;
		} else if (message instanceof Asked) {
			final Class<?> messageClass = ((Asked) message).getMessageClass();
			assertMessageClassNotHandled(messageClass);
			askedActors.put(messageClass, getSender());
		}
		for (final Class<?> messageClass : providers.keySet()) {
			if (messageClass.isInstance(message)) {
				providers.get(messageClass).newActor(getContext())
						.tell(message, getSelf());
				return;
			}
		}
		for (final Class<?> messageClass : askedActors.keySet()) {
			if (messageClass.isInstance(message)) {
				askedActors.get(messageClass).remove(0)
						.tell(message, getSelf());
				return;
			}
		}
		unhandled(message);
	}
}
