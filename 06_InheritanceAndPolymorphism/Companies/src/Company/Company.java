package Company;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Company {

  //компаратор сравнения сотрудников по зарплате
  private static final EmployeeComparator comparator = new EmployeeComparator();

  //коллекция сотрудников
  public final List<Employee> staff = new ArrayList<>();

  private long income = 0; //доход компании

  //найм ОДНОГО сотрудника
  public void hire(Employee employee) {
    if (!this.equals(employee.getCompany())) {
      staff.add(employee);
      employee.setCompany(this);
    }
  }

  //найм ГРУППЫ сотрудников
  public void hireAll(List<Employee> employees) {
    for (Employee employee : employees) {
      hire(employee);
    }
  }

  //увольнение ОДНОГО сотрудника
  public void fire(Employee employee) {
    if (this.equals(employee.getCompany())) {
      staff.remove(employee);
      employee.fire();
    }
  }

  //получение списка самых высоких зарплат
  public List<Employee> getTopSalaryStaff(int count) {
    List<Employee> list = new ArrayList<>();

    if (count > 0) { //проверяем запрос отрицательного размера
      count = correctCount(count);
      staff.sort(comparator);
      Collections.reverse(staff);
      list = staff.subList(0, count);
    }

    return list;
  }

  //получение списка самых низких зарплат
  public List<Employee> getLowestSalaryStaff(int count) {
    List<Employee> list = new ArrayList<>();

    if (count > 0) { //проверяем запрос отрицательного размера
      count = correctCount(count);
      staff.sort(comparator);
      list = staff.subList(0, count);
    }

    return list;
  }

  //корректировка размера запрошенного списка,
  //при его превышении размена коллекции сотрудников
  private int correctCount(int count) {
    return Math.min(count, staff.size());
  }

  public long getIncome() {
    return income;
  }

  protected void setIncome(long income) {
    this.income = income;
  }
}
