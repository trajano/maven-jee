package net.trajano.servicebus.master.test;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.concurrent.Future;

import net.trajano.servicebus.master.MapReduceActorProvider;
import net.trajano.servicebus.master.MapReduceWork;
import akka.actor.ActorRef;

public class WordCounterActorProvider extends MapReduceActorProvider {
	public static class Accumulator {
		private int count = 0;

		public void add(final int n) {
			count += n;
		}

		public int getCount() {
			return count;
		}

		@Override
		public String toString() {
			return String.valueOf(count);
		}
	}

	@Override
	public Future<Object> ask() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object initializeAccumulator(final Object message) {
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
			actor.tell(new MapReduceWork(line));
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
	public Object process(final Object derivedMessage) {
		final String trimmed = ((String) derivedMessage).trim();
		if (trimmed.isEmpty()) {
			return new Integer(0);
		}
		return new Integer(trimmed.split("\\s+").length);
	}

	@Override
	public void reduce(final Object accumulator, final Object result) {
		((Accumulator) accumulator).add((Integer) result);
	}

}
