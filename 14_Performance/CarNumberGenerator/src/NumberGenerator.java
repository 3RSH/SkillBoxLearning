import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.List;

public class NumberGenerator extends Thread {

  private final List<String> numbers;
  private String path = "14_Performance/CarNumberGenerator/res/numbers#num.txt";

  public NumberGenerator(int index, List<String> numbers) {
    this.path = path.replaceFirst("#num", String.valueOf(index));
    this.numbers = numbers;
  }

  @Override
  public void run() {

    try (PrintWriter writer = new PrintWriter(path)) {
      numbers.forEach(writer::write);
      writer.flush();

    } catch (FileNotFoundException e) {
      e.printStackTrace();
    }
  }
}
