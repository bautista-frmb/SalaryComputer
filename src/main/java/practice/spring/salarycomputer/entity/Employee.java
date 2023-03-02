package practice.spring.salarycomputer.entity;

import java.text.DecimalFormat;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "employees")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Employee {

  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  private String id;
  private String firstName;
  private String  lastName;
  private int age;
  private double monthlyRate;
  private double sss;
  private double philhealth;
  @Transient
  @Setter(value = AccessLevel.PRIVATE)
  @Getter(value = AccessLevel.PRIVATE)
  private double grossSalary;

  public double getGrossSalary(int numberOfDaysWorked) {
    this.grossSalary = this.monthlyRate / 24d * numberOfDaysWorked;
    return this.grossSalary;
  }

  public double getNetSalary() {
    return this.grossSalary - (this.philhealth + this.sss);
  }
  
  public String[] displayInfo() {
    DecimalFormat decimalFormat = new DecimalFormat("#,###.00");
    return new String[] {
      "ID: " + this.id,
      "Name: " + this.firstName + " " + this.lastName,
      "Age: " + this.age,
      "Monthly rate: ₱" + decimalFormat.format(this.monthlyRate),
      "Gross salary: ₱" + decimalFormat.format(this.grossSalary),
      "SSS Contribution: ₱" + decimalFormat.format(this.sss),
      "PhilHealth Contribution: ₱" + decimalFormat.format(this.philhealth),
      "Net salary: ₱" + decimalFormat.format(this.getNetSalary()),
    };
  }

  public String[] toStringArray() {
    DecimalFormat decimalFormat = new DecimalFormat("#,###.00");
    return new String[] {
      "ID: " + this.id,
      "Name: " + this.firstName + " " + this.lastName,
      "Age: " + this.age,
      "Monthly rate: ₱" + decimalFormat.format(this.monthlyRate),
      "SSS Contribution: ₱" + decimalFormat.format(this.sss),
      "PhilHealth Contribution: ₱" + decimalFormat.format(this.philhealth)
    };
  }
}
