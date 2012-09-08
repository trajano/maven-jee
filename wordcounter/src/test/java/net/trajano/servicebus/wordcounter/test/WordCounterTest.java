package net.trajano.servicebus.wordcounter.test;

import static junit.framework.Assert.assertEquals;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.concurrent.RunnableFuture;

import net.trajano.servicebus.master.ActorProvider;
import net.trajano.servicebus.master.internal.AkkaServiceBus;
import net.trajano.servicebus.wordcounter.Accumulator;
import net.trajano.servicebus.wordcounter.MultiAccumulator;
import net.trajano.servicebus.wordcounter.internal.MultiWordCounterActorProvider;
import net.trajano.servicebus.wordcounter.internal.WordCounterActorProvider;

import org.junit.Test;

import akka.actor.ActorSystem;

/**
 * Objective, I should be able to do multi-level map reduce.
 * 
 * @author trajano
 * 
 */
public class WordCounterTest {
	@Test
	public void lipsum() throws Exception {
		final ActorSystem system = ActorSystem.create("PiSystem");
		final AkkaServiceBus serviceBus = new AkkaServiceBus(system);
		final ActorProvider provider = new WordCounterActorProvider();
		serviceBus.registerActorProvider(provider);
		final RunnableFuture<Accumulator> ask = serviceBus.ask(
				Accumulator.class, 2000);
		serviceBus.tell("/lipsum.txt");
		serviceBus.deregisterActorProvider(provider);
		ask.run();
		final Accumulator result = ask.get();
		assertEquals(512, result.getCount());
	}

	@Test
	public void notes() throws Exception {
		final ActorSystem system = ActorSystem.create("PiSystem");
		final AkkaServiceBus serviceBus = new AkkaServiceBus(system);
		final ActorProvider provider = new WordCounterActorProvider();
		serviceBus.registerActorProvider(provider);
		final RunnableFuture<Accumulator> ask = serviceBus.ask(
				Accumulator.class, 2000);
		serviceBus.tell("/notes.txt");
		serviceBus.deregisterActorProvider(provider);
		ask.run();
		final Accumulator result = ask.get();
		assertEquals(8, result.getCount());
	}

	@Test
	public void noteslipsum() throws Exception {
		final ActorSystem system = ActorSystem.create("PiSystem");
		final AkkaServiceBus serviceBus = new AkkaServiceBus(system);
		final ActorProvider provider = new WordCounterActorProvider();
		final ActorProvider provider2 = new MultiWordCounterActorProvider(
				serviceBus);
		serviceBus.registerActorProvider(provider);
		serviceBus.registerActorProvider(provider2);
		final RunnableFuture<MultiAccumulator> ask = serviceBus.ask(
				MultiAccumulator.class, 2000);
		serviceBus.tell(new String[] { "/lipsum.txt", "/notes.txt" });
		ask.run();
		final MultiAccumulator result = ask.get();
		assertEquals(520, result.getCount());
		serviceBus.deregisterActorProvider(provider2);
		serviceBus.deregisterActorProvider(provider);
	}

	/**
	 * Tests using without any actors. Shows that the algorithm works.
	 * 
	 * @throws Exception
	 */
	@Test
	public void sanityWordCount() throws Exception {
		final BufferedReader reader = new BufferedReader(new InputStreamReader(
				Thread.currentThread().getContextClassLoader()
						.getResourceAsStream("notes.txt")));
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
		assertEquals(8, count);
	}
}
