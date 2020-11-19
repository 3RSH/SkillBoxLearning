import static java.nio.file.StandardCopyOption.REPLACE_EXISTING;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileVisitor;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import org.apache.commons.io.FileUtils;

public class Loader {

  public static final String REQUEST_SRC = "Введите путь к копируемой директории: ";
  public static final String REQUEST_DST = "Введите путь к директории для размещения копии: ";
  public static final String INCORRECT_INPUT = "Введён неверный, или несуществующий путь!";
  public static final String SUCCESS = "Директория %s скопирована в %s.\n";
  public static final String FAIL = "Копирование отменено.";
  public static final String REQUEST_METHOD_1 = "Выполнить копирование через Files.walk.copy (Y/N): ";
  public static final String REQUEST_METHOD_2 = "Выполнить копирование через Files.walkFileTree (Y/N): ";
  public static final String REQUEST_METHOD_3 = "Выполнить копирование через FileUtils.copyDirectory (Y/N): ";


  private static final Scanner scanner = new Scanner(System.in);

  public static void main(String[] args) {

    for (;;) {

      //запрос источника копирования
      System.out.println(REQUEST_SRC);
      String path = scanner.nextLine();
      File source = new File(path);

      if (!(source.isAbsolute() && source.exists())) {
        System.out.println(INCORRECT_INPUT);
        continue;
      }

      //запрос места размещения копии
      System.out.println(REQUEST_DST);
      path = scanner.nextLine();
      File target = new File(path);

      if (!target.isAbsolute()) {
        System.out.println(INCORRECT_INPUT);
        continue;
      }

      //выбор метода копирования
      if (requestMethod(REQUEST_METHOD_1)) {
        try {
          copyDirByFilesWalkCopy(source.toPath(), target.toPath());
          System.out.printf(SUCCESS, source, target);
        } catch (IOException e) {
          e.printStackTrace();
        }
      } else if (requestMethod(REQUEST_METHOD_2)) {
        try {
          copyDirByFilesWalkFileTree(source.toPath(), target.toPath());
          System.out.printf(SUCCESS, source, target);
        } catch (IOException e) {
          e.printStackTrace();
        }
      } else if (requestMethod(REQUEST_METHOD_3)) {
        try {
          copyDirByFileUtilsCopyDirectory(source, target);
          System.out.printf(SUCCESS, source, target);
        } catch (IOException e) {
          e.printStackTrace();
        }
      } else {
        System.out.println(FAIL);
      }
    }
  }

  //копирование директории через Files.walk.copy
  private static void copyDirByFilesWalkCopy(Path source, Path target) throws IOException {
    List<Path> paths = new ArrayList<>();

    Files.walk(source).forEach(paths::add);

    for (Path p : paths) {
      Files.copy(p, target.resolve(source.relativize(p)), REPLACE_EXISTING);
    }
  }

  //копирование директории через Files.walkFileTree
  private static void copyDirByFilesWalkFileTree(Path source, Path target) throws IOException {
    FileVisitor<Path> fileVisitor = new CopyDirFileVisitor(source, target);

    Files.walkFileTree(source, fileVisitor);
  }

  //копирование директории через FileUtils.copyDirectory (org.apache.commons.io)
  private static void copyDirByFileUtilsCopyDirectory(File source, File target) throws IOException {
    FileUtils.copyDirectory(source, target);
  }

  //запрос метода копирования
  private static boolean requestMethod(String request) {
    String answer;

    for (;;) {
      System.out.print(request);
      answer = scanner.nextLine();

      if (answer.equalsIgnoreCase("Y")) {
        return true;
      } else if (answer.equalsIgnoreCase("N")) {
        return false;
      }
    }
  }
}
