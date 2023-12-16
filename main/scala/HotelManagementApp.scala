import scala.util.control.Breaks._
import hotelmanagement.controllers.{RoomController, GuestController, EmployeeController}

object HotelManagementApp {

  def menu():Unit ={
    print("<-<-<-<-<- Hotel Menu ->->->->->\n")
    println("1- Rooms\n2- Employees\n3- Show Guests\n4- Check-in\n5- Check-out\n6- Exit")
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
          else if (Choice == 2) EmployeeController.manage_employees();
          else if (Choice == 3) GuestController.show_guests()
          else if (Choice == 4) GuestController.check_in()
          else if (Choice == 5) GuestController.check_out()
          else if (Choice == 6) {println("Thanks :) :)"); break();}
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