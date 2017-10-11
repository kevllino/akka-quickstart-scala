package akkaBasicTools

import akka.actor.{ActorSystem, Props}

object Watch extends App {

  val system = ActorSystem("watch")
  val counter = system.actorOf(Props[Counter], "counter")
  val watcher = system.actorOf(Props[Watcher], "watcher")

  Thread.sleep(1000)

  system.terminate()

}
