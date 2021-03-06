import сompany.Company;
import employee.Employee;
import employee.Manager;
import employee.Operator;
import employee.TopManager;
import java.util.ArrayList;
import java.util.List;

public class Main {

  public static void main(String[] args) {

    //создаём компанию
    Company company = new Company();

    //нанимаем 180 операторов
    for (int i = 0; i < 180; i++) {
      company.hire(new Operator());
    }

    //создаём коллекцию для сотрудников
    List<Employee> employees1 = new ArrayList<>();

    //добавляем в коллекцию 80 менеджеров
    for (int i = 0; i < 80; i++) {
      employees1.add(new Manager());
    }

    //добавляем в коллекцию 10 топ-менеджеров
    for (int i = 0; i < 10; i++) {
      employees1.add(new TopManager());
    }

    //нанимаем всех сотрудников из коллекции в компанию
    company.hireAll(employees1);

    //смотрим доход компании
    System.out.println("Доход компании: " + company.getIncome());

    //выводим список самых высоких зарплат в компании
    for (Employee e : company.getTopSalaryStaff(15)) {
      System.out.println(e.getMonthSalary());
    }

    System.out.println("==========================================");

    //выводим список самых низких зарплат в компании
    for (Employee e : company.getLowestSalaryStaff(30)) {
      System.out.println(e.getMonthSalary());
    }

    //УВОЛЬНЯЕМ 50% СОТРУДНИКОВ КОМАПНИИ
    employees1.clear();

    for (Employee e : company.getStaff()) {
      employees1.add(e);

      if (employees1.size() == company.getStaff().size() / 2) {
        break;
      }
    }

    for (Employee e : employees1) {
      company.fire(e);
    }

    //смотрим доход компании
    System.out.println("Доход компании: " + company.getIncome());

    System.out.println("==========================================");
    System.out.println("==========================================");

    //выводим список самых высоких зарплат в компании
    for (Employee e : company.getTopSalaryStaff(15)) {
      System.out.println(e.getMonthSalary());
    }

    System.out.println("==========================================");

    //выводим список самых низких зарплат в компании
    for (Employee e : company.getLowestSalaryStaff(30)) {
      System.out.println(e.getMonthSalary());
    }

    //смотрим доход компании
    System.out.println("Доход компании: " + company.getIncome());
  }
}

