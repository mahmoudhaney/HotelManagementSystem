package hotelmanagement.models

import scala.io.Source
import scala.util.{Failure, Success, Try}

class Guest
{
  // --------------------- Data Members ---------------------
  private var id: Int = 1
  private var name: String = "Mahmoud Haney"
  private var mobile: String = "01115007982"
  private var gender: String = "Male"
  private var idProof: String = "30303010101212"
  private var checkInDate: String = "1/1/2023"
  private var checkOutDate: String = "1/2/2023"
  private var filePath = "src/main/scala/hotelmanagement/filehandling/Guest.txt"

  // --------------------- Class Methods ---------------------
  def Display()
  {
    Try {
      val source = Source.fromFile(filePath)

      // Print the data in a table format
      source.getLines().foreach { line =>
        val columns = line.split(",")
        printTableRow(columns)
        printTableLine(line)
      }
      source.close()
    } match {
      case Success(_) =>
      case Failure(exception) => println(s"Error reading file: ${exception.getMessage}")
    }
    // Function to print a table row with borders
    def printTableRow(columns: Array[String]): Unit = {
      columns.foreach { value =>
        print("==> " + value)
      }
      println()
    }

    // Function to print a line separating rows with borders
    def printTableLine(header: String): Unit = {
      val line = "+------" * header.split(" ").length + "+"
      println(line)
    }
  }

}
