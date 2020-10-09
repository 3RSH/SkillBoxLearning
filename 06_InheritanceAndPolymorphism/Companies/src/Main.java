import Company.Company;
import Company.Employee;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;

public class Main {

  //Коллекция компаний
  private static final HashMap<Integer, Company> companies = new HashMap<>();

  //Константы команд
  private static final String HELP = "HELP";
  private static final String EXIT = "EXIT";
  private static final String LIST = "LIST";
  private static final String ADD_COMPANY = "ADD";
  private static final String GET_COMPANY = "COMPANY\\s[0-9]+";
  private static final String INFO = "INFO";
  private static final String HIRE = "HIRE\\s[-A-Z]+";
  private static final String HIRE_ALL = "HIRE\\s[0-9]+\\s[-A-Z]+";
  private static final String FIRE = "FIRE\\s[-A-Z]+";
  private static final String FIRE_ALL = "FIRE\\s[0-9]+\\s[-A-Z]+";
  private static final String TOP_SALARY = "TOP\\s[0-9]+\\sSALARY";
  private static final String LOWEST_SALARY = "LOWEST\\s[0-9]+\\sSALARY";

  //Текстовые константы
  private static final String DEFAULT = "";
  private static final String BEGIN = "Введите команду(HELP - список команд): ";
  private static final String HEAD_LIST = "\nСписок компаний: \n";
  private static final String NO_COMPANY = "Компания не выбрана!\n";
  private static final String NO_TO_COMPANY = "Компании №%d не существует!%n";
  private static final String COMPANY_HEAD = "%nКомпания №%d доход: %s руб.%n\t\t";


  private static boolean isRun = true; //маркер работы программы
  private static int companyNum = 0; //маркер компании в работе

  public static void main(String[] args) {
    Scanner scanner = new Scanner(System.in);
    String inCompany; //переменная заголовка итерации рабочего цикла
    String input; //переменная хранения введённой строки

    //рабочий цикл
    while (isRun) {
      if (companies.containsKey(companyNum)) {
        inCompany = String.format(COMPANY_HEAD,
            companyNum,
            moneyFormat(companies.get(companyNum).getIncome()));
      } else {
        inCompany = DEFAULT;
      }

      System.out.print(inCompany + BEGIN);
      input = scanner.nextLine();

      //ПОМОЩЬ
      if (input.matches(HELP)) {
        help();

        //ВЫХОД
      } else if (input.matches(EXIT)) {
        exit();

        //СИПСОК и "выход" из работы со компанией
      } else if (input.matches(LIST)) {
        list();

        //СОЗДАНИЕ компании
      } else if (input.matches(ADD_COMPANY)) {
        add();

        //ВЫБОР компании
      } else if (input.matches(GET_COMPANY)) {
        select(input);

        //ИНФО о компании
      } else if (input.matches(INFO)) {
        info();

        //НАЙМ одного сотрудника
      } else if (input.matches(HIRE)) {
        hire(input);

        //НАЙМ группы сотрудников
      } else if (input.matches(HIRE_ALL)) {
        hireAll(input);

        //УВОЛЬНЕНИЕ сотрудника
      } else if (input.matches(FIRE)) {
        fire(input);

        //УВОЛЬНЕНИЕ группы сотрудников
      } else if (input.matches(FIRE_ALL)) {
        fireAll(input);

        //ТОП высоких зарплат
      } else if (input.matches(TOP_SALARY)) {
        topSalary(input);

        //ТОП низких зарплат
      } else if (input.matches(LOWEST_SALARY)) {
        lowestSalary(input);
      }
    }
  }

  //=============================================================================
  //ОСНОВНЫЕ(рабочие) МЕТОДЫ:

  //ПОМОЩЬ
  private static void help() {
    StringBuilder help = new StringBuilder();

    help.append("\nСписок доступных команд:\n")
        .append("\tEXIT - выход из программы;\n")
        .append("\tHELP - список команд;\n")
        .append("\tLIST - вывод списка компаний и выход из работы с компанией;\n")
        .append("\tADD - создать компанию;\n")
        .append("\tCOMPANY {номер компании} - работа с конкретой компанией;\n\n")
        .append("- команды для работы с компанией: \n")
        .append("\t\tHIRE {должность} - нанять сотрудника;\n")
        .append("\t\tHIRE {количество} {должность} - нанять несколько сотрудников;\n")
        .append("\t\tFIRE {должность} - уволить сотрудника;\n")
        .append("\t\tFIRE {количество} {должность} - уволить несколько сотрудников;\n")
        .append("\t\tINFO - вывод информации о компании;\n")
        .append("\t\tTOP {количество} SALARY - вывод списка высоких зарплат в компании;\n")
        .append("\t\tLOWEST {количество} SALARY - вывод списка низких зарплат в компании;\n")
        .append("- список возможных должностей: \n")
        .append("\t\tOPERATOR - зарплата фиксированная;\n")
        .append("\t\tMANAGER - зарплата фиксированная + ")
        .append("% от суммы заработанных для компании денег(% от 115 000 - 140 000 руб.);\n")
        .append("\t\tTOP-MANAGER - зарплата фиксированная + ")
        .append("% от фиксировамнной части, в случае превышения дохода компании ")
        .append("10 000 000 руб. ;\n\n");

    System.out.print(help);
  }

  //ВЫХОД
  private static void exit() {
    isRun = false;
  }

  //СИПСОК
  private static void list() {
    StringBuilder list = new StringBuilder().append(HEAD_LIST);

    for (int key : companies.keySet()) {
      list.append("\tКомпания №")
          .append(key)
          .append(" ")
          .append("доход: ")
          .append(moneyFormat(companies.get(key).getIncome()))
          .append(" руб. ;\n");
    }

    list.append("\n");
    System.out.print(list);
    companyNum = 0;
  }

  //СОЗДАНИЕ компании
  private static void add() {
    companies.put(Company.getCount() + 1, new Company());
    companyNum = Company.getCount();
  }

  //ВЫБОР компании
  private static void select(String input) {
    companyNum = Integer.parseInt(input.replaceAll("\\D", ""));

    if (!companies.containsKey(companyNum)) {
      System.out.printf(NO_TO_COMPANY, companyNum);
      System.out.println();
    }
  }

  //ИНФО о компании
  private static void info() {
    if (companies.containsKey(companyNum)) {
      System.out.println(companies.get(companyNum).getInfo());
    } else {
      System.out.println(NO_COMPANY);
    }
  }

  //НАЙМ одного сотрудника
  private static void hire(String input) {
    if (companies.containsKey(companyNum)) {
      input = input.replace("HIRE ", "");
      companies.get(companyNum).hire(input);
    } else {
      System.out.println(NO_COMPANY);
    }
  }

  //НАЙМ группы сотрудников
  private static void hireAll(String input) {
    if (companies.containsKey(companyNum)) {
      int amount = Integer.parseInt(input.replaceAll("[^0-9]+", ""));
      input = input.replaceAll("HIRE\\s[0-9]+\\s", "");

      companies.get(companyNum).hireAll(input, amount);
    } else {
      System.out.println(NO_COMPANY);
    }
  }

  //УВОЛЬНЕНИЕ сотрудника
  private static void fire(String input) {
    if (companies.containsKey(companyNum)) {
      input = input.replace("FIRE ", "");
      companies.get(companyNum).fire(input);
    } else {
      System.out.println(NO_COMPANY);
    }
  }

  //УВОЛЬНЕНИЕ группы сотрудников
  private static void fireAll(String input) {
    if (companies.containsKey(companyNum)) {
      int amount = Integer.parseInt(input.replaceAll("[^0-9]+", ""));

      input = input.replaceAll("FIRE\\s[0-9]+\\s", "");
      companies.get(companyNum).fireAll(input, amount);
    } else {
      System.out.println(NO_COMPANY);
    }
  }

  //ТОП высоких зарплат
  private static void topSalary(String input) {
    if (companies.containsKey(companyNum)) {
      StringBuilder output = new StringBuilder();
      int amount = Integer.parseInt(input.replaceAll("[^0-9]+", ""));

      for (Employee e : companies.get(companyNum).getTopSalaryStaff(amount)) {
        output.append("\t")
            .append(e.getClass().getSimpleName())
            .append(" - ")
            .append(moneyFormat(e.getMonthSalary()))
            .append(" руб. ;\n");
      }

      System.out.println(output);
    } else {
      System.out.println(NO_COMPANY);
    }
  }

  //ТОП низких зарплат
  private static void lowestSalary(String input) {
    if (companies.containsKey(companyNum)) {
      StringBuilder output = new StringBuilder();
      int amount = Integer.parseInt(input.replaceAll("[^0-9]+", ""));

      for (Employee e : companies.get(companyNum).getLowestSalaryStaff(amount)) {
        output.append("\t")
            .append(e.getClass().getSimpleName())
            .append(" - ")
            .append(moneyFormat(e.getMonthSalary()))
            .append(" руб. ;\n");
      }

      System.out.println(output);
    } else {
      System.out.println(NO_COMPANY);
    }
  }

  //=============================================================================
  //ДОПОЛНИТЕЛЬНЫЕ(вспомогательные) МЕТОДЫ:

  //ФОРМАТИРОВАНИЕ числа для печати
  private static String moneyFormat(long amount) {
    StringBuilder str = new StringBuilder();
    String num = String.valueOf(amount);

    if (num.charAt(0) == '-') {
      str.append('-');
      num = num.replaceAll("-", "");
    }

    char[] nums = num.toCharArray();
    List<Character> numsOut = new ArrayList<>();

    int numsCount = 0;

    for (int i = nums.length; i > 0; i--) {
      numsOut.add(nums[i - 1]);
      numsCount++;
      if (numsCount == 3) {
        numsCount = 0;
        numsOut.add(' ');
      }
    }

    if (numsOut.get(numsOut.size() - 1).equals(' ')) {
      numsOut.remove(numsOut.size() - 1);
    }

    for (int i = numsOut.size(); i > 0; i--) {
      str.append(numsOut.get(i - 1));
    }

    return str.toString();
  }
}

