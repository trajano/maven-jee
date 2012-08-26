package net.trajano.servicebus.master.test;

import java.io.Serializable;

import net.trajano.servicebus.master.ActorProvider;
import akka.actor.ActorContext;
import akka.actor.ActorRef;
import akka.actor.Props;
import akka.actor.UntypedActor;

public class JavaActorProvider implements ActorProvider {
	public static class Actor extends UntypedActor {

		@Override
		public void onReceive(final Object message) throws Exception {
			if (message instanceof Message) {
				System.out.println(((Message) message).getMessage());
			} else {
				unhandled(message);
			}
		}

	}

	public static class Message implements Serializable {
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		private final String message;

		public Message(final String message) {
			this.message = message;
		}

		public String getMessage() {
			return message;
		}
	}

	@Override
	public Class<?>[] messageClassesHandled() {
		return new Class<?>[] { Message.class };
	}

	@Override
	public ActorRef newActor(final ActorContext context) {
		return context.actorOf(new Props(Actor.class));
	}

}
