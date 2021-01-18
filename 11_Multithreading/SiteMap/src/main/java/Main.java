import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Set;
import java.util.TreeSet;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.concurrent.ForkJoinPool;

public class Main {

  //ссылка на анализируемый сайт
  public static final String URL = "http://sendel.ru";

  //путь сохранения результата
  private static final String TARGET_PATH = "out/";

  //массив сбора ссылок
  private static final CopyOnWriteArraySet<String> links = new CopyOnWriteArraySet<>();

  public static void main(String[] args) {

    //запускаем пул потоков ForkJoinPool и ждём завершения его работы
    new ForkJoinPool()
        .invoke(new LinksWriter(getDomain(URL), getDomain(URL), links));

    //формируем карту сайта из списка полученных ссылок
    Set<String> map = getMap(links);

    //записываем карту сайта в файл в требуемом формате
    writeSiteMapToFile(getDomain(URL), TARGET_PATH, map);

  }

  //Получение карты сайта из списка ссылок сайта
  private static Set<String> getMap(Set<String> links) {
    Set<String> map = new TreeSet<>();

    for (String link : links) {
      String url = link;

      //чистим ссылки от условий отображения страницы (информация после знака '?')
      if (link.matches(".+\\?.*")) {
        url = link.substring(0, link.indexOf('?'));
      }

      //убираем дублирование ссылок из-за отсутствия/наличия
      //символа '/' в конце ссылки
      if (url.lastIndexOf('/') == url.length() - 1) {
        map.add(url);
      } else {
        map.add(url.matches(".+\\.[a-z0-9]{3,4}")
            ? url
            : url + "/");
      }
    }
    return map;
  }

  //Запись карты сайта в файл
  private static void writeSiteMapToFile(String siteUrl, String path, Set<String> map) {

    //формируем понный путь результирующего файла
    StringBuilder fileName = new StringBuilder()
        .append(path).append("SiteMapOf_")
        .append(siteUrl, siteUrl.indexOf('/') + 2, siteUrl.lastIndexOf('/'))
        .append(".txt");

    try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileName.toString()))) {
      for (String url : map) {
        //форматируем и записываем строки ссылки в файл
        writer.append(formatUrl(url, siteUrl));
      }
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  //Форматирование строки ссылки перед записью в файл
  //, с учётом вложенности
  private static String formatUrl(String url, String domain) {

    String halfUrl = url.replaceAll(domain, "");

    int tabCount = 0;

    for (char c : halfUrl.toCharArray()) {
      if (c == '/') {
        tabCount++;
      }
    }

    if (halfUrl.matches(".+[^/]")) {
      tabCount++;
    }

    StringBuilder out = new StringBuilder()
        .append("\t".repeat(Math.max(0, tabCount)))
        .append(url).append("\n");

    return out.toString();
  }

  //Форматирование ссылки на сайт
  private static String getDomain(String siteUrl) {
    return siteUrl.matches(".+/")
        ? siteUrl
        : siteUrl + "/";
  }
}
