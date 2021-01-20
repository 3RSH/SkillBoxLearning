import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.concurrent.ForkJoinTask;
import java.util.concurrent.RecursiveAction;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.Marker;
import org.apache.logging.log4j.MarkerManager;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

public class LinksWriter extends RecursiveAction {

  //инициализация Логгера
  private static final Logger LOGGER = LogManager.getLogger(Main.class);

  //инициализация маркеров для Логгера
  private static final Marker EXCEPTION_MARKER = MarkerManager.getMarker("EXCEPTION");
  private static final Marker TREAD_INFO_MARKER = MarkerManager
      .getMarker("TREAD_INFO");

  //переменная проверки домена ссылки
  private final String domain;

  //переменная рабочей ссылки
  private final String url;

  //переменная-ссылка на массив сбора ссылок
  private final CopyOnWriteArraySet<String> links;

  public LinksWriter(String domain, String url, CopyOnWriteArraySet<String> links) {
    this.domain = domain;
    this.url = url;
    this.links = links;
  }

  @Override
  protected void compute() {

    //добавляем рабочую ссылку в массив сбора ссылок
    links.add(url);

    //мониторинг работы потоков
    LOGGER.info(TREAD_INFO_MARKER, Thread.currentThread().getName()
            + ": всего ссылок найдено " + links.size());

    //прерываем поток для избежания блокировки сайтом
    try {
      Thread.sleep(200);
    } catch (InterruptedException e) {
      LOGGER.info(EXCEPTION_MARKER, "Перехвачено исключение: {}",
          e.toString());
    }

    //получаем список ссылок со страницы
    //, фильтруем их по отсутствию в массиве сбора ссылок
    //, и заполняем список рекурсивных подзадач
    List<LinksWriter> tasks = getLinks(url).stream()
        .filter(Predicate.not(links::contains))
        .map(l -> new LinksWriter(domain, l, links))
        .collect(Collectors.toList());

    //запускаем список подзадач и ждём окончания их работы
    ForkJoinTask.invokeAll(tasks);
  }

  //Получение списка ссылок со страницы по ссылке
  private List<String> getLinks(String url) {

    Document doc = null;
    try {
      //получаем страницу по ссылке
      doc = Jsoup.connect(url).get();
    } catch (IOException e) {
      LOGGER.info(EXCEPTION_MARKER, "Перехвачено исключение: {}",
          e.toString());
    }

    //проверка страницы на null
    if (doc == null) {
      return Collections.emptyList();
    }

    //фильтруем элементы по домену и отсутствию символа '#'
    //, и возвращаем список полных ссылок из этих элементов
    return doc.select("a[href]").stream()
        .filter(e -> e.absUrl("href").matches(domain + ".+"))
        .filter(e -> !e.attr("href").matches(".*#.*"))
        .map(e -> e.absUrl("href"))
        .collect(Collectors.toList());
  }
}
