package practice.spring.salarycomputer.controller;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttribute;

import jakarta.servlet.http.HttpSession;
import practice.spring.salarycomputer.entity.Employee;
import practice.spring.salarycomputer.service.EmployeeService;

@Controller
public class EmployeeController {

  private static final String EMPLOYEE = "employee";
  private static final String ERROR = "error";
  private static final String ADD_EMPLOYEE = "add_employee";
  private static final String UPDATE_EMPLOYEE = "update_employee";
  private static final Object EMPLOYEE_NOT_FOUND = "Employee not found";

  @Autowired
  private EmployeeService employeeService;

  @GetMapping("/")
  public String index(Model model) {
    model.addAttribute("employeeList", employeeService.getAllEmployees());
    return "index";
  }

  @GetMapping("/home")
  public String home() {
    return "home";
  }

  @PostMapping("/home")
  public String getEmployeeSalary(
    @RequestParam("firstName") String firstName,
    @RequestParam("lastName") String lastName,
    @RequestParam("numOfDaysWorked") int numOfDaysWorked,
    Model model
  ) {
    Employee employee = employeeService.getEmployeeSalary(firstName, lastName, numOfDaysWorked);
    if (employee != null) 
      model.addAttribute(EMPLOYEE, employee);
    else
      model.addAttribute(ERROR, EMPLOYEE_NOT_FOUND);
    return "home";
  }
  
  @GetMapping("/add-employee")
  public String addEmployee(Model model) {
    model.addAttribute("employeeToAdd", new Employee());
    return ADD_EMPLOYEE;
  }

  @PostMapping("/add-employee")
  public String addEmployee(@ModelAttribute("employeeToAdd") Employee employeeToAdd, BindingResult bindingResult, Model model) {
    if (bindingResult.hasErrors()) {
      model.addAttribute(ERROR, bindingResult.toString());
      return ADD_EMPLOYEE;
    }
    if (employeeService.getEmployee(employeeToAdd.getFirstName(), employeeToAdd.getLastName()).isPresent()) {
      model.addAttribute(ERROR, "Employee already exists");
      return ADD_EMPLOYEE;
    }
    employeeService.saveEmployee(employeeToAdd);
    model.addAttribute("addedEmployee", employeeToAdd);
    return ADD_EMPLOYEE;
  }

  @GetMapping("/update-employee")
  public String updateForm() {
    return UPDATE_EMPLOYEE;
  }

  @PostMapping("/update-employee/edit")
  public String findEmployee(
    @RequestParam("firstName") String firstName,
    @RequestParam("lastName") String lastName,
    Model model,
    HttpSession session
  ) {
    Optional<Employee> employee = employeeService.getEmployee(firstName, lastName);
    if (employee.isPresent()) {
      session.setAttribute(EMPLOYEE, employee.get());
      model.addAttribute(EMPLOYEE, employee.get());
    }
    else
      model.addAttribute(ERROR, EMPLOYEE_NOT_FOUND);
    return UPDATE_EMPLOYEE;
  }

  @RequestMapping(method = {RequestMethod.GET, RequestMethod.PUT}, value = "/update-employee/edit")
  public String updateEmployee(
    @SessionAttribute Employee employee,
    @RequestParam("newInfo") int newInfo,
    @RequestParam("editableInfo") String editableInfo,
    Model model
  ) {
    if (employee == null) {
      model.addAttribute(ERROR, "Unknown Error");
      return UPDATE_EMPLOYEE;
    }
    if (String.valueOf(newInfo).equals("")) return UPDATE_EMPLOYEE;
    try {
      if (employeeService.updateEmployeeInfo(employee, newInfo, editableInfo))
        model.addAttribute(EMPLOYEE, employee);
      else
        model.addAttribute(ERROR, "Must be higher than current monthly rate");
    } catch (IllegalArgumentException iae) {
      model.addAttribute(ERROR, iae.getMessage());
    }
    model.addAttribute("successMsg", editableInfo + " successfully changed");
    model.addAttribute(EMPLOYEE, employee);
    return UPDATE_EMPLOYEE;
  }

  @GetMapping("/delete-employee")
  public String deleteForm() {
    return "delete_employee";
  }

  @PostMapping("/delete-employee")
  public String deleteEmployee(
    @RequestParam("fromWhere") String fromWhere,
    @RequestParam("firstName") String firstName,
    @RequestParam("lastName") String lastName,
    Model model
  ) {
    if (employeeService.deleteEmployee(firstName, lastName) > 0) {
      model.addAttribute("deleted", true);
      model.addAttribute("deletedMsg", "Successfully deleted");
    }
    else
      model.addAttribute(ERROR, EMPLOYEE_NOT_FOUND + ". Cannot delete");
    if (fromWhere.equals("index")) return "redirect:/";
    else return "delete_employee";
  }

}
