package net.trajano.servicebus.client;

import net.trajano.servicebus.master.ServiceBus;
import net.trajano.servicebus.wordcounter.Accumulator;
import scala.concurrent.Await;
import scala.concurrent.Future;
import scala.concurrent.util.Duration;
import akka.util.Timeout;

public class TestClient {
	public TestClient(final ServiceBus bus) throws Exception {
		{
			final Future<Accumulator> ask = bus.ask(Accumulator.class,
					Timeout.longToTimeout(1000));
			bus.tell("/lipsum.txt");
			System.out.println(Await.result(ask, Duration.parse("2 seconds")));
		}
		{
			final Future<Accumulator> ask = bus.ask(Accumulator.class,
					Timeout.longToTimeout(1000));
			bus.tell("/notes.txt");
			System.out.println(Await.result(ask, Duration.parse("2 seconds")));
		}
	}
}
