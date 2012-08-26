package net.trajano.servicebus.master

/**
 * This is a message that represents the registration of an actor into the OSGi environment.
 */
case class ActorRegistration(provider: ActorProvider)
