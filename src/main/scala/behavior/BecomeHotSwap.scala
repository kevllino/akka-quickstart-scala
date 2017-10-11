package behavior

import akka.actor.{ActorSystem, Props}
import behavior.UserStorage.DBOperation.Create

object BecomeHotSwap extends App {
  import UserStorage._

  val system = ActorSystem("hotswap-become")

  val userStorage = system.actorOf(Props[UserStorage], "userStorage")

  userStorage ! Connect
  userStorage ! Operation(Create, Some(User("admin", "admin@gmail.com")))
  userStorage ! Disconnect

  Thread.sleep(100)
  system.terminate()

}
