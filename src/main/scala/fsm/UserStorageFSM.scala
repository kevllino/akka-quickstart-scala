package fsm

import akka.actor.{ActorSystem, FSM, Props, Stash}
import behavior.UserStorage.DBOperation.Create
import behavior.{User, UserStorage}
import behavior.UserStorage.Operation

object UserStorageFSM {
  sealed trait State
  case object Connected extends State
  case object Disconnected extends State

  sealed trait Data
  case object EmptyData extends Data
}

trait DBOperation {
  object DBOperation {
    case object Create extends DBOperation
    case object Update extends DBOperation
    case object Read extends DBOperation
    case object Delete extends DBOperation
  }
  case object Connect
  case object Disconnect
  case class Operation(op: DBOperation, user: User)
}

class UserStorageFSM extends FSM[UserStorageFSM.State, UserStorageFSM.Data] with Stash with DBOperation{
  import UserStorageFSM._

  startWith(Disconnected, EmptyData)

  // state function is a partial function that takes an event and returns its state
  when(Disconnected) {
    case Event(Connect, _) =>
      println(s"User connected to the DB")
      unstashAll()
      goto(Connected) using(EmptyData)
    case Event(_, _) =>
      stash()
      stay() using(EmptyData)
  }

  initialize()

}


