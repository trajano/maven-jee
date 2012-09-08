package net.trajano.servicebus.master;

import java.util.concurrent.RunnableFuture;

public interface ServiceBus {
	/**
	 * Asks the service bus to provide a {@link RunnableFuture} that can be
	 * waited on when the message is sent to the master. The future must be
	 * run() before get().
	 * 
	 * @param messageClass
	 * @return
	 */
	<T> RunnableFuture<T> ask(Class<T> messageClass, long timeout);

	void tell(Object message);

}
