import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.concurrent.ForkJoinTask;
import java.util.concurrent.RecursiveAction;
import java.util.stream.Collectors;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

public class LinksWriter extends RecursiveAction {

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
    System.out.println(Thread.currentThread().getName()
        + ": всего ссылок найдено " + links.size());

    //прерываем поток для избежания блокировки сайтом
    try {
      Thread.sleep(200);
    } catch (InterruptedException e) {
      e.printStackTrace();
    }

    //список для сбора рекурсивных подзадач
    List<LinksWriter> tasks = new ArrayList<>();

    //получаем список ссылок со страницы
    //, фильтруем их по отсутствию в массиве сбора ссылок
    //, и заполняем список рекурсивных подзадач
    getLinks(url).stream().filter(l -> !links.contains(l))
        .forEach(l -> tasks.add(new LinksWriter(domain, l, links)));

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
      e.printStackTrace();
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
