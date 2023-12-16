package hotelmanagement.actors
import akka.actor.{Actor, ActorLogging, Props}

object RoomActor {
  def props: Props = Props[RoomActor]

  // Messages
  case class AddRoom()
  case class UpdateRoom()
  case class DeleteRoom()
  case object DisplayRooms
}

class RoomActor extends Actor with ActorLogging {
  import RoomActor._

  override def receive: Receive = {
    case AddRoom() => log.info("")

    case UpdateRoom() => log.info("")

    case DeleteRoom() => log.info("")

    case DisplayRooms => log.info("")
  }
}
