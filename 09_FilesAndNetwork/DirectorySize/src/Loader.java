import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.DecimalFormat;
import java.util.Scanner;

class Loader {

  public static final String REQUEST = "\nВведите путь до папки:\n\t";
  public static final String INCORRECT_INPUT = "\tПуть неверен, или не существует!";
  public static final String[] UNITS = {"б", "Кб", "Мб", "Гб", "Тб"};
  public static final DecimalFormat OUTPUT_FORMAT = new DecimalFormat("#.#");
  public static final int OUTPUT_SIZE = 999;

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

      //через java.nio.file.Files
      System.out.println("Рассчёт через java.nio.file.Files:");

      try {
        Path pathF = Paths.get(path);

        if (pathF.isAbsolute() && Files.exists(pathF)) {

          //через Paths -> Path -> Files.walkFileTree()
          System.out.println(" - Рассчёт через Paths -> Path -> Files.walkFileTree():");
          System.out.printf("\tРазмер папки %s составляет %s\n"
              , pathF.toAbsolutePath(), formatSizeForPrint(getSizeOfDirectory(pathF)));

          //через Files -> Path -> File.walk() (без учёта размера файлов-директорий)
          System.out.println(" - Рассчёт через Files -> Path -> File.walk():");
          System.out.printf("\tРазмер папки %s составляет %s\n"
              , pathF.toAbsolutePath()
              , formatSizeForPrint(getSizeOfFiles(pathF)));
        } else {
          System.out.println(INCORRECT_INPUT);
        }
      } catch (IOException e) {
        e.printStackTrace();
        break;
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

  //через Paths -> Path -> Files.walkFileTree()
  private static long getSizeOfDirectory(Path path) throws IOException {

    MyFileVisitor visitor = new MyFileVisitor();

    Files.walkFileTree(path, visitor);
    return visitor.getSize();
  }

  //через Files -> Path -> File.walk() (без учёта размера файлов-директорий)
  private static long getSizeOfFiles(Path path) throws IOException {
    return Files.walk(path)
        .map(Path::toFile)
        .filter(file -> !file.isDirectory())
        .mapToLong(File::length)
        .sum();
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
