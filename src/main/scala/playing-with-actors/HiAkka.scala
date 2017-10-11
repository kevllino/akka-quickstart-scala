package packt

import akka.actor.{Actor, ActorSystem, Props}

case class WhoToGreet(who: String)

class Greeter extends Actor {
  override def receive: Receive = {
    case WhoToGreet(who) => println(s"Hello $who")
  }
}

object HiAkka extends App {

  val system = ActorSystem("hello")
  val greeter = system.actorOf(Props[Greeter], "greeter")
  greeter ! WhoToGreet("Kevin")
}
