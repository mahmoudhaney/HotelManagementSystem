package hotelmanagement.models
import java.io.{BufferedWriter, FileWriter, PrintWriter}
import scala.io.Source
import scala.util.{Failure, Success, Try}

class Room
{
  // --------------------- Data Members ---------------------
  private var _id: Int = 1
  private var _type: String = "Single"
  private var _price: Double = 100.0
  private var _isOccupied: Boolean = false
  private val filePath = "src/main/scala/hotelmanagement/filehandling/Rooms.txt"

  // --------------------- Getters ---------------------
  def id: Int = _id
  def roomType: String = _type
  def price: Double = _price
  def isOccupied: Boolean = _isOccupied

  // --------------------- Setters ---------------------
  def id_=(newId: Int): Unit = _id = newId
  def roomType_=(newType: String): Unit = _type = newType
  def price_=(newPrice: Double): Unit = _price = newPrice
  def isOccupied_=(newIsOccupied: Boolean): Unit = _isOccupied = newIsOccupied

  // --------------------- Class Methods ---------------------
  def does_room_exist(roomId: Int): Boolean = {
    Try {
      val source = Source.fromFile(this.filePath)
      val exists = source.getLines().exists(line => line.split(" ")(0).toInt == roomId)
      source.close()
      exists
    } match {
      case Success(result) => result
      case Failure(exception) =>
        println(s"Error checking if room exists: ${exception.getMessage}")
        false
    }
  }

  def add_room(new_room: Room): Unit = {
    Try {
      val writer = new PrintWriter(new BufferedWriter(new FileWriter(this.filePath, true))) // 'true' for append mode
      try {
        writer.print(s"${new_room.id} ${new_room.roomType} ${new_room.price} ${new_room.isOccupied}" + "\n")
      } finally {
        writer.close()
      }
    }match {
      case Success(_) => println(s"Room with ID ${new_room.id} added successfully.")
      case Failure(exception) => println(s"Error deleting room: ${exception.getMessage}")
    }
  }

  def updateRoomById(roomId: Int, updatedRoom: Room): Unit = {
    Try {
      val source = Source.fromFile(this.filePath)
      val lines = source.getLines().map { line =>
        val columns = line.split(" ")
        if (columns(0).toInt == roomId) {
          // Update the room data if the room ID matches
          s"${updatedRoom.id} ${updatedRoom.roomType} ${updatedRoom.price} ${updatedRoom.isOccupied}"
        } else {
          line
        }
      }.mkString("\n")
      source.close()

      val writer = new PrintWriter(new BufferedWriter(new FileWriter(this.filePath)))
      try {
        writer.print(lines)
      } finally {
        writer.close()
      }
    } match {
      case Success(_) => println(s"Room with ID $roomId updated successfully.")
      case Failure(exception) => println(s"Error updating room: ${exception.getMessage}")
    }
  }

  def deleteRoomById(roomId: Int): Unit = {
    Try {
      val source = Source.fromFile(this.filePath)
      val lines = source.getLines().filterNot(line => line.split(" ")(0).toInt == roomId).mkString("\n")
      source.close()

      val writer = new PrintWriter(new BufferedWriter(new FileWriter(this.filePath)))
      try {
        writer.println(lines)
      } finally {
        writer.close()
      }
    } match {
      case Success(_) => println(s"Room with ID $roomId deleted successfully.")
      case Failure(exception) => println(s"Error deleting room: ${exception.getMessage}")
    }
  }



  // Check if a room is available
  def isRoomAvailable(roomId: Int): Boolean = {
    Try {
      val source = Source.fromFile(this.filePath)
      val available = source.getLines().exists { line =>
        val columns = line.split(" ")
        columns.length == 4 && columns(0).toInt == roomId && columns(3) == "false"
      }
      source.close()
      available
    } match {
      case Success(result) => result
      case Failure(exception) =>
        println(s"Error checking room availability: ${exception.getMessage}")
        false
    }
  }

  def getRoomPrice(roomId: Int): Option[Double] = {
    Try {
      val source = Source.fromFile(this.filePath)
      val priceOption = source.getLines().collectFirst {
        case line if line.startsWith(s"$roomId ") =>
          line.split(" ").lift(2).map(_.toDouble)
      }
      source.close()
      priceOption.flatten
    } match {
      case Success(result) => result
      case Failure(exception) =>
        println(s"Error getting room price: ${exception.getMessage}")
        None
    }
  }

  def updateRoomOccupancy(roomId: Int, isOccupied: Boolean): Unit = {
    Try {
      val source = Source.fromFile(this.filePath)
      val lines = source.getLines().map { line =>
        val columns = line.split(" ")
        if (columns.length == 4 && columns(0).toInt == roomId) {
          s"${columns(0)} ${columns(1)} ${columns(2)} $isOccupied"
        } else {
          line
        }
      }.mkString("\n")
      source.close()

      val writer = new PrintWriter(new BufferedWriter(new FileWriter(this.filePath)))
      try {
        writer.print(lines)
      } finally {
        writer.close()
      }
    } match {
      case Success(_) => println(s"Room Occupancy with ID $roomId updated successfully.")
      case Failure(exception) => println(s"Error updating room occupancy: ${exception.getMessage}")
    }
  }

  def printTableRow(columns: Array[String]): Unit = {
    print("|")
    columns.foreach { value =>
      print(s" $value | ")
    }
    println()
  }

  def printTableLine(header: String): Unit = {
    val line = "+------" * header.split(" ").length + "+"
    println(line)
  }

  def DisplayAvailableRooms(): Unit = {
    Try {
      val source = Source.fromFile(this.filePath)
      // Print the data in a table format
      source.getLines().foreach { line =>
        val columns = line.split(" ")
        if (columns.length == 4 && columns(3) == "false") {
          printTableRow(columns)
          printTableLine(line)
        }
      }
      source.close()
    } match {
      case Success(_) =>
      case Failure(exception) => println(s"Error reading file: ${exception.getMessage}")
    }
  }

  def Display(): Unit = {
    Try {
      val source = Source.fromFile(this.filePath)
      // Print the data in a table format
      source.getLines().foreach { line =>
        val columns = line.split(" ")
        printTableRow(columns)
        printTableLine(line)
      }
      source.close()
    } match {
      case Success(_) =>
      case Failure(exception) => println(s"Error reading file: ${exception.getMessage}")
    }
  }
  // --------------------- Override toString ---------------------
  override def toString: String = s"Room(id=$id, roomType=$roomType, price=$price, isOccupied=$isOccupied)"

}

