package net.trajano.servicebus.master;

import akka.actor.UntypedActor;

public class MasterActor extends UntypedActor {
	// providers.foreach(provider => provider.handles(classOf[_]) &&
	// provider.newActor(context) ! _)
	//
	// private final Set<ActorProvider> providers = new HashSet<>(); def receive
	// = {
	// case ActorRegistration(provider) => {
	// log.info("received test")
	// System.out.println("!!!!")
	// providers += provider
	// }
	// case _ => {
	//
	// }
}
