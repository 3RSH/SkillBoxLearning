import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.DecimalFormat;
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
      List<String> operations = Files.readAllLines(Paths.get(FILE_PATH));

      operations.remove(0);
      operations.forEach(Main::parseMovementString);

      System.out.printf("Сумма расходов: %s руб.\n", formatMoney(expense));
      System.out.printf("Сумма доходов: %s руб.\n", formatMoney(arrival));
      System.out.println("\nСуммы расходов по организациям:");

      for (String k : expenseByOrganisations.keySet()) {
        System.out.printf("%s\t\t%s руб.\n", k, formatMoney(expenseByOrganisations.get(k)));
      }

    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  //Извлечение из строки операции необходимой информации
  private static void parseMovementString(String operation) {

    //т.к. кавычки возможны только в последнем столбце,
    //разбиваем строку по запятым с лимитом
    String[] details = operation.split(",", 8);

    //вычленяем информацию об организации из "Описания операции"
    String organization = details[5].substring(16, 70).trim();

    //подготавливаем строку "Прихода" для парсинга в Double
    String in = details[6].replaceAll("\"", "")
        .replaceAll(",", ".");

    //подготавливаем строку "Расхода" для парсинга в Double
    String out = details[7].replaceAll("\"", "")
        .replaceAll(",", ".");

    //инкрементируем общий приход
    arrival += Double.parseDouble(in);

    //инкрементируем общий расход
    expense += Double.parseDouble(out);

    //вычленяем название организации из информации об организации
    if (organization.contains("/")) {
      organization = organization.substring(organization.lastIndexOf("/") + 1).trim();
    } else {
      organization = organization.substring(organization.lastIndexOf("\\") + 1).trim();
    }

    //заполняем список расходов по организациям
    if (expenseByOrganisations.containsKey(organization)) {
      Double exp = Double.parseDouble(out) + expenseByOrganisations.get(organization);
      expenseByOrganisations.put(organization, exp);
    } else {
      expenseByOrganisations.put(organization, Double.parseDouble(out));
    }
  }

  //Форматирование суммы для вывода
  private static String formatMoney(Double amount) {
    DecimalFormat format = new DecimalFormat("###,###.00");
    return format.format(amount).replaceAll(",", " ");
  }
}
