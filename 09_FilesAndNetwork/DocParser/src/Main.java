import static java.nio.file.StandardCopyOption.REPLACE_EXISTING;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.FileChannel;
import java.nio.channels.ReadableByteChannel;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class Main {

  public static final String SITE = "https://lenta.ru";
  public static final String PARSE_TARGET_IO = "09_FilesAndNetwork/DocParser/images/JavaIO/";
  public static final String PARSE_TARGET_NIO = "09_FilesAndNetwork/DocParser/images/JavaNIO/";
  public static final String ERROR_DOWNLOAD = "Файл %s не скачан.\n";

  public static void main(String[] args) {
    try {
      //получаем документ из сайта
      Document doc = Jsoup.connect(SITE).get();
      //получем коллекцию элементов с тегом <img> (картинки)
      Elements elements = doc.select("img");

      //скачиваем файл по ссылке(параметр src) с каждого элемента,
      //печатая имена файлов в консоль:

      //- через Java.IO
      System.out.println("Copy file by Java.IO:");
      elements.forEach(Main::copyFileByJavaIO);

      //- через Java.NIO
      System.out.println("\nCopy file by Java.NIO:");
      elements.forEach(Main::copyFileByJavaNIO);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  //Скачивание файла через Java.IO
  private static void copyFileByJavaIO(Element element) {
    String imageURL = element.attr("abs:src");
    String fileName = getFileNameFromURL(imageURL);

    //try (...) - для корректного закрытия потока InputStream
    try (InputStream in = new URL(imageURL).openStream()) {
      Files.copy(in, Path.of(PARSE_TARGET_IO + fileName), REPLACE_EXISTING);
      System.out.println(fileName);
    } catch (IOException ex) {
      System.out.println(exceptionMessage(ex, fileName));
    }
  }

  //Скачивание файла через Java.NIO
  private static void copyFileByJavaNIO(Element element) {
    String imageURL = element.attr("abs:src");
    String fileName = getFileNameFromURL(imageURL);

    //try (...) - для корректного закрытия потока InputStream(new URL(imageURL).openStream()))
    try (ReadableByteChannel readableByteChannel = Channels
        .newChannel(new URL(imageURL).openStream())) {

      //try (...) - для корректного закрытия потока OutputStream
      try (FileOutputStream outputStream = new FileOutputStream(PARSE_TARGET_NIO + fileName)) {
        FileChannel fileChannel = outputStream.getChannel();

        fileChannel.transferFrom(readableByteChannel, 0, Long.MAX_VALUE);
        System.out.println(fileName);
      }
    } catch (IOException ex) {
      System.out.println(exceptionMessage(ex, fileName));
    }
  }

  //Получение имени файла из URL
  private static String getFileNameFromURL(String url) {
    String fileName = url.substring(url.lastIndexOf('/') + 1);

    if (fileName.contains("?")) {
      fileName = fileName.substring(0, fileName.indexOf('?'));
    }

    return fileName;
  }

  //Формируем сообщение при ошибке копирования файла
  private static String exceptionMessage(Exception ex, String fileName) {
    StringBuilder builder = new StringBuilder().append(ex.getClass().getCanonicalName())
        .append("\n");

    Arrays.stream(ex.getStackTrace()).forEach(s -> builder.append(s).append("\n"));
    builder.append(String.format(ERROR_DOWNLOAD, fileName));
    return builder.toString();
  }
}
