import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Loader {

  public static void main(String[] args) throws Exception {
    long start = System.currentTimeMillis();

    List<String> numbers = new ArrayList<>();

    char[] letters = {'У', 'К', 'Е', 'Н', 'Х', 'В', 'А', 'Р', 'О', 'С', 'М', 'Т'};

    for (int regionCode = 1; regionCode < 100; regionCode++) {

      String regionStr = padNumber(regionCode, 2);
      
      for (int number = 1; number < 1000; number++) {

        String numberStr = padNumber(number, 3);
        StringBuilder builder = new StringBuilder();

        for (char firstLetter : letters) {

          for (char secondLetter : letters) {

            for (char thirdLetter : letters) {

              builder.append(firstLetter)
                  .append(numberStr)
                  .append(secondLetter)
                  .append(thirdLetter)
                  .append(regionStr)
                  .append("\n");
            }
          }
        }

        numbers.add(builder.toString());
      }
    }

    //Вывод номеров в несколько файлов из нескольких потоков(4-е потока, для примера)

    List<NumberGenerator> threads = new ArrayList<>();

    for (int i = 0; i < 4; i++) {
      threads.add(new NumberGenerator(i, numbers));
    }

    for (NumberGenerator nGen : threads) {
      nGen.start();
      nGen.join();
    }

    System.out.println((System.currentTimeMillis() - start) + " ms");

  }

  private static String padNumber(int number, int numberLength) {
    String numberStr = String.valueOf(number);
    int padSize = numberLength - numberStr.length();

    return (padSize < 1)
        ? numberStr
        : ("0".repeat(padSize) + numberStr);
  }
}
