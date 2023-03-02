package practice.spring.salarycomputer.service;

import java.util.Optional;
import java.util.stream.StreamSupport;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import practice.spring.salarycomputer.entity.Employee;
import practice.spring.salarycomputer.repository.EmployeeRepo;

@Service
public class EmployeeService {

  @Autowired
  private EmployeeRepo employeeRepo;

  public Optional<Employee> getEmployee(String firstName, String lastName) {
    return employeeRepo.findByFirstNameAndLastName(firstName, lastName);
  }

  public Employee[] getAllEmployees() {
    return StreamSupport.stream(employeeRepo.findAll().spliterator(), false).toArray(Employee[]::new);
  }

  public Employee saveEmployee(Employee employeeToAdd) {
    return employeeRepo.save(employeeToAdd);
  }

  @Transactional
  public int deleteEmployee(String firstName, String lastName) {
    return employeeRepo.deleteByFirstNameAndLastName(firstName, lastName);
  }

  public Employee getEmployeeSalary(String firstName, String lastName, int numOfDaysWorked) {
    Optional<Employee> employee = this.getEmployee(firstName, lastName);
    if (employee.isPresent()) {
      employee.get().getGrossSalary(numOfDaysWorked);
      return employee.get();
    }
    return null;
  }

  public boolean updateEmployeeInfo(Employee employee, int newInfo, String editableInfo) {
    switch (editableInfo) {
      case "Age":
        employee.setAge(newInfo);
        this.saveEmployee(employee);
        break;
      case "Monthly Rate":
        if (newInfo <= employee.getMonthlyRate())
          return false;
        else {
          employee.setMonthlyRate(newInfo);
          this.saveEmployee(employee);
        }
        break;
      case "SSS Contribution":
        employee.setSss(newInfo);
        this.saveEmployee(employee);
        break;
      case "Philhealth Contribution":
        employee.setPhilhealth(newInfo);
        this.saveEmployee(employee);
        break;
      default:
        throw new IllegalArgumentException("Invalid Editable Info");
    }
    return true;
  }

}
