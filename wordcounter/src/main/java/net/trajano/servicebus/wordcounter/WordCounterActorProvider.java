package net.trajano.servicebus.wordcounter;

import java.io.BufferedReader;
import java.io.InputStreamReader;

import net.trajano.servicebus.master.MapReduceActorProvider;
import akka.actor.ActorRef;

public class WordCounterActorProvider extends
		MapReduceActorProvider<Accumulator, String, Integer> {

	@Override
	public Accumulator initializeAccumulator(final Object message) {
		return new Accumulator();
	}

	@Override
	public int map(final Object message, final ActorRef actor) throws Exception {
		final BufferedReader reader = new BufferedReader(new InputStreamReader(
				Thread.currentThread().getContextClassLoader()
						.getResourceAsStream("lipsum.txt")));
		int count = 0;
		String line = reader.readLine();
		while (line != null) {
			actor.tell(line);
			++count;
			line = reader.readLine();
		}
		reader.close();
		return count;
	}

	@Override
	public Class<?>[] messageClassesHandled() {
		return new Class<?>[] { String.class };
	}

	@Override
	public Integer process(final String derivedMessage) {
		final String trimmed = derivedMessage.trim();
		if (trimmed.isEmpty()) {
			return 0;
		}
		return trimmed.split("\\s+").length;
	}

	@Override
	public void reduce(final Accumulator accumulator, final Integer result) {
		accumulator.add(result);
	}

}
