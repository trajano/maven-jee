package net.trajano.servicebus.master.internal;

public class Asked {
	/**
	 * Message class being asked.
	 */
	private final Class<?> messageClass;

	public Asked(final Class<?> messageClass) {
		this.messageClass = messageClass;
	}

	public Class<?> getMessageClass() {
		return messageClass;
	}
}
