package net.trajano.servicebus.master.test;

import static junit.framework.Assert.assertEquals;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import net.trajano.servicebus.master.ActorProvider;
import net.trajano.servicebus.master.internal.AkkaServiceBus;

import org.junit.Test;

import akka.actor.ActorSystem;

/**
 * Objective, I should be able to do multi-level map reduce.
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
		final AkkaServiceBus serviceBus = new AkkaServiceBus(system);
		final ActorProvider provider = new WordCounterActorProvider();
		serviceBus.registerActorProvider(provider);
		final ExecutorService executorService = new ThreadPoolExecutor(1, 20,
				5, TimeUnit.SECONDS, new ArrayBlockingQueue<Runnable>(10));
		final Future<Accumulator> ask = serviceBus.ask(Accumulator.class,
				executorService, 2000);
		serviceBus.tell("lipsum.txt");
		serviceBus.deregisterActorProvider(provider);
		final Accumulator result = ask.get();
		assertEquals(512, result.getCount());
	}
}
