package hotelmanagement.models
import java.io.{BufferedWriter, FileWriter, PrintWriter}
import scala.io.Source
import scala.util.{Failure, Success, Try}

class Employee
{
  // --------------------- Data Members ---------------------
  private var _id: Int = 1
  private var _name: String = "name"
  private var _email: String = "example@ex.com"
  private var _department: String = "HR"
  private var _salary: Double = 100.0
  private val filePath = "src/main/scala/hotelmanagement/filehandling/Employees.txt"

  // --------------------- Getters ---------------------
  def id: Int = _id
  def name: String = _name
  def email: String = _email
  def department: String = _department
  def salary: Double = _salary

  // --------------------- Setters ---------------------
  def id_=(newId: Int): Unit = _id = newId
  def name_=(newName: String): Unit = _name = newName
  def email_=(newEmail: String): Unit = _email = newEmail
  def department_=(newDepartment: String): Unit = _department = newDepartment
  def salary_=(newSalary: Double): Unit = _salary = newSalary

  // --------------------- Class Methods ---------------------
  def does_employee_exist(employeeId: Int): Boolean = {
    Try {
      val source = Source.fromFile(this.filePath)
      val exists = source.getLines().exists(line => line.split(" ")(0).toInt == employeeId)
      source.close()
      exists
    } match {
      case Success(result) => result
      case Failure(exception) =>
        println(s"Error checking if employee exists: ${exception.getMessage}")
        false
    }
  }

  def add_employee(new_employee: Employee): Unit = {
    Try {
      val writer = new PrintWriter(new BufferedWriter(new FileWriter(this.filePath, true))) // 'true' for append mode
      try {
        writer.print(s"${new_employee.id} ${new_employee.name} ${new_employee.email} ${new_employee.department} ${new_employee.salary}" + "\n")
      } finally {
        writer.close()
      }
    }match {
      case Success(_) => println(s"Employee with ID ${new_employee.id} added successfully.")
      case Failure(exception) => println(s"Error adding employee: ${exception.getMessage}")
    }
  }

  def updateEmployeeById(employeeId: Int, updatedEmployee: Employee): Unit = {
    Try {
      val source = Source.fromFile(this.filePath)
      val lines = source.getLines().map { line =>
        val columns = line.split(" ")
        if (columns(0).toInt == employeeId) {
          // Update the room data if the room ID matches
          s"${updatedEmployee.id} ${updatedEmployee.name} ${updatedEmployee.email} ${updatedEmployee.department} ${updatedEmployee.salary}"
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
      case Success(_) => println(s"Employee with ID $employeeId updated successfully.")
      case Failure(exception) => println(s"Error updating employee: ${exception.getMessage}")
    }
  }

  def deleteEmployeeById(employeeId: Int): Unit = {
    Try {
      val source = Source.fromFile(this.filePath)
      val lines = source.getLines().filterNot(line => line.split(" ")(0).toInt == employeeId).mkString("\n")
      source.close()

      val writer = new PrintWriter(new BufferedWriter(new FileWriter(this.filePath)))
      try {
        writer.println(lines)
      } finally {
        writer.close()
      }
    } match {
      case Success(_) => println(s"Employee with ID $employeeId deleted successfully.")
      case Failure(exception) => println(s"Error deleting employee: ${exception.getMessage}")
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

  def printTableRow(columns: Array[String]): Unit = {
    print("|")
    columns.foreach { value =>
      print(s" $value | ")
    }
    println()
  }

  def printTableLine(header: String): Unit = {
    val line = "+--------" * header.split(" ").length + "+"
    println(line)
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
  override def toString: String = s"Room(id=$id, name=$name, email=$email, department=$department, salary=$salary)"

}

