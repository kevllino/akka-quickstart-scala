package persistence

import akka.actor.ActorLogging
import akka.persistence._

object Counter {
  sealed trait Operation {
    val count: Int
  }
  case class Increment(override val count: Int) extends Operation
  case class Decrement(override val count: Int) extends Operation

  case class Cmd(op: Operation)
  case class Evt(op: Operation)

  case class State(count: Int)

}

class Counter extends PersistentActor with ActorLogging {
  import Counter._
  override def persistenceId: String = "counter-example"

  var state: State = State(count = 0)
  def updateState(evt: Evt) = evt match {
    case Evt(Increment(count)) =>
      state = State(count = state.count + count)
      takeSnapshot
    case Evt(Decrement(count)) =>
      state = State(count = state.count - count)
      takeSnapshot
  }

  // persistent actor starts on recovery mode
  // persistent receive on recovery mode
  // how state is updated during recovery by handling events and snapshot of a message
  val receiveRecover: Receive = {
    case evt: Evt =>
      println(s"Counter receive $evt on recovering mode")
      updateState(evt)
    case SnapshotOffer(_, snapshot: State) =>
      println(s"Counter receive snapshot with data $snapshot on recovering mode")
      state = snapshot
    case RecoveryCompleted =>
      println("Revocery completed, now switching to receiving mode")
  }

  // persistent receive on normal mode
  val receiveCommand: Receive = {
    case cmd @ Cmd(op) =>
      println(s"Counter receive $cmd")
      persist(Evt(op)) { evt =>
        // persist events and when persistence succeeds, it applies modif on them.
        updateState(evt)
      }
    case "print" =>
      println(s"The current state of the counter is")
    case SaveSnapshotSuccess(metadata) =>
      println(s"save snapshot succeed ")

    case SaveSnapshotFailure(metadata, reason) =>
      println(s"save snapshot failed ")

  }

  def takeSnapshot = if (state.count % 5 == 0) saveSnapshot(state)


  // convenience method for skipping recovery => replay of messages from the journal is skipped
  // override def recovery: Recovery = Recovery.none

}