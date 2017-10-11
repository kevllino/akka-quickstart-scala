package routing

import akka.actor.{Actor, ActorRef, ActorSystem, Props}
import Worker._

class RouterPool extends Actor {
  var routees: List[ActorRef] = _

  override def preStart(): Unit = {
    routees = List.fill(5)(
      context.actorOf(Props[Worker])
    )
  }

  override def receive: Receive = {
    case msg: Work =>
      println("I'm a router who just received a message...")
      routees(util.Random.nextInt(routees.size)) forward msg
  }

}

class RouterGroup(routees: List[String]) extends Actor {
  def receive = {
    case msg: Work =>
      println("I'm a router who just received a Work message...")
      context.actorSelection(routees(util.Random.nextInt(routees.size))) forward msg
  }
}

object Router extends App {
  val system = ActorSystem("router")
  val router = system.actorOf(Props[RouterPool])

  system.actorOf(Props[Worker], "worker1")
  system.actorOf(Props[Worker], "worker2")

  val workers = List("/user/worker1", "/user/worker2")
  val routerGroup = system.actorOf(Props(classOf[RouterGroup], workers))

  routerGroup ! Work()
  routerGroup ! Work()

  Thread.sleep(100)

  system.terminate()
}
