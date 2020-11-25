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

  //список расходов по организациям
  private static final HashMap<String, Double> expenseByOrganisations = new HashMap<>();
  private static Double arrival = 0.0; //общий приход
  private static Double expense = 0.0; //общий расход

  public static void main(String[] args) {
    try {
      //парсим файл CSV в список экземпляров класса Movement
      List<Movement> list = parse(FILE_PATH);

      //считаем общий расход и доход
      expense = list.stream().mapToDouble(Movement::getExpense).sum();
      arrival = list.stream().mapToDouble(Movement::getIncome).sum();

      System.out.printf("Сумма расходов: %s руб.\n", formatMoney(expense));
      System.out.printf("Сумма доходов: %s руб.\n", formatMoney(arrival));

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

    operations.remove(0);
    operations.forEach(s -> movements.add(getMovementFromStringCVS(s)));
    return movements;
  }


  //Парсинг строки операции в экземпляр класса Movement
  private static Movement getMovementFromStringCVS(String operation) {
    //переменные для конструктора Movement
    LocalDate date;
    String name;
    double income;
    double expense;
    String mcc;

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

    //инициализируем переменную date
    date = LocalDate.parse(data.get(3), dateFormatter);

    //инициализируем переменную name
    name = data.get(5).substring(16, 70);
    name = name.replaceAll("[\\\\/]+", "/").trim();
    name = name.substring(name.indexOf('/')).trim();
    name = name.replaceAll("/+", " ").trim();

    //инициализируем переменную income
    income = Double.parseDouble(data.get(6).replace(',', '.'));

    //инициализируем переменную expense
    expense = Double.parseDouble(data.get(7).replace(',', '.'));

    //инициализируем переменную mcc
    mcc = data.get(5).substring(data.get(5).length() - 7);

    //возвращаем экземпляр класса Movement
    return new Movement(date, name, income, expense, mcc);
  }

  //Форматирование суммы для вывода
  private static String formatMoney(Double amount) {
    DecimalFormat format = new DecimalFormat("###,###.00");
    return format.format(amount).replaceAll(",", " ");
  }
}
