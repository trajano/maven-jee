package net.trajano.servicebus.master.test;

import static junit.framework.Assert.assertEquals;
import static org.mockito.Mockito.mock;

import java.io.BufferedReader;
import java.io.InputStreamReader;

import net.trajano.servicebus.master.ActorProvider;
import net.trajano.servicebus.master.internal.AkkaServiceBus;

import org.junit.Test;
import org.osgi.framework.BundleContext;

import scala.concurrent.Await;
import scala.concurrent.Future;
import scala.concurrent.util.Duration;
import akka.actor.ActorSystem;
import akka.util.Timeout;

/**
 * Objective, I should be able to do multi-level map reduce.
 * 
 * @author trajano
 * 
 */
public class WordCounterTest {
	/**
	 * Tests using without any actors. Shows that the algorithm works.
	 * 
	 * @throws Exception
	 */
	@Test
	public void sanityWordCount() throws Exception {
		final BufferedReader reader = new BufferedReader(new InputStreamReader(
				Thread.currentThread().getContextClassLoader()
						.getResourceAsStream("lipsum.txt")));
		int count = 0;
		String line = reader.readLine();
		while (line != null) {
			final String trimmed = line.trim();
			if (!trimmed.isEmpty()) {
				count += trimmed.split("\\s+").length;
			}
			line = reader.readLine();
		}
		reader.close();
		assertEquals(512, count);
	}

	@Test
	public void useActivator() throws Exception {
		final ActorSystem system = ActorSystem.create("PiSystem");
		final AkkaServiceBus serviceBus = new AkkaServiceBus();
		serviceBus.configure(mock(BundleContext.class), system);
		final ActorProvider provider = new WordCounterActorProvider();
		serviceBus.registerActorProvider(provider);
		final Future<WordCounterActorProvider.Accumulator> ask = serviceBus
				.ask(WordCounterActorProvider.Accumulator.class,
						Timeout.intToTimeout(2000));
		serviceBus.tell("lipsum.txt");
		serviceBus.deregisterActorProvider(provider);
		final WordCounterActorProvider.Accumulator result = Await.result(ask,
				Duration.parse("2 seconds"));
		assertEquals(512, result.getCount());
	}
}
