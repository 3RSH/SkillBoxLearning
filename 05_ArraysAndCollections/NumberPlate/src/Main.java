import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Scanner;
import java.util.TreeSet;

public class Main {

  //КОНСТАНТЫ для генерации номера
  public static final char[] CHARS = {'А', 'В', 'Е', 'К', 'М', 'Н', 'О',
      'Р', 'С', 'Т', 'У', 'Х'};
  public static final String[] NUMBERS = {"111", "222", "333", "444",
      "555", "666", "777", "888", "999"};
  public static final char[] REGION_AMPLIFICATION = {' ', '1', '7'};
  public static final String[] NUMBER_BY_PARTS = new String[6];

  //КОНСТАНТА для проверки ввода
  public static final String NUMBER_MASK = "[А-Я]\\d{3}[А-Я]{2}\\d{2,3}";
  //КОНСТАНТЫ для формирования ответа
  public static final String MESSAGE_ARR = "Поиск перебором: номер ";
  public static final String MESSAGE_BIN = "Бинарный поиск: номер ";
  public static final String MESSAGE_HASH = "Поиск в HashSet: номер ";
  public static final String MESSAGE_TREE = "Поиск в TreeSet: номер ";
  public static final String SUCCESS = " найден, поиск занял ";
  public static final String FAIL = " не найден, поиск занял ";

  public static void main(String[] args) {
    long t = System.currentTimeMillis();
    ArrayList<String> plates = vipNumber();
    t = System.currentTimeMillis() - t;
    System.out.println("vipNumber - " + t + " мс");
    t = System.currentTimeMillis();
    ArrayList<String> platesStr = vipNumberStr();
    t = System.currentTimeMillis() - t;
    System.out.println("vipNumberStr - " + t + " мс");
    compareAndCheckArrays(plates, platesStr);
    HashSet<String> platesHashSet = new HashSet<>(plates);
    TreeSet<String> platesTreeSet = new TreeSet<>(plates);
    Scanner scanner = new Scanner(System.in);

    for (;;) {
      StringBuilder message = new StringBuilder();
      String input = scanner.nextLine();
      boolean result;

      //выход из программы, если ввод не соответствует шаблону
      if (!input.matches(NUMBER_MASK)) {
        break;
      }

      message.append(MESSAGE_ARR).append(input);
      //форматируем ввод
      input = numFormatting(input);

      //поиск перебором
      long time = System.nanoTime();
      result = plates.contains(input);
      time = System.nanoTime() - time;
      message.append(result ? SUCCESS : FAIL)
          .append(time)
          .append(" нс\n")
          .append(MESSAGE_BIN)
          .append(input.replaceAll("\\s", ""));

      //бинарный поиск
      time = System.nanoTime();
      int i = Collections.binarySearch(plates, input);
      time = System.nanoTime() - time;
      message.append(i < 0 ? FAIL : SUCCESS)
          .append(time)
          .append(" нс\n")
          .append(MESSAGE_HASH)
          .append(input.replaceAll("\\s", ""));

      //поиск в HashSet
      time = System.nanoTime();
      result = platesHashSet.contains(input);
      time = System.nanoTime() - time;
      message.append(result ? SUCCESS : FAIL)
          .append(time)
          .append(" нс\n")
          .append(MESSAGE_TREE)
          .append(input.replaceAll("\\s", ""));

      //поиск в TreeSet
      time = System.nanoTime();
      result = platesTreeSet.contains(input);
      time = System.nanoTime() - time;
      message.append(result ? SUCCESS : FAIL)
          .append(time)
          .append(" нс");

      System.out.println(message);
    }
  }

  //сравнение и проверка массивов от разных генераторов
  private static void compareAndCheckArrays(ArrayList<String> arr1, ArrayList<String> arr2) {
    System.out.println(arr1.equals(arr2) ? "Массивы одинаковы." : "Массивы разные.");
    Collections.sort(arr2);
    System.out.println(arr1.equals(arr2) ? "Массивы отсортированы." : "Массивы не отсортированы.");
  }

  //генерация массива через StringBuilder
  private static ArrayList<String> vipNumber() {
    ArrayList<String> array = new ArrayList<>();

    for (char firstChar : CHARS) {
      StringBuilder number = new StringBuilder().append(firstChar);

      for (String num : NUMBERS) {
        number.append(num);

        for (char secondChar : CHARS) {
          number.append(secondChar);

          for (char thirdChar : CHARS) {
            number.append(thirdChar);

            for (char regAmplification : REGION_AMPLIFICATION) {
              number.append(regAmplification);

              for (int region = 1; region < 100; region++) {
                if ((region / 10) == 0) {
                  number.append('0').append(region);
                } else {
                  number.append(region);
                }
                array.add(number.toString());
                number.delete(7, 9);
              }

              number.deleteCharAt(6);
            }

            number.deleteCharAt(5);
          }

          number.deleteCharAt(4);
        }

        number.delete(1, 4);
      }
    }

    return array;
  }

  //генерация массива через String[]
  private static ArrayList<String> vipNumberStr() {
    ArrayList<String> array = new ArrayList<>();

    for (char firstChar : CHARS) {
      NUMBER_BY_PARTS[0] = String.valueOf(firstChar);

      for (String num : NUMBERS) {
        NUMBER_BY_PARTS[1] = num;

        for (char secondChar : CHARS) {
          NUMBER_BY_PARTS[2] = String.valueOf(secondChar);

          for (char thirdChar : CHARS) {
            NUMBER_BY_PARTS[3] = String.valueOf(thirdChar);

            for (char regAmplification : REGION_AMPLIFICATION) {
              NUMBER_BY_PARTS[4] = String.valueOf(regAmplification);

              for (int region = 1; region < 100; region++) {
                StringBuilder number = new StringBuilder();

                if ((region / 10) == 0) {
                  NUMBER_BY_PARTS[5] = "0" + region;
                } else {
                  NUMBER_BY_PARTS[5] = String.valueOf(region);
                }

                for (String str : NUMBER_BY_PARTS) {
                  number.append(str);
                }

                array.add(number.toString());
              }
            }
          }
        }
      }
    }

    return array;
  }

  //форматирование номера под формат его хранения в массиве
  private static String numFormatting(String num) {
    if (num.length() == 8) {
      num = num.replaceAll("(.{6})(.{2})", "$1 $2");
    }

    return num;
  }
}
