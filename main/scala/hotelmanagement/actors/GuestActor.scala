package hotelmanagement.actors
import akka.actor.{Actor, ActorLogging, Props}


object GuestActor {
  def props: Props = Props[GuestActor]

  // Messages
  case class CheckIn()
  case class CheckOut()
  case object DisplayGuests
}

class GuestActor extends Actor with ActorLogging {
  import GuestActor._

  override def receive: Receive = {
    case CheckIn() => log.info("")

    case CheckOut() => log.info("")

    case DisplayGuests => log.info("")
  }
}
