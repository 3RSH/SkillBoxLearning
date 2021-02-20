import java.util.ArrayList;
import java.util.List;

public class Loader {

  public static void main(String[] args) throws Exception {

    long start = System.currentTimeMillis();

    //Вывод номеров в несколько файлов из нескольких потоков(4-е потока, для примера)

    List<NumberGenerator> threads = new ArrayList<>();

    for (int i = 0; i < 4; i++) {
      threads.add(new NumberGenerator(i));
    }

    for (NumberGenerator nGen : threads) {
      nGen.start();
      nGen.join();
    }

    System.out.println((System.currentTimeMillis() - start) + " ms");
  }
}
