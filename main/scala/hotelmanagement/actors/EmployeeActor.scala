package hotelmanagement.actors
import akka.actor.{Actor, ActorLogging, Props}

object EmployeeActor {
  def props: Props = Props[RoomActor]

  // Messages
  case class AddEmployee()
  case class UpdateEmployee()
  case class DeleteEmployee()
  case object DisplayEmployee
}

class EmployeeActor extends Actor with ActorLogging {
  import EmployeeActor._

  override def receive: Receive = {
    case AddEmployee() => log.info("")

    case UpdateEmployee() => log.info("")

    case DeleteEmployee() => log.info("")

    case DisplayEmployee => log.info("")
  }
}
