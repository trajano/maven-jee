package net.trajano.servicebus.client;

import java.util.concurrent.RunnableFuture;

import net.trajano.servicebus.master.ServiceBus;
import net.trajano.servicebus.wordcounter.Accumulator;

public class TestClient {
	public TestClient(final ServiceBus bus) throws Exception {
		{
			final RunnableFuture<Accumulator> ask = bus.ask(Accumulator.class,
					1000);
			bus.tell("/lipsum.txt");
			ask.run();
			System.out.println(ask.get());
		}
		{
			final RunnableFuture<Accumulator> ask = bus.ask(Accumulator.class,
					1000);
			bus.tell("/notes.txt");
			ask.run();
			System.out.println(ask.get());
		}
	}
}
