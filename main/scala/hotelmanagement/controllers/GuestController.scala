package hotelmanagement.controllers
import hotelmanagement.models.Guest

object GuestController {
  def show_guests():Unit ={
    new Guest().Display();
  }

  def check_in():Unit ={
    new Guest().Display();
  }

  def check_out():Unit ={
    new Guest().Display();
  }

}
