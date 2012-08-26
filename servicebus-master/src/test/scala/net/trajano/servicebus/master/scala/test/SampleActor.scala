package net.trajano.servicebus.master.scala.test

import net.trajano.servicebus.master.ActorProvider
import akka.actor.ActorSystem
import akka.actor.Props
import akka.actor.Actor

class SampleActorProvider extends ActorProvider {
  override def handles() = Array(classOf[SampleMessage])
  override def newActor(system: ActorSystem) = system.actorOf(Props[SampleActor], name = "sample")
}

case class SampleMessage(message: String)

class SampleActor extends Actor {
  override def receive = {
    case SampleMessage(message) => {
      System.out.println(message)
    }
  }
}