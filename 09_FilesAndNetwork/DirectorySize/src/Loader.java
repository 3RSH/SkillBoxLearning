import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.DecimalFormat;
import java.util.Scanner;

class Loader {

  private static final String REQUEST = "\nВведите путь до папки:\n\t";
  private static final String INCORRECT_INPUT = "\tПуть неверен, или не существует!";
  private static final String[] UNITS = {"б", "Кб", "Мб", "Гб", "Тб"};
  private static final DecimalFormat OUTPUT_FORMAT = new DecimalFormat("#.#");
  private static final int OUTPUT_SIZE = 999;

  private static final Scanner scanner = new Scanner(System.in);

  public static void main(String[] args) {

    for (;;) {
      System.out.print(REQUEST);
      String path = scanner.nextLine();

      //через класс File
      System.out.println("Рассчёт через класс File:");

      File file = new File(path);

      if (file.exists() && file.isDirectory()) {
        System.out.printf("\tРазмер папки %s составляет %s\n"
            , file.getAbsolutePath(), formatSizeForPrint(getSizeOfDirectory(file)));
      } else {
        System.out.println(INCORRECT_INPUT);
      }

      //через Paths/Path/Files
      System.out.println("Рассчёт через Paths/Path/Files:");

      Path pathF = Paths.get(path);

      if (pathF.isAbsolute() && Files.exists(pathF)) {
        System.out.printf("\tРазмер папки %s составляет %s\n"
            , pathF.toAbsolutePath(), formatSizeForPrint(getSizeOfDirectory(pathF)));
      } else {
        System.out.println(INCORRECT_INPUT);
      }
    }
  }

  //через класс File
  private static long getSizeOfDirectory(File dir) {

    long size = 0;
    File[] files = dir.listFiles();

    if (files != null) {
      for (File f : files) {
        size += f.isDirectory()
            ? getSizeOfDirectory(f) + f.length()
            : f.length();
      }
    }

    size += dir.length();
    return size;
  }

  //через Paths/Path/Files
  private static long getSizeOfDirectory(Path path) {

    MyFileVisitor visitor = new MyFileVisitor();

    try {
      Files.walkFileTree(path, visitor);
    } catch (IOException e) {
      e.printStackTrace();
    }

    return visitor.getSize();
  }

  //форматирование результата (размера) для вывода
  private static String formatSizeForPrint(long size) {
    StringBuilder builder = new StringBuilder();

    double outputSize = (double) size;
    int rankCount = 0;

    while (outputSize > OUTPUT_SIZE) {
      outputSize /= 1024;
      rankCount++;
    }

    builder.append(OUTPUT_FORMAT.format(outputSize))
        .append(" ")
        .append(UNITS[rankCount]);

    return String.valueOf(builder);
  }
}
