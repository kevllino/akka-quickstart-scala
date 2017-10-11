package packt

import akka.actor.{Actor, ActorRef, ActorSystem, Props}
import akka.util.Timeout
import akka.pattern.ask
import packt.Checker.{BlackUser, CheckUser, WhiteUser}
import packt.Recorder.NewUser
import packt.Storage.AddUser

import scala.concurrent.duration._

case class User(name: String, email: String)

object Recorder {
  sealed trait RecordedMsg
  case class NewUser(user: User) extends RecordedMsg

  def props(checker: ActorRef, storage: ActorRef) =
    Props(new Recorder(checker, storage))
}

object Checker {
  sealed trait CheckerMsg
  case class CheckUser(user: User) extends CheckerMsg

  sealed trait CheckerResponse
  case class BlackUser(user: User) extends CheckerMsg
  case class WhiteUser(user: User) extends CheckerMsg
}

object Storage {
  sealed trait StorageMsg
  case class AddUser(user: User) extends StorageMsg
}

class Storage extends Actor {
  var users = List.empty[User]

  override def receive: Receive = {
    case AddUser(user: User) =>
      users = user :: users
  }
}

class Checker extends Actor {
  val blackList = List(
    User("Donal", "donal@gmail.com")
  )

  override def receive: Receive = {
    case CheckUser(user) if blackList.map(_.email) contains  user.email =>
      // sends message to sender
      sender() ! BlackUser(user)
    case NewUser(user) =>
     sender() ! WhiteUser(user)
  }
}

class Recorder(checker: ActorRef, storage: ActorRef) extends Actor {
  import scala.concurrent.ExecutionContext.Implicits.global

  implicit val timeout = Timeout(5 seconds)

  def receive = {
    case NewUser(user) =>
      checker ? CheckUser(user) map {
        case WhiteUser(user) =>
          storage ! AddUser(user)
        case BlackUser(user) =>
          println(s"Recorder: $user in the blacklist")
      }
  }

}

object TalkToActor extends App {

  val system = ActorSystem("talk-to-actor")
  val checker = system.actorOf(Props[Checker], "checker")
  val storage = system.actorOf(Props[Storage], "storage")
  val recorder = system.actorOf(Recorder.props(checker, storage), "recorder")

  recorder ! Recorder.NewUser(User("Kev", "donal@gmail.com"))

  Thread.sleep(100)
  system.terminate()

}
