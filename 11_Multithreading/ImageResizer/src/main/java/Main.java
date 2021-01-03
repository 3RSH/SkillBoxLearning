import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class Main {

  private static final int newWidth = 300;

  public static void main(String[] args) {
    String srcFolder = "/users/user/Desktop/src";
    String dstFolder = "/users/user/Desktop/dst";

    File srcDir = new File(srcFolder);

    File[] files = srcDir.listFiles();

    for (File[] file : getFilesByCores(files)) {
      long start = System.currentTimeMillis();
      ImageResizer resizer = new ImageResizer(file, newWidth, dstFolder, start);
      new Thread(resizer).start();
    }
  }

  //Получение массива списков файлов разделённых по количеству ядер процессора
  private static List<File[]> getFilesByCores(File[] files) {

    //количество ядер процессора
    int procCount = Runtime.getRuntime().availableProcessors();

    //массив для записи результатов
    List<File[]> filesByProc = new ArrayList<>();

    if (files != null) {
      //длина массива на ядро
      int length = (int) Math.ceil((double) files.length / procCount);

      //индекс начала считывания из исходного массива files
      int startIndex = 0;

      //получаем подмассивы и записываем их в filesByProc
      for (int i = 0; i < procCount; i++) {

        File[] filesPart = new File[length];

        if (filesPart.length < (files.length - startIndex)) {
          System.arraycopy(files, startIndex, filesPart, 0, filesPart.length);
          startIndex += length;
        } else {
          System.arraycopy(files, startIndex, filesPart, 0, files.length - startIndex);
        }

        filesByProc.add(filesPart);
      }
    }

    //возвращаем результирующий массив
    return filesByProc;
  }
}
