package net.trajano.servicebus.master

import akka.actor.Actor
import akka.actor.ActorLogging
import scala.collection.mutable.Set

class MasterActor extends Actor with ActorLogging {
  val providers = Set[ActorProvider]()
  def receive = {
    case ActorRegistration(provider) => {
      log.info("received test")
      System.out.println("!!!!")
      providers += provider
    }
    case _ => {
      val message = _
      // providers.foreach(provider => provider.handles(classOf[_]) && provider.newActor(context) ! _)
    }
  }
}