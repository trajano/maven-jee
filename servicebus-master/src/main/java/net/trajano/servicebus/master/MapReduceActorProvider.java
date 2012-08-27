package net.trajano.servicebus.master;

import java.util.concurrent.Future;

import net.trajano.servicebus.master.internal.MapReduceActor;
import akka.actor.ActorContext;
import akka.actor.ActorRef;
import akka.actor.Props;
import akka.actor.UntypedActor;
import akka.actor.UntypedActorFactory;

/**
 * This is an actor provider that handles data that uses the map-reduce pattern.
 * The actors are internal implementations.
 * 
 * TODO determine whether we should genericify this, there are many types that
 * can be used but it may be prohibitive when the parameter list is too long.
 */
public abstract class MapReduceActorProvider implements ActorProvider {

	/**
	 * This provides a {@link Future} that will contain the final result.
	 */
	public abstract Future<Object> ask();

	/**
	 * Initialize the accumulator.
	 * 
	 * @param message
	 *            message containing data to initialize the accumulator with.
	 * 
	 * @return
	 */
	public abstract Object initializeAccumulator(Object message);

	/**
	 * This takes a given message and tells another actor the message.
	 * Implementors of this class must call {@link ActorRef#tell(Object)} with
	 * the message {@link MapReduceWork} containing the derived message from the
	 * map.
	 * 
	 * @param message
	 * @param actor
	 *            this is the actor that is supposed to listen for work
	 *            messages.
	 * @return number of split items
	 * @throws Exception
	 */
	public abstract int map(Object message, ActorRef actor) throws Exception;

	@Override
	public final ActorRef newActor(final ActorContext context) {
		return context.actorOf(new Props(new UntypedActorFactory() {
			private static final long serialVersionUID = 1L;

			@Override
			public UntypedActor create() {
				return new MapReduceActor(MapReduceActorProvider.this);
			}
		}), getClass().getName());
	}

	/**
	 * Processes a submessage that has been split off by
	 * {@link #map(Object, ActorRef)}
	 * 
	 * @param derivedMessage
	 * @return the results of the processing. This may be <code>null</code> if
	 *         there is no response expected.
	 */
	public abstract Object process(Object derivedMessage);

	/**
	 * Combines the results into one.
	 * 
	 * @param accumulator
	 *            the results that have been accumulated so far.
	 * @param result
	 *            result from the work to be reduced.
	 */
	public abstract void reduce(Object accumulator, Object result);
}
