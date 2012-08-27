package net.trajano.servicebus.master.test;

import net.trajano.servicebus.master.ActorProvider;
import akka.actor.Actor;
import akka.actor.ActorContext;
import akka.actor.ActorRef;
import akka.actor.Props;
import akka.actor.UntypedActor;
import akka.actor.UntypedActorFactory;

public class ExceptionalActorProvider implements ActorProvider {

	@Override
	public Class<?>[] messageClassesHandled() {
		return new Class<?>[] { String.class };
	}

	@Override
	public ActorRef newActor(final ActorContext context) {
		return context.actorOf(new Props(new UntypedActorFactory() {

			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			@Override
			public Actor create() {
				return new UntypedActor() {

					@Override
					public void onReceive(final Object arg0) throws Exception {
						throw new Exception("always exception");
					}
				};
			}
		}));
	}

}
