package hotelmanagement.controllers
import hotelmanagement.models.Employee
import scala.util.control.Breaks.{break, breakable}
import scala.io.StdIn
import akka.actor.ActorSystem
import hotelmanagement.actors.EmployeeActor

object EmployeeController {
  private val system = ActorSystem("HotelManagementSystem")
  private val employeeActor = system.actorOf(EmployeeActor.props, "employeeActor")

  def menu():Unit ={
    print("---------- Employees ----------\n")
    println("1- Show Employees\n2- Add Employee\n3- Update Employee\n4- Delete Employee\n5- Exit")
    print("---------------------------\n")
  }

  def new_employee():Unit ={
    val new_employee = new Employee;
    employeeActor ! EmployeeActor.AddEmployee()
    println("---> Employee Data <---")
    print("Enter Employee ID: ")
    new_employee.id = StdIn.readInt()
    if (new_employee.does_employee_exist(new_employee.id)) {
      println(s"Error!!: Employee with ID ${new_employee.id} already exists.")
    } else {
      print("Enter Employee's Name: ")
      new_employee.name = StdIn.readLine()
      print("Enter Employee's email: ")
      new_employee.email = StdIn.readLine()
      print("Enter Employee's Department: ")
      new_employee.department = StdIn.readLine()
      print("Enter Employee's Salary: ")
      new_employee.salary = StdIn.readDouble()

      new_employee.add_employee(new_employee)
    }
  }

  def update_employee():Unit ={
    employeeActor ! EmployeeActor.UpdateEmployee()
    print("Enter Employee ID to update: ")
    val employeeIdToUpdate = StdIn.readInt()

    val employee = new Employee
    if (employee.does_employee_exist(employeeIdToUpdate)) {
      employee.id = employeeIdToUpdate
      print("Enter Employee's Name: ")
      employee.name = StdIn.readLine()
      print("Enter Employee's email: ")
      employee.email = StdIn.readLine()
      print("Enter Employee's Department: ")
      employee.department = StdIn.readLine()
      print("Enter Employee's Salary: ")
      employee.salary = StdIn.readDouble()

      employee.updateEmployeeById(employeeIdToUpdate, employee)
    } else {
      println(s"Employee with ID $employeeIdToUpdate does not exist.")
    }
  }

  def delete_employee():Unit ={
    employeeActor ! EmployeeActor.DeleteEmployee()
    print("Enter Employee ID to delete: ")
    val employeeIdToDelete = StdIn.readInt()
    val employee = new Employee
    if (employee.does_employee_exist(employeeIdToDelete)) {
      employee.deleteEmployeeById(employeeIdToDelete)
    } else {
      println(s"Employee with ID $employeeIdToDelete does not exist.")
    }
  }

  def manage_employees():Unit ={
    breakable
    {
      while (true) {
        try
        {
          menu()
          print("Choose: ")
          var Choice = scala.io.StdIn.readInt()

          if      (Choice == 1) {employeeActor ! EmployeeActor.DisplayEmployee; new Employee().Display();}
          else if (Choice == 2) new_employee()
          else if (Choice == 3) update_employee()
          else if (Choice == 4) delete_employee()
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
