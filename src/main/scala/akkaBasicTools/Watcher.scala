package akkaBasicTools

import akka.actor.{Actor, ActorIdentity, ActorRef, Identify}

class Watcher extends Actor {
  var counterRef: ActorRef = _
  val selection = context.actorSelection("/user/counter")

  selection ! Identify(None)

  override def receive: Receive = {
    case ActorIdentity(_, Some(ref)) =>
      println(s"Actor Reference counter is $ref")
    case ActorIdentity(_, None) =>
      println(s"Actor Reference counter is :(")
  }

}
