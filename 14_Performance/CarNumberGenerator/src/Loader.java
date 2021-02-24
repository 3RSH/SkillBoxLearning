import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

public class Loader {

  private static final String PATH = "14_Performance/CarNumberGenerator/res/numbers.txt";

  public static void main(String[] args) throws Exception {

    long start = System.currentTimeMillis();

    //Создаём пул потоков (количество потоков зависит от количества ядер процессора)
    ExecutorService executorService = Executors.newFixedThreadPool(Runtime.getRuntime()
        .availableProcessors());

    List<Future<List<String>>> futures = new ArrayList<>();

    for (int regionNumber = 1; regionNumber < 100; regionNumber++) {

      futures.add(executorService.submit(new NumberGenerator(regionNumber)));
    }

    executorService.shutdown();
    executorService.awaitTermination(1, TimeUnit.HOURS);

    System.out.println("Время генерации номеров: " + (System.currentTimeMillis() - start) + " ms");
    start = System.currentTimeMillis();

    //Запись номеров в файл
    try (PrintWriter writer
        = new PrintWriter(PATH)) {

      for (var future : futures) {
        future.get().forEach(writer::write);
      }

      writer.flush();
    }

    System.out.println("Время записи в файл: " + (System.currentTimeMillis() - start) + " ms");
  }
}
