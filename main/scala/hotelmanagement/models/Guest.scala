package hotelmanagement.models
import java.io.{BufferedWriter, FileWriter, PrintWriter}
import scala.io.Source
import scala.util.{Failure, Success, Try}
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import com.itextpdf.text.{Document, Paragraph, PageSize}
import com.itextpdf.text.pdf.PdfWriter
import java.awt.Desktop
import java.io.File

class Guest
{
  // --------------------- Data Members ---------------------
  private var _name: String = "name"
  private var _mobile: String = "011"
  private var _gender: String = "male"
  private var _idProof: String = "30303010101212"
  private var _checkInDate: String = "1/1/2023"
  private var filePath = "src/main/scala/hotelmanagement/filehandling/Guests.txt"

  // --------------------- Getters ---------------------
  def name: String = _name
  def mobile: String = _mobile
  def gender: String = _gender
  def idProof: String = _idProof
  def checkInDate: String = _checkInDate

  // --------------------- Setters ---------------------
  def name_=(newName: String): Unit = _name = newName
  def mobile_=(newMobile: String): Unit = _mobile = newMobile
  def gender_=(newGender: String): Unit = _gender = newGender
  def idProof_=(newIdProof: String): Unit = _idProof = newIdProof
  def checkInDate_=(newCheckInDate: String): Unit = _checkInDate = newCheckInDate

  // --------------------- Class Methods ---------------------
  def does_guest_exist(roomId: Int): Boolean = {
    Try {
      val source = Source.fromFile(this.filePath)
      val exists = source.getLines().exists(line => line.split(" ")(5).toInt == roomId)
      source.close()
      exists
    } match {
      case Success(result) => result
      case Failure(exception) =>
        println(s"Error checking if room exists: ${exception.getMessage}")
        false
    }
  }

  def AddGuestToFile(new_guest: Guest, roomId: Int, roomPrice: Double): Unit = {
    Try {
      val writer = new PrintWriter(new BufferedWriter(new FileWriter(this.filePath, true))) // 'true' for append mode
      try {
        writer.println(s"${new_guest.name} ${new_guest.mobile} ${new_guest.gender} ${new_guest.idProof} ${new_guest.checkInDate} $roomId $roomPrice")
      } finally {
        writer.close()
      }
    } match {
      case Success(_) => println(s"Guest with Room ID $roomId added successfully.")
      case Failure(exception) => println(s"Error adding guest to file: ${exception.getMessage}")
    }
  }

  def calculateTotalCost(checkinDate: LocalDate, roomPrice: Double): Double = {
    val checkoutDate = LocalDate.now()
    var daysStayed = java.time.temporal.ChronoUnit.DAYS.between(checkinDate, checkoutDate)
    if (daysStayed == 0) daysStayed = 1
    daysStayed * roomPrice
  }

  private def deleteGuestFromFile(roomId: Int, guests: List[String]): Unit = {
    val updatedGuests = guests.filterNot(_.contains(s" $roomId "))
    val writer = new PrintWriter(new BufferedWriter(new FileWriter(this.filePath)))
    try {
      updatedGuests.foreach(writer.println)
    } finally {
      writer.close()
    }
  }

  def checkout(roomId: Int): Unit = {
    Try {
      val source = Source.fromFile(this.filePath)
      val guests = source.getLines().toList
      source.close()

      guests.find(_.contains(s" $roomId ")).foreach { guestData =>
        val guestInfo = guestData.split(" ")
        val guestName = guestInfo(0)
        val guestMobile = guestInfo(1)
        val checkinDate = LocalDate.parse(guestInfo(4), DateTimeFormatter.ofPattern("yyyy-MM-dd"))
        val roomIdFromFile = guestInfo(5).toInt
        val roomPrice = guestInfo(6).toDouble

        // Calculate the total cost
        val totalCost = calculateTotalCost(checkinDate, roomPrice)

        // Create a bill PDF
        createBillPDF(guestName, guestMobile, checkinDate, LocalDate.now(), roomPrice, totalCost)

        // Delete the guest from the file
        deleteGuestFromFile(roomId, guests)
      }
    } match {
      case Success(_) => println(s"Checkout for room ID $roomId successfully done.")
      case Failure(exception) => println(s"Error during checkout: ${exception.getMessage}")
    }
  }

  private def openPDFFile(filePath: String): Unit = {
    val file = new File(filePath)
    Desktop.getDesktop.open(file)
  }

  private def createBillPDF(guestName: String, guestMobile: String, checkinDate: LocalDate, checkoutDate: LocalDate, roomPrice: Double, totalCost: Double): Unit = {
    val folderPath = "C:\\Users\\Mahmoud Haney\\IdeaProjects\\HotelManagementSystem\\src\\main\\scala\\hotelmanagement\\Bills\\"
    val filePath = s"${folderPath}bill_${guestName}_${checkinDate}_${checkoutDate}.pdf"

    val document = new Document(PageSize.A4)
    val pdfWriter = PdfWriter.getInstance(document, new java.io.FileOutputStream(filePath))
    document.open()

    val titleFont = new com.itextpdf.text.Font(com.itextpdf.text.Font.FontFamily.HELVETICA, 16, com.itextpdf.text.Font.BOLD)
    val bodyFont = new com.itextpdf.text.Font(com.itextpdf.text.Font.FontFamily.HELVETICA, 12)

    var daysStayed = java.time.temporal.ChronoUnit.DAYS.between(checkinDate, checkoutDate)
    if(daysStayed == 0) daysStayed = 1;

    document.add(new Paragraph("Guest Bill", titleFont))
    document.add(new Paragraph(s"Guest Name: $guestName", bodyFont))
    document.add(new Paragraph(s"Mobile: $guestMobile", bodyFont))
    document.add(new Paragraph(s"Check-in Date: ${checkinDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))}", bodyFont))
    document.add(new Paragraph(s"Checkout Date: ${checkoutDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))}", bodyFont))
    document.add(new Paragraph(s"Days Stayed: $daysStayed", bodyFont))
    document.add(new Paragraph(s"Room Price: $roomPrice", bodyFont))
    document.add(new Paragraph(s"Total Cost: $totalCost", bodyFont))
    document.add(new Paragraph("Thank you, Hope see again :)  :)", titleFont))

    document.close()
    pdfWriter.close()

    println(s"Bill PDF saved at: $filePath")

    // Open the saved PDF file
    try {
      openPDFFile(filePath)
    } catch {
      case e: Exception =>
        println(s"Error opening PDF: ${e.getMessage}")
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

    def printTableRow(columns: Array[String]): Unit = {
      print("|")
      columns.foreach { value =>
        print(s" $value | ")
      }
      println()
    }

    def printTableLine(data: String): Unit = {
      val line = "+----------" * data.split(" ").length + "+"
      println(line)
    }
  }

  // --------------------- Override toString ---------------------
  override def toString: String = s"Guest(name=$name, mobile=$mobile, gender=$gender, idProof=$idProof, checkInDate=$checkInDate)"

}
