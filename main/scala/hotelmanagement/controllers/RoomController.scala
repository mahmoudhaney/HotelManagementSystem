package hotelmanagement.controllers
import hotelmanagement.models.Room
import scala.util.control.Breaks.{break, breakable}
import scala.io.StdIn
import akka.actor.ActorSystem
import hotelmanagement.actors.RoomActor

object RoomController {
  private val system = ActorSystem("HotelManagementSystem")
  private val roomActor = system.actorOf(RoomActor.props, "roomActor")

  def menu():Unit ={
    print("---------- Rooms ----------\n")
    println("1- Show Rooms\n2- Add Room\n3- Update Room\n4- Delete Room\n5- Exit")
    print("---------------------------\n")
  }

  def new_room():Unit ={
    val new_room = new Room;
    roomActor ! RoomActor.AddRoom()
    println("---> Room Data <---")
    print("Enter Room ID: ")
    new_room.id = StdIn.readInt()
    if (new_room.does_room_exist(new_room.id)) {
      println(s"Error!!: Room with ID ${new_room.id} already exists.")
    } else {
      print("Enter Room Type (Single/Double/Triple): ")
      new_room.roomType = StdIn.readLine()
      print("Enter Room Price: ")
      new_room.price = StdIn.readDouble()
      print("Is the Room Occupied? (true/false): ")
      new_room.isOccupied = StdIn.readBoolean()

      new_room.add_room(new_room)
    }
  }

  def update_room():Unit ={
    roomActor ! RoomActor.UpdateRoom()
    print("Enter Room ID to update: ")
    val roomIdToUpdate = StdIn.readInt()

    val room = new Room
    if (room.does_room_exist(roomIdToUpdate)) {
      room.id = roomIdToUpdate
      print("Enter Room Type (Single/Double/Triple): ")
      room.roomType = StdIn.readLine()
      print("Enter Room Price: ")
      room.price = StdIn.readDouble()
      print("Is the Room Occupied? (true/false): ")
      room.isOccupied = StdIn.readBoolean()

      room.updateRoomById(roomIdToUpdate, room)
    } else {
      println(s"Room with ID $roomIdToUpdate does not exist.")
    }
  }

  def delete_room():Unit ={
    roomActor ! RoomActor.DeleteRoom()
    print("Enter Room ID to delete: ")
    val roomIdToDelete = StdIn.readInt()
    val room = new Room
    if (room.does_room_exist(roomIdToDelete)) {
      room.deleteRoomById(roomIdToDelete)
    } else {
      println(s"Room with ID $roomIdToDelete does not exist.")
    }
  }

  def manage_rooms():Unit ={
    breakable
    {
      while (true) {
        try
        {
          menu()
          print("Choose: ")
          var Choice = scala.io.StdIn.readInt()

          if      (Choice == 1) {roomActor ! RoomActor.DisplayRooms; new Room().Display();}
          else if (Choice == 2) new_room()
          else if (Choice == 3) update_room()
          else if (Choice == 4) delete_room()
          else if (Choice == 5) break()
          else    println("!!Oops!! - Unknown Choice")
        }
        catch {
          case x: NumberFormatException => println("!!Oops!! Invalid Choice")
        }
      }
    }
  }

}
