import java.io.FileNotFoundException;
import java.io.PrintWriter;

public class NumberGenerator extends Thread {

  private String path = "14_Performance/CarNumberGenerator/res/numbers#num.txt";

  public NumberGenerator(int index) {
    this.path = path.replaceFirst("#num", String.valueOf(index));
  }

  private static String padNumber(int number, int numberLength) {
    String numberStr = String.valueOf(number);
    int padSize = numberLength - numberStr.length();

    return (padSize < 1)
        ? numberStr
        : ("0".repeat(padSize) + numberStr);
  }

  @Override
  public void run() {

    char[] letters = {'У', 'К', 'Е', 'Н', 'Х', 'В', 'А', 'Р', 'О', 'С', 'М', 'Т'};

    try (PrintWriter writer = new PrintWriter(path)) {

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

          writer.write(builder.toString());
        }
      }

      writer.flush();
    } catch (FileNotFoundException e) {
      e.printStackTrace();
    }
  }
}
