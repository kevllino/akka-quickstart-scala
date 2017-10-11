package akkaBasicTools

import akka.actor.{ActorRef, ActorSystem, PoisonPill, Props}

object ActorPathExample extends App {
  val system = ActorSystem("actor-path")

  // Different actor references but same actor selection
  val counter1: ActorRef = system.actorOf(Props[Counter], "counter")
  println(s"Actor reference for counter ${counter1}")

  val counterSelection1 = system.actorSelection("counter")
  println(s"Actor selection for counter ${counterSelection1}")

  counter1 ! PoisonPill

  Thread.sleep(100)

  val counter2: ActorRef = system.actorOf(Props[Counter], "counter")
  println(s"Actor reference for counter ${counter2}")

  val counterSelection2 = system.actorSelection("counter")
  println(s"Actor selection for counter ${counterSelection2}")

  system.terminate()
}
