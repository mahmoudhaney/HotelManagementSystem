import scala.util.control.Breaks._
import hotelmanagement.controllers.RoomController
import hotelmanagement.controllers.GuestController

object HotelManagementApp {

  def menu():Unit ={
    print("<-<-<-<-<- Hotel Menu ->->->->->\n")
    println("1- Rooms\n2- Show Guests\n3- Check-in\n4- Check-out\n5- Exit")
    print("<-<-<-<-<-<-<-<-->->->->->->->->\n")
  }

  def pick_choice():Unit ={
    breakable
    {
      while (true) {
        try
        {
          menu()
          print("Choose a feature: ")
          var Choice = scala.io.StdIn.readInt()

          if      (Choice == 1) RoomController.manage_rooms();
          else if (Choice == 2) GuestController.show_guests()
          else if (Choice == 3) GuestController.check_in()
          else if (Choice == 4) GuestController.check_out()
          else if (Choice == 5) {println("Thanks :) :)"); break();}
          else    println("!!Oops!! - Unknown Choice")
        }
        catch {
          case x: NumberFormatException => println("!!Oops!! Invalid Choice")
        }
      }
    }
  }

  def main(args: Array[String]): Unit = {
    pick_choice()
  }

}