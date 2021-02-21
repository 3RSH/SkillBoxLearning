import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

public class NumberGenerator implements Callable<List<String>> {

  private final int regionCode;

  public NumberGenerator(int regionCode) {
    this.regionCode = regionCode;
  }

  private String padNumber(int number, int numberLength) {
    String numberStr = String.valueOf(number);
    int padSize = numberLength - numberStr.length();

    return (padSize < 1)
        ? numberStr
        : ("0".repeat(padSize) + numberStr);
  }

  @Override
  public List<String> call() {

    List<String> numbersAreas = new ArrayList<>();
    StringBuilder builder = new StringBuilder();

    char[] letters = {'У', 'К', 'Е', 'Н', 'Х', 'В', 'А', 'Р', 'О', 'С', 'М', 'Т'};

    String regionStr = padNumber(regionCode, 2);

    for (int number = 1; number < 1000; number++) {

      String numberStr = padNumber(number, 3);

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

      numbersAreas.add(builder.toString());
      builder.setLength(0);
    }

    return numbersAreas;
  }
}
