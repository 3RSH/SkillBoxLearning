package Company;

import java.util.ArrayList;
import java.util.List;

public class Company {

  private static int count = 0; //счётчик

  //текстовые константы, для найма/увольнения
  private static final String[] POSITIONS = {"operator", "manager", "top-manager"};
  private static final String[] PRINT_POST_POSITIONS = {"", "а", "ов"};
  private static final String[] PRINT_POSITIONS = {" Оператор", " Менеджер", " Топ-менеджер"};

  //компаратор сравнения сотрудников по зарплате
  private static final EmployeeComparator comparator = new EmployeeComparator();

  //константы фиксированных частей зарплат
  private final int operatorFixSalary = 20000;
  private final int managerFixSalary = 40000;
  private final int topManagerFixSalary = 60000;

  //константы процентов
  protected final float managerPercent = 0.05f;
  protected final float topManagerPercent = 1.5f;

  //константа порогового значения дохода,
  //для начисления бонусов
  protected final long goodIncome = 10000000;

  //коллекция сотрудников
  private final List<Employee> staff = new ArrayList<>();
  private Employee employee; //переменная-буфер Employee для найма сотрудника
  private long income = 0; //доход компании

  public Company(){
    count++;
  }

  //=============================================================================
  //ОСНОВНЫЕ(рабочие) МЕТОДЫ:

  //найм ОДНОГО сотрудника
  public void hire(String post) {
    StringBuilder output = new StringBuilder();

    for (int i = 0; i < POSITIONS.length; i++) {
      if (POSITIONS[i].equals(post.toLowerCase())) {
        createEmployee(i);
        staff.add(employee);
        recountIncome();
        output.append("Нанят")
            .append(PRINT_POSITIONS[i])
            .append(".");
        System.out.println(output);
      }
    }
  }

  //найм ГРУППЫ сотрудников определённой должности
  public void hireAll(String post, int amount) {
    StringBuilder output = new StringBuilder();

    for (int i = 0; i < POSITIONS.length; i++) {
      if (POSITIONS[i].equals(post.toLowerCase())) {
        for (int j = 0; j < amount; j++) {
          createEmployee(i);
          staff.add(employee);
        }
        output.append("Нанято: ")
            .append(amount)
            .append(PRINT_POSITIONS[i])
            .append(postPosition(amount))
            .append(".");
        recountIncome();
        System.out.println(output);
      }
    }
  }

  //увольнение ОДНОГО сотрудника
  public boolean fire(String post) {
    StringBuilder output = new StringBuilder();

    for (int i = 0; i < POSITIONS.length; i++) {
      if (POSITIONS[i].equals(post.toLowerCase())) {
        post = post.replaceAll("[^A-Z]+", "").toLowerCase();

        for (int j = 0; j < staff.size(); j++) {
          if (staff.get(j).getClass().getSimpleName().toLowerCase().equals(post)) {
            staff.get(j).setCompany(null);
            staff.remove(j);
            recountIncome();
            output.append("Уволен ")
                .append(PRINT_POSITIONS[i])
                .append(".");
            System.out.println(output);
            return true;
          }
        }
      }
    }
    return false;
  }

  //увольнение ГРУППЫ сотрудников определённой должности
  public void fireAll(String post, int amount) {
    StringBuilder output = new StringBuilder();
    int count = 0;

    for (int i = 0; i < amount; i++) {
      count = fire(post) ? (count + 1) : count;
    }

    for (int i = 0; i < POSITIONS.length; i++) {
      if (POSITIONS[i].equals(post.toLowerCase())) {
        output.append("Уволено: ")
            .append(count)
            .append(PRINT_POSITIONS[i])
            .append(postPosition(amount))
            .append(".");
        System.out.println(output);
      }
    }
  }

  //получение списка самых высоких зарплат
  public List<Employee> getTopSalaryStaff(int count) {
    List<Employee> list = new ArrayList<>();
    int index = staff.size() - 1;

    if (count > 0) { //проверяем запрос отрицательного размера
      count = correctCount(count);

      for (int i = 0; i < count; i++) {
        list.add(staff.get(index));
        index--;
      }

      return list;
    }

    return null;
  }

  //получение списка самых низких зарплат
  public List<Employee> getLowestSalaryStaff(int count) {
    List<Employee> list = new ArrayList<>();

    if (count > 0) { //проверяем запрос отрицательного размера
      count = correctCount(count);

      for (int i = 0; i < count; i++) {
        list.add(staff.get(i));
      }

      return list;
    }

    return null;
  }

  //получение информации о составе компании
  public String getInfo() {
    int opCount = 0;
    int manCount = 0;
    int tManCount = 0;
    StringBuilder info = new StringBuilder()
        .append("\tОбщяя численность сотрудников: ")
        .append(staff.size())
        .append(";\n");

    for (Employee value : staff) {
      for (int i = 0; i < POSITIONS.length; i++) {
        String position = value.getClass().getSimpleName().toLowerCase();
        String pos = POSITIONS[i].replaceAll("[^a-z]+", "");

        if (position.equals(pos)) {
          switch (i) {
            case 0 -> opCount++;
            case 1 -> manCount++;
            case 2 -> tManCount++;
          }
        }
      }
    }

    info.append("\t\tОператоров - ")
        .append(opCount)
        .append(";\n")
        .append("\t\tМенеджеров - ")
        .append(manCount)
        .append(";\n")
        .append("\t\tТоп-менеджеров - ")
        .append(tManCount)
        .append(";\n");

    return info.toString();
  }

  //=============================================================================
  //ДОПОЛНИТЕЛЬНЫЕ(вспомогательные) МЕТОДЫ:

  //создание требуемого сотрудника
  private void createEmployee(int index) {
    switch (index) {
      case 0 -> employee = new Operator();
      case 1 -> employee = new Manager();
      case 2 -> employee = new TopManager();
    }
  }

  //склонение названий должностей от количества
  private String postPosition(int amount) {
    int i = amount % 10;
    int j = amount % 100;
    boolean check1 = i == 1 || i == 2 || i == 3 || i == 4;
    boolean check2 = j != 11 && j != 12 && j != 13 && j != 14;
    if (check1) {
      if (check2) {
        if (i == 1) {
          return PRINT_POST_POSITIONS[0];
        } else {
          return PRINT_POST_POSITIONS[1];
        }
      }
    }
    return PRINT_POST_POSITIONS[2];
  }

  //корректировка размера запрошенного списка,
  //при его превышении размена коллекции сотрудников
  private int correctCount(int count) {
    return Math.min(count, staff.size());
  }

  //перерасчёт компании при изменении состава
  private void recountIncome() {
    income = 0;

    for (int i = 0; i < staff.size(); i++) {
      staff.get(i).setCompany(this);
      income -= staff.get(i).getMonthSalary();
    }

    staff.sort(comparator);
  }

  //=============================================================================
  //ГЕТТЕРЫ:

  public static int getCount() {
    return count;
  }

  public long getIncome() {
    return income;
  }

  protected int getOperatorFixSalary() {
    return operatorFixSalary;
  }

  protected int getManagerFixSalary() {
    return managerFixSalary;
  }

  protected int getTopManagerFixSalary() {
    return topManagerFixSalary;
  }

  protected float getManagerPercent() {
    return managerPercent;
  }

  protected float getTopManagerPercent() {
    return topManagerPercent;
  }

  protected long getGoodIncome() {
    return goodIncome;
  }

  //=============================================================================
  //СЕТТЕРЫ:

  protected void setIncome(long income) {
    this.income = income;
  }
}
