package behavior

import akka.actor.{Actor, Stash}
import behavior.UserStorage.{Connect, Disconnect, Operation}

case class User(username: String, email: String) {
  override def toString: String = s"user:$username @ $email"
}

object UserStorage {
  trait DBOperation
  object DBOperation {
    case object Create extends DBOperation
    case object Update extends DBOperation
    case object Read extends DBOperation
    case object Delete extends DBOperation
  }

  case object Connect
  case object Disconnect
  case class Operation(dBOperation: DBOperation, user: Option[User])
}

class UserStorage extends Actor with Stash {

  override def receive: Receive = disconnected

  def connected: Actor.Receive = {
    case Disconnect =>
      println("User Storage disconnect from DB")
      context.unbecome()
    case Operation(op, user) =>
      println(s"User Storage receive $op to do in user: ${user.get.toString}")
  }
  def disconnected: Actor.Receive = {
    case Connect =>
      println(s"User connected to the DB")
      unstashAll()
      context.become(connected)
    case _ =>
      stash()
  }
}