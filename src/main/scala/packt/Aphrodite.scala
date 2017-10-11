package packt

import akka.actor.SupervisorStrategy.Escalate
import akka.actor.{Actor, ActorRef, ActorSystem, OneForOneStrategy, Props, SupervisorStrategy}
import akka.actor.SupervisorStrategy._

// supervision example
class Aphrodite extends Actor {
  import Aphrodite._
  override def preStart() = {
    println("Aphrodite preStart hook ...")
  }

  override def preRestart(reason: Throwable, message: Option[Any]) = {
    println("Aphrodite preRestart hook ...")
    super.preRestart(reason, message)
  }

  override def postRestart(reason: Throwable): Unit = {
    println("Aphrodite preRestart hook ...")
    super.postRestart(reason)
  }

  override def postStop(): Unit =
    println("Aphrodite postStop...")

  override def receive: Receive = {
    case "Resume" =>
      throw ResumeException
    case "Stop" =>
      throw StopException
    case "Restart" =>
      throw RestartException
  }
}

object Aphrodite {
  case object ResumeException extends Exception
  case object StopException extends Exception
  case object RestartException extends Exception
}

// supervision strategy

class Hera extends Actor {
  import Aphrodite._
  import scala.concurrent.duration._

  var childRef: ActorRef = _

  override val supervisorStrategy: SupervisorStrategy =
    OneForOneStrategy(maxNrOfRetries = 10, withinTimeRange = 1 second) {
      case ResumeException => Resume
      case RestartException => Restart
      case StopException => Stop
      case _ : Exception => Escalate
    }

  override def preStart() = {
    childRef = context.actorOf(Props[Aphrodite], "Aphrodite")
    Thread.sleep(100)
  }

  override def receive: Receive = ???

}

object Supervision extends App {

  // Create the 'supervision' actor system
  val system = ActorSystem("supervision")

  // Create Hera Actor
  val hera = system.actorOf(Props[Hera], "hera")

  //   hera ! "Resume"
  //   Thread.sleep(1000)
  //   println()

  //  hera ! "Restart"
  //  Thread.sleep(1000)
  //  println()

  hera ! "Stop"
  Thread.sleep(1000)
  println()


  system.terminate()

}