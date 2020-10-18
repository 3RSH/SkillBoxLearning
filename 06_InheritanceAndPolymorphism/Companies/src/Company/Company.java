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

  //получение актуального дохода
  //с корректировкой ЗП сотрудникам,
  //у которых ЗП зависит от дохода
  //НЕОБХОДИМО ДЕЛАТЬ ВСЕГДА, ПРОСЛЕ НАЙМА/УВОЛЬНЕНИЯ!!!
  public long getIncome() {
    long income = 0;
    long goodIncome = TopManager.getGoodIncome();
    int bonus = (int) (TopManager.getTopManagerFixSalary()
        * TopManager.getTopManagerPercent());

    //первичный рассчёт, до начисления бонусов
    for (Employee e : getStaff()) {
      income += e.getIncome();
    }

    //ищем экземпляры класса TopManager:
    for (Employee e : getStaff()) {
      //если находим, топ работаем с ЕГО методами:
      if (e.getClass().equals(TopManager.class)) {
        //сбрасываем ЗП до фиксированной части;
        ((TopManager) e).resetMonthSalary();
        //проверяем состояние дохода,
        //и если он более контрольного значения
        //класса TopManager:
        if (income > goodIncome) {
          income -= bonus; // - вычитаем бонус из дохода,
          //и добавляем к ЗП экземпляра класса TopManager;
          ((TopManager) e).addMonthSalary(bonus);
        }
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
