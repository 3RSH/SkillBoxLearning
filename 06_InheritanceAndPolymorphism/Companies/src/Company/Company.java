package Company;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class Company {

  //компаратор класса
  private static final Comparator<Employee> comparator = new EmployeeComparator();

  //коллекция сотрудников
  private final List<Employee> staff = new ArrayList<>();

  //найм ОДНОГО сотрудника
  public void hire(Employee employee) {
    if (!this.equals(employee.getCompany())) {
      employee.setCompany(this);
      staff.add(employee);
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
      employee.fire();
      staff.remove(employee);
    }
  }

  //получение списка самых высоких зарплат
  public List<Employee> getTopSalaryStaff(int count) {
    return getSortedList(comparator.reversed(), count);
  }

  //получение списка самых низких зарплат
  public List<Employee> getLowestSalaryStaff(int count) {
    return getSortedList(comparator, count);
  }

  //получение копии списка сотрудников
  //для безопасной работы с ним
  public List<Employee> getStaff() {
    return new ArrayList<>(staff);
  }

  //получение дохода компании
  public long getIncome() {
    long income = 0;

    //первичный рассчёт, до начисления бонусов
    for (Employee employee : getStaff()) {
      if (employee instanceof EmployeeGenerateIncome) {
        income += ((EmployeeGenerateIncome) employee).getGeneratedIncome();
      }
    }

    return income; //возвращаем актуальный доход
  }

  //получение упорядоченного списка определённой длины
  private List<Employee> getSortedList(Comparator<Employee> comparator, int count) {
    List<Employee> list = getStaff();
    list.sort(comparator);
    return list.subList(0, (count < 0)  //проверка отрицательной длины:
        ? 0                             // - список нулевой длины(пустой);
        : Math.min(count, list.size()));// - проверка превышения длины:
                                        //         = выбираем наименьшее значение;
  }
}
