package persistence

import akka.persistence.fsm.PersistentFSM
import akka.persistence.fsm.PersistentFSM.FSMState

import scala.reflect.ClassTag
import scala.reflect._
import akka.persistence._
import akka.persistence.fsm._

object Account {

  // Account states
  sealed trait State extends FSMState
  case object Empty extends State {
    override def identifier: String = "Empty"
  }
  case object Active extends State {
    override def identifier: String = "Active"
  }

  // Account Data
  sealed trait Data {
    val amount: Float
  }
  case object ZeroBalance extends Data {
    override val amount: Float = 0.0f
  }

  case class Balance(override val amount: Float) extends Data

  // domain event (persist events)
  sealed trait DomainEvent
  case class AcceptedTransaction(amount: Float, `type`: TransactionType) extends DomainEvent
  case class RejectedTransaction(amunt: Float, `type`: TransactionType, reason: String) extends DomainEvent

  // transaction types
  sealed trait TransactionType
  case object Credit extends TransactionType
  case object Debit extends TransactionType

  // commands
  case class Operation(amount: Float, `type`: TransactionType)
}

class Account extends PersistentFSM[Account.State, Account.Data, Account.DomainEvent] {
  import Account._

  override def persistenceId: String = "account"

  override def applyEvent(domainEvent: DomainEvent, currentData: Data): Data = {
    domainEvent match  {
      case AcceptedTransaction(amount, Credit) =>
        val newAmount = currentData.amount + amount
        println(s"New amoiunt is $newAmount")
        Balance(newAmount)

      case AcceptedTransaction(amount, Debit) =>
        val newAmount = currentData.amount - amount
        println(s"New amoiunt is $newAmount")
        if(newAmount > 0)
          Balance(newAmount)
        else
          ZeroBalance
      case RejectedTransaction(_, _, reason) =>
        println(s"Rejected transaction with reason: $reason")
        currentData
    }
  }

  override def domainEventClassTag: ClassTag[DomainEvent] = classTag[DomainEvent]

  startWith(Empty, ZeroBalance)

  when(Empty) {
    case Event(Operation(amount, Credit), _) =>
      println(s"Hi it's your first Credit Operation")
      goto(Active) applying AcceptedTransaction(amount, Credit)
    case Event(Operation(amount, Debit), _) =>
      println(s"Hi it's your first Debit Operation")
      stay applying RejectedTransaction(amount, Debit, "balance is zero")
  }

  when(Active) {
    case Event(Operation(amount, Credit), _) =>
      stay applying AcceptedTransaction(amount, Credit)
    case Event(Operation(amount, Debit), balance) =>
      val newBalance = balance.amount - amount
      if (newBalance > 0) {
        stay applying AcceptedTransaction(amount, Credit)
      }
      else if ( newBalance == 0)
        goto(Empty) applying AcceptedTransaction(amount, Debit)
      else stay applying RejectedTransaction(amount, Debit, "balance doesnt covere operation ")
  }
}
