package practice.spring.salarycomputer.repository;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

import practice.spring.salarycomputer.entity.Employee;

public interface EmployeeRepo extends CrudRepository<Employee, String> {

  public Optional<Employee> findByFirstNameAndLastName(String firstName, String lastName);

  public int deleteByFirstNameAndLastName(String firstName, String lastName);

}
