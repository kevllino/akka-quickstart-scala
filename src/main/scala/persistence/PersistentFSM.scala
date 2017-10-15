package persistence

import akka.persistence._
import akka.actor.{ Actor, ActorRef, ActorSystem, Props }


object PersistentFSM extends App {
  import Account._

  val system = ActorSystem("persistent-fsm-actors")

  val account = system.actorOf(Props[Account])

  account ! Operation(1, Debit)

  account ! Operation(1000, Credit)

  account ! Operation(10, Debit)

  Thread.sleep(1000)

  system.terminate()

}
