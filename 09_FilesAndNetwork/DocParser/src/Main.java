import static java.nio.file.StandardCopyOption.REPLACE_EXISTING;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class Main {

  public static final String SITE = "https://lenta.ru";
  public static final String PARSE_TARGET = "09_FilesAndNetwork/DocParser/images";

  public static void main(String[] args) {
    try {
      //получаем документ из сайта
      Document doc = Jsoup.connect(SITE).get();
      //получем коллекцию элементов с тегом <img> (картинки)
      Elements elements = doc.select("img");

      //скачиваем файл по ссылке(параметр src) с каждого элемента,
      //печатая имена файлов в консоль
      for (Element element : elements) {
        System.out.println(CopyFileFromURL(element.attr("abs:src")));
      }

    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  //Скачивание файла по ссылке и возврат имени скачанного файла
  private static String CopyFileFromURL(String imageURL) throws IOException {
    URL url = new URL(imageURL);
    InputStream in = url.openStream();
    String fileName = imageURL.substring(imageURL.lastIndexOf('/'));

    if (fileName.contains("?")) {
      fileName = fileName.substring(0, fileName.indexOf('?'));
    }

    Files.copy(in, Path.of(PARSE_TARGET + fileName), REPLACE_EXISTING);
    return fileName.replaceAll("/", "");
  }
}
