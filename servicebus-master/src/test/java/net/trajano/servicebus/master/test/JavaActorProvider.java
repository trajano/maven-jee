package net.trajano.servicebus.master.test;

import net.trajano.servicebus.master.ActorProvider;
import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import akka.actor.UntypedActor;

public class JavaActorProvider implements ActorProvider {
	public static class Actor extends UntypedActor {

		@Override
		public void onReceive(Object message) throws Exception {
			if (message instanceof Message) {
				System.out.println(((Message) message).message);
			} else {
				unhandled(message);
			}
		}

	}

	public static class Message {
		public String message;
	}

	@Override
	public Class<?>[] handles() {
		return new Class<?>[] { Message.class };
	}

	/*
	 * override def handles() = Set(classOf[SampleMessage]) override def
	 * newActor(system: ActorSystem) = system.actorOf(Props[SampleActor], name =
	 * "sample") }
	 * 
	 * case class SampleMessage(message: String)
	 * 
	 * class SampleActor extends Actor { override def receive = { case
	 * SampleMessage(message) => { System.out.println(message) } } }
	 */

	@Override
	public ActorRef newActor(final ActorSystem system) {
		return system.actorOf(new Props(Actor.class));
	}
}
