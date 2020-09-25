import java.util.ArrayList;
import java.util.Map;
import java.util.Scanner;
import java.util.TreeMap;
import java.util.TreeSet;

public class Loader {

  public static final TreeMap<String, String> phoneBook = new TreeMap<>();

  private static final String PHONE_MASK = "\\+*\\d[-()\\s\\d]+";
  private static final String NAME_MASK = "[A-zА-я]+[-._,/()A-zА-я\\s]*";
  private static final String ERROR = "Ошибка! Номер указан некорректно.";

  private static boolean isRun = true;

  public static void main(String[] args) {
    Scanner scanner = new Scanner(System.in);

    while (isRun) {
      String input = scanner.nextLine();

      if (input.equals("LIST")) { //вывод содержимого базы
        printPhoneBook();
      } else if (input.equals("EXIT")) { //выход из программы
        exit();
      } else if (input.matches(PHONE_MASK)) { //обработка ввода номера
        String phone = clearPhone(input);

        if (phoneIsIncorrect(phone)) {
          System.out.println(ERROR);
          continue;
        }

        phone = phoneFormatting(phone);

        if (hasPhone(phone)) {
          printMember(phoneBook.get(phone));
          continue;
        }

        System.out.print("Введите имя: ");
        input = scanner.nextLine();

        phoneBook.put(phone, input);
      } else if (input.matches(NAME_MASK)) { //обработка ввода имени
        if (hasName(input)) {
          printMember(input);
          continue;
        }

        System.out.print("Введите номер: ");
        String phone = clearPhone(scanner.nextLine());

        if (phoneIsIncorrect(phone)) {
          System.out.println(ERROR);
          continue;
        }

        phone = phoneFormatting(phone);

        if (hasPhone(phone)) {
          System.out.println("Такой номер уже есть в базе:");
          printMember(phoneBook.get(phone));
          continue;
        }

        phoneBook.put(phone, input);
      }
    }
  }

  private static void exit() { //выход из программы
    isRun = false;
  }

  private static void printMember(String name) { //вывод информации по контакту
    StringBuilder message = new StringBuilder();

    message.append(name).append(" :\n");

    for (String phone : searchPhones(name)) {
      message.append("\t").append(phonePrintFormat(phone)).append("\n");
    }

    message.append("\n");
    System.out.print(message);
  }

  private static void printPhoneBook() { //вывод phoneBook по порядку ИМЁН
    TreeSet<String> outputSet = new TreeSet<>();

    for (Map.Entry<String, String> entry : phoneBook.entrySet()) {
      outputSet.add(entry.getValue());
    }

    for (String member : outputSet) {
      printMember(member);
    }
  }

  private static String clearPhone(String phone) { //очистка номера
    return phone.replaceAll("\\D", "");
  }

  private static boolean phoneIsIncorrect(String phone) { //проверка номера
    boolean conditionA = !(phone.length() == 11 && phone.charAt(1) == '9');
    boolean conditionB = !(phone.length() == 10 && phone.charAt(0) == '9');

    return conditionA && conditionB;
  }

  private static String phoneFormatting(String num) { //форматирование номера для хранения
    if (num.length() == 11) {
      return num.substring(1);
    }

    return num;
  }

  private static String phonePrintFormat(String phone) { //форматирование номера для вывода
    return phone.replaceAll("(\\d{3})(\\d{3})(\\d{2})(\\d{2})", "+7 ($1) $2-$3-$4");
  }

  private static ArrayList<String> searchPhones(String name) { //поиск номеров по имени
    ArrayList<String> phones = new ArrayList<>();

    for (Map.Entry<String, String> entry : phoneBook.entrySet()) {
      if (name.equals(entry.getValue())) {
        phones.add(entry.getKey());
      }
    }

    return phones;
  }

  private static boolean hasPhone(String phone) { //проверка наличия номера в phoneBook
    for (Map.Entry<String, String> entry : phoneBook.entrySet()) {
      if (phone.equals(entry.getKey())) {
        return true;
      }
    }

    return false;
  }

  private static boolean hasName(String name) { //проверка налиичя имени в phoneBook
    for (Map.Entry<String, String> entry : phoneBook.entrySet()) {
      if (name.equals(entry.getValue())) {
        return true;
      }
    }

    return false;
  }
}
