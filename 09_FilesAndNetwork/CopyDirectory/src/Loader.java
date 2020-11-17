import static java.nio.file.Files.copy;
import static java.nio.file.Files.exists;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Scanner;

public class Loader {

  public static final String REQUEST_SRC = "Введите путь к копируемой директории: ";
  public static final String REQUEST_DST = "Введите путь к директории для размещения копии: ";
  public static final String INCORRECT_INPUT = "Введён неверный, или несуществующий путь!";

  public static void main(String[] args) {

    Scanner scanner = new Scanner(System.in);

    for (;;) {

      System.out.println(REQUEST_SRC);
      String path = scanner.nextLine();
      Path pathSrc = Paths.get(path);

      if (!(pathSrc.isAbsolute() && exists(pathSrc))) {
        System.out.println(INCORRECT_INPUT);
        continue;
      }

      System.out.println(REQUEST_DST);
      path = scanner.nextLine();
      Path pathDst = Paths.get(path);

      if (!(pathDst.isAbsolute() && exists(pathDst))) {
        System.out.println(INCORRECT_INPUT);
        continue;
      }

      try {
        Files.walk(pathSrc)
            .forEach(p -> {
              try {
                copy(p, pathDst.resolve(pathSrc.getParent().relativize(p)));
              } catch (IOException e) {
                e.printStackTrace();
              }
            });
      } catch (IOException e) {
        e.printStackTrace();
      }
    }
  }
}
