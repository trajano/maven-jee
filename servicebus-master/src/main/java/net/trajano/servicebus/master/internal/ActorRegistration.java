package net.trajano.servicebus.master.internal;

import java.io.Serializable;

import net.trajano.servicebus.master.ActorProvider;

/**
 * This is a message that represents the registration of an actor into the OSGi
 * environment. It is an internal message sent by Activator.
 */
public class ActorRegistration implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * The provider.
	 */
	private final ActorProvider provider;

	public ActorRegistration(final ActorProvider provider) {
		this.provider = provider;
	}

	public ActorProvider getProvider() {
		return provider;
	}
}
