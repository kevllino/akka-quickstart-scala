package packt

import akka.actor.{Actor, ActorSystem, Props}
import packt.MusicController.{Play, Stop}
import packt.MusicPlayer.{StartMusic, StopMusic}

object MusicController {
  sealed trait ControllerMsg
  case object Play extends ControllerMsg
  case object Stop extends ControllerMsg

  def props = Props[MusicController]
}

class MusicController extends Actor {
  override def receive: Receive = {
    case Play => println("Music started")
    case Stop => println("Music stopped")
    case _ => println("danfer")
  }
}

object MusicPlayer {
  sealed trait PlayerMsg
  case object StartMusic extends PlayerMsg
  case object StopMusic extends PlayerMsg
}

class MusicPlayer extends Actor {
  override def receive: Receive = {
    case StopMusic => println("Music stopping")
    case StartMusic =>
      val controller = context.actorOf(MusicController.props, "controller")
      controller ! Play
    case _ => println("danfer")
  }
}

object ActorCreation extends App {
  val system = ActorSystem("actor-creation")

  val musicPlayer = system.actorOf(Props[MusicPlayer], "player")
  musicPlayer ! StartMusic

  musicPlayer ! StopMusic

  system.terminate()

}
