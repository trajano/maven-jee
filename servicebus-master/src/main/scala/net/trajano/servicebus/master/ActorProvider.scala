package net.trajano.servicebus.master

import akka.actor.ActorRef
import akka.actor.ActorSystem

trait ActorProvider {
  def handles(): Array[Class[_]]
  def newActor(system: ActorSystem): ActorRef
}