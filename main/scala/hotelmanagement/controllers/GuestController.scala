package hotelmanagement.controllers
import hotelmanagement.models.{Guest, Room}
import java.time.LocalDate
import scala.io.StdIn
import akka.actor.ActorSystem
import hotelmanagement.actors.{GuestActor, RoomActor}

object GuestController {
  private val system = ActorSystem("HotelManagementSystem")
  private val guestActor = system.actorOf(GuestActor.props, "guestActor")
  private val roomActor = system.actorOf(RoomActor.props, "roomActor")

  def show_guests():Unit ={
    guestActor ! GuestActor.DisplayGuests
    new Guest().Display();
  }

  def check_in():Unit ={
    guestActor ! GuestActor.CheckIn()
    roomActor ! RoomActor.UpdateRoom()
    println("========== Checking-in ==========")
    val guest = new Guest
    val room = new Room

    println("Available Rooms:")
    room.DisplayAvailableRooms()
    print("Enter Room ID to book: ")
    val roomIdToBook = StdIn.readInt()

    if (room.isRoomAvailable(roomIdToBook)) {
      // Get the chosen room price
      val roomPriceOption = room.getRoomPrice(roomIdToBook)
      var room_price: Double = 0
      roomPriceOption  match {
        case Some(price) =>
          room_price = price
        case None =>
          println(s"Error: Room with ID $roomIdToBook not found or price not available.")
      }

      // Set today's date as the check-in date
      guest.checkInDate = LocalDate.now().toString
      // Get guest details
      print("Enter Guest Name: ")
      guest.name = StdIn.readLine()
      print("Enter Guest Mobile: ")
      guest.mobile = StdIn.readLine()
      print("Enter Guest Gender: ")
      guest.gender = StdIn.readLine()
      print("Enter Guest ID Proof: ")
      guest.idProof = StdIn.readLine()

      // Add the Guest
      guest.AddGuestToFile(guest, roomIdToBook, room_price)

      // Update the "Rooms.txt" file, marking the chosen room as occupied
      room.updateRoomOccupancy(roomIdToBook, isOccupied = true)
    } else {
      println(s"Error: Room with ID $roomIdToBook is not available.")
    }
  }

  def check_out():Unit ={
    guestActor ! GuestActor.CheckOut()
    roomActor ! RoomActor.UpdateRoom()
    val room = new Room;
    val guest = new Guest;
    println("========== Checking-out ==========")
    print("Enter Guest's Room ID: ")
    val roomID = StdIn.readInt()
    if (guest.does_guest_exist(roomID)) {
      guest.checkout(roomID);
      // Update the "Rooms.txt" file, marking the chosen room as not occupied
      room.updateRoomOccupancy(roomID, isOccupied = false)
    } else {
      println(s"Error!!: Guest with Room ID ${roomID} does not exist.")
    }
  }

}
