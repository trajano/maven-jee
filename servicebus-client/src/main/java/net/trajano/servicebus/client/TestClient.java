package net.trajano.servicebus.client;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import net.trajano.servicebus.master.ServiceBus;
import net.trajano.servicebus.wordcounter.Accumulator;
import net.trajano.servicebus.wordcounter.MultiAccumulator;

public class TestClient {
	public TestClient(final ServiceBus bus) throws Exception {

		final ExecutorService executorService = new ThreadPoolExecutor(1, 20,
				5, TimeUnit.SECONDS, new ArrayBlockingQueue<Runnable>(10));
		{
			final Future<Accumulator> ask = bus.ask(Accumulator.class,
					executorService, 1000);
			bus.tell("/lipsum.txt");
			System.out.println(ask.get());
		}
		{
			final Future<Accumulator> ask = bus.ask(Accumulator.class,
					executorService, 1000);
			bus.tell("/notes.txt");
			System.out.println(ask.get());
		}
		{
			final Future<MultiAccumulator> ask = bus.ask(
					MultiAccumulator.class, executorService, 1000);
			bus.tell(new String[] { "/notes.txt", "/lipsum.txt" });
			System.out.println(ask.get());
		}

	}
}
