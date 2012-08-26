package net.trajano.servicebus.master.internal

import akka.actor.ActorSystem
import akka.osgi.ActorSystemActivator
import org.osgi.framework.BundleContext
import akka.actor.Props

/**
 * The way it would work is there's a bundle that provides the actor system.
 */
class Activator extends ActorSystemActivator {
  def configure(context: BundleContext, system: ActorSystem) {
  }
}