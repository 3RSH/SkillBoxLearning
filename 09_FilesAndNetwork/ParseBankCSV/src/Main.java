import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Main {

  //константа пути к файлу
  public static final String FILE_PATH = "09_FilesAndNetwork/files/movementList.csv";

  //константа шаблона строки операции
  public static final String STRING_PATTERN = "[А-яё ]+,\\d{20},[A-Z]{3}"
      + ",\\d{2}\\.\\d{2}\\.\\d{2},[A-Z0-9_]+,.+,[0-9,\"]+,[0-9,\"]+";

  //констаны для парсинга строки операции
  public static final int DATE_INDEX = 3;
  public static final int OPERATION_INDEX = 5;
  public static final int INCOME_INDEX = 6;
  public static final int EXPENSE_INDEX = 7;
  public static final int MCC_LENGTH = 7;
  public static final int NAME_FIELD_START = 16;
  public static final int NAME_FIELD_END = 70;

  public static void main(String[] args) {
    try {
      //парсим файл CSV в список экземпляров класса Movement
      List<Movement> list = parse(FILE_PATH);

      //считаем общий расход и доход
      Double expense = list.stream().mapToDouble(Movement::getExpense).sum();
      Double arrival = list.stream().mapToDouble(Movement::getIncome).sum();

      System.out.printf("Сумма расходов: %s руб.\n", formatMoney(expense));
      System.out.printf("Сумма доходов: %s руб.\n", formatMoney(arrival));

      //создаём список расходов по организациям
      HashMap<String, Double> expenseByOrganisations = new HashMap<>();

      //получаем список организаций, по который был расход
      list.stream().filter(movement -> movement.getExpense() != 0)
          .forEach(move -> expenseByOrganisations.put(move.getName(), 0.0));

      //заполняем полученный список значениями расходов
      for (String name : expenseByOrganisations.keySet()) {
        list.stream().filter(movement -> name.equals(movement.getName()))
            .forEach(movement -> expenseByOrganisations
                .put(name, (expenseByOrganisations.get(name) + movement.getExpense())));
      }

      System.out.println("\nСуммы расходов по организациям:");

      //выводим список расходов по организациям
      for (String k : expenseByOrganisations.keySet()) {
        System.out.printf("%s\t\t%s руб.\n", k, formatMoney(expenseByOrganisations.get(k)));
      }
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  //Парсинг файла выписки
  private static List<Movement> parse(String pathToMovements) throws IOException {
    List<Movement> movements = new ArrayList<>();
    List<String> operations = Files.readAllLines(Paths.get(pathToMovements));

    operations.stream().filter(str -> str.matches(STRING_PATTERN))
        .forEach(str -> movements.add(getMovementFromStringCVS(str)));
    return movements;
  }

  //Парсинг строки операции в экземпляр класса Movement
  private static Movement getMovementFromStringCVS(String operation) {
    ArrayList<String> data = new ArrayList<>();
    DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd.MM.yy");
    StringBuilder buffer = new StringBuilder();

    //разбиваем строку по запятым
    String[] details = operation.split(",");

    //обрабатываем кавычки и соединяем разделённые данные
    for (int i = 0; i < (details.length - 1); i++) {
      if (details[i].contains("\"")) {
        buffer.append(details[i]).append(',').append(details[i + 1]);
        details[i] = buffer.toString().replace("\"", "");
        details[i + 1] = "";
        buffer.setLength(0);
      }
    }

    //получаем чистый массив данных
    for (String str : details) {
      if (!str.equals("")) {
        data.add(str);
      }
    }

    //Инициализируем переменные для конструктора Movement:
    // - инициализируем переменную date
    LocalDate date = LocalDate.parse(data.get(DATE_INDEX), dateFormatter);

    // - инициализируем переменную name
    String name = data.get(OPERATION_INDEX).substring(NAME_FIELD_START, NAME_FIELD_END);
    name = name.replaceAll("[\\\\/]+", "/").trim();
    name = name.substring(name.indexOf('/')).trim();
    name = name.replaceAll("/+", " ").trim();

    // - инициализируем переменную income
    double income = Double.parseDouble(data.get(INCOME_INDEX).replace(',', '.'));

    // - инициализируем переменную expense
    double expense = Double.parseDouble(data.get(EXPENSE_INDEX).replace(',', '.'));

    // - инициализируем переменную mcc
    String mcc = data.get(OPERATION_INDEX)
        .substring(data.get(OPERATION_INDEX).length() - MCC_LENGTH);

    //возвращаем экземпляр класса Movement
    return new Movement(date, name, income, expense, mcc);
  }

  //Форматирование суммы для вывода
  private static String formatMoney(Double amount) {
    DecimalFormat format = new DecimalFormat("###,###.00");
    return format.format(amount).replaceAll(",", " ");
  }
}
