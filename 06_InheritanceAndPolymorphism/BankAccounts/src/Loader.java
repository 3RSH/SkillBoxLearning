import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Scanner;

public class Loader {

  private static final HashMap<Integer, BankAccount> accounts = new HashMap<>();

  private static final String ACCOUNT = "BankAccount";
  private static final String DEPOSIT_ACCOUNT = "DepositAccount";
  private static final String CARD_ACCOUNT = "CardAccount";
  private static final String SIMPLE_TYPE = "ПРОСТОЙ";
  private static final String DEPOSIT_TYPE = "ДЕПОЗИТ";
  private static final String CARD_TYPE = "КАРТА";
  private static final String DEFAULT = "";
  private static final String BEGIN = "Введите команду(HELP - список команд): ";
  private static final String HEAD_LIST = "\nСписок счетов: \n";
  private static final String SUCCESS = "Операция прошла успешно.\n";
  private static final String FAIL = "Операция не прошла!\n";
  private static final String NO_ACC = "Счёт не выбран!\n";
  private static final String NO_TO_ACC = "Счёта №%d не существует!%n";
  private static final String HELP = "HELP";
  private static final String EXIT = "EXIT";
  private static final String LIST = "LIST";
  private static final String ADD_ACCOUNT = "ADD";
  private static final String ADD_DEPOSIT = "ADD DEPOSIT";
  private static final String ADD_CARD = "ADD CARD";
  private static final String GET_ACCOUNT = "ACC\\s[0-9]+";
  private static final String ACCOUNT_HEAD = "%nСчёт №%d (%s): %s руб.%n\t\t";
  private static final String ACCOUNT_CHARGE = "CHARGE\\s[0-9]+\\.?[0-9]{0,2}";
  private static final String ACCOUNT_WRITE_OFF = "WRITE OFF\\s[0-9]+\\.?[0-9]{0,2}";
  private static final String ACCOUNT_SEND = "SEND\\s[0-9]+\\s[0-9]+\\.?[0-9]{0,2}";

  private static boolean isRun = true; //маркер работы программы
  private static int accNum = 0; //маркер номера счёта в работе

  public static void main(String[] args) {
    Scanner scanner = new Scanner(System.in);
    String inAcc; //переменная заголовка итерации рабочего цикла
    String input; //переменная хранения введённой строки

    //рабочий цикл
    while (isRun) {
      if (accounts.containsKey(accNum)) {
        inAcc = String.format(ACCOUNT_HEAD,
            accNum,
            accountType(accounts.get(accNum)),
            accounts.get(accNum).getAccount());
      } else {
        inAcc = DEFAULT;
      }

      System.out.print(inAcc + BEGIN);
      input = scanner.nextLine();

      //ПОМОЩЬ
      if (input.matches(HELP)) {
        help();

        //ВЫХОД
      } else if (input.matches(EXIT)) {
        exit();

        //СИПСОК и "выход" из работы со счётом
      } else if (input.matches(LIST)) {
        list();

        //СОЗДАНИЕ Простого счёта
      } else if (input.matches(ADD_ACCOUNT)) {
        accounts.put(BankAccount.getCount() + 1, new BankAccount(BigDecimal.valueOf(0)));
        accNum = BankAccount.getCount();

        //СОЗДАНИЕ Депозитного счёта
      } else if (input.matches(ADD_DEPOSIT)) {
        accounts.put(BankAccount.getCount() + 1, new DepositAccount(BigDecimal.valueOf(0)));
        accNum = BankAccount.getCount();

        //СОЗДАНИЕ Карточного счёта
      } else if (input.matches(ADD_CARD)) {
        accounts.put(BankAccount.getCount() + 1, new CardAccount(BigDecimal.valueOf(0)));
        accNum = BankAccount.getCount();

        //ВЫБОР счёта
      } else if (input.matches(GET_ACCOUNT)) {
        accNum = Integer.parseInt(input.replaceAll("\\D", ""));

        if (!accounts.containsKey(accNum)) {
          System.out.printf(NO_TO_ACC, accNum);
          System.out.println();
        }

        //ПОПОЛНЕНИЕ счёта
      } else if (input.matches(ACCOUNT_CHARGE)) {
        Double sum = Double.parseDouble(input.replaceAll("CHARGE\\s", ""));

        System.out.println(opReport(deposit(accNum, sum)));

        //СПИСАНИЕ со счёта
      } else if (input.matches(ACCOUNT_WRITE_OFF)) {
        Double sum = Double.parseDouble(input.replaceAll("WRITE OFF\\s", ""));

        System.out.println(opReport(withdraw(accNum, sum)));

        //ПЕРЕВОД со счёта
      } else if (input.matches(ACCOUNT_SEND)) {
        int toAcc = Integer.parseInt(input.split("\\s")[1]);
        BigDecimal sum = BigDecimal.valueOf(Double.parseDouble(input.split("\\s")[2]));

        System.out.println(opReport(send(accNum, toAcc, sum)));
      }
    }
  }

  //ПОМОЩЬ
  private static void help() {
    StringBuilder help = new StringBuilder();

    help.append("\nСписок доступных команд:\n")
        .append("\tEXIT - выход из программы;\n")
        .append("\tHELP - список команд;\n")
        .append("\tLIST - вывод списка счетов;\n")
        .append("\tADD - создать простой счёт;\n")
        .append("\tADD DEPOSIT - создать депозитный счёт;\n")
        .append("\tADD CARD - создать карточный счёт;\n")
        .append("\tACC {номер счёта} - работа с конкретым счётом;\n\n")
        .append("- команды для работы со счётом: \n")
        .append("\t\tCHARGE {сумма} - пополнить счёт;\n")
        .append("\t\tWRITE OFF {сумма} - списать со счёта;\n")
        .append("\t\tSEND {номер счёта} {сумма} - перевести на указанный счёт;\n\n");

    System.out.print(help);
  }

  //ВЫХОД
  private static void exit() {
    isRun = false;
  }

  //СИПСОК
  private static void list() {
    StringBuilder list = new StringBuilder().append(HEAD_LIST);

    for (int key : accounts.keySet()) {
      list.append("\tСчёт №")
          .append(key)
          .append(" ")
          .append(" : ")
          .append(accounts.get(key).getAccount())
          .append(" руб. ;\n");
    }

    list.append("\n");
    System.out.print(list);
    accNum = 0;
  }

  //ПОПОЛНЕНИЕ
  private static boolean deposit(int accNum, Double sum) {
    if (accounts.containsKey(accNum)) {
      accounts.get(accNum).deposit(BigDecimal.valueOf(sum));
      return true;
    }

    System.out.print(NO_ACC);
    return false;
  }

  //СПИСАНИЕ
  private static boolean withdraw(int accNum, Double sum) {
    if (accounts.containsKey(accNum)) {
      return accounts.get(accNum).withdraw(BigDecimal.valueOf(sum));
    }
    System.out.print(NO_ACC);
    return false;
  }

  //ПЕРЕВОД
  private static boolean send(int accNum, int toAcc, BigDecimal sum) {
    if (!accounts.containsKey(accNum)) {
      System.out.print(NO_ACC);
      return false;
    } else if (!accounts.containsKey(toAcc)) {
      System.out.printf(NO_TO_ACC, toAcc);
      return false;
    }

    return accounts.get(accNum).send(accounts.get(toAcc), sum);
  }

  //РАПОРТ операционный
  private static String opReport(boolean res) {
    if (res) {
      return SUCCESS;
    }
    return FAIL;
  }

  //ТИП счёта
  private static String accountType(BankAccount account) {
    return switch (account.getClass().getSimpleName()) {
      case (ACCOUNT) -> SIMPLE_TYPE;
      case (DEPOSIT_ACCOUNT) -> DEPOSIT_TYPE;
      case (CARD_ACCOUNT) -> CARD_TYPE;
      default -> DEFAULT;
    };
  }
}
