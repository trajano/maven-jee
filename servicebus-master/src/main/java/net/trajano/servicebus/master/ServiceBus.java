package net.trajano.servicebus.master;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
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
	<T> Future<T> ask(Class<T> messageClass, ExecutorService executor,
			long timeout);

	void tell(Object message);

}
