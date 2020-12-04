import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class Main {

  private static final String URL = "https://www.moscowmap.ru/metro.html#lines";
  private static final String RESULT_FILE
      = "09_FilesAndNetwork/JsonParse/src/main/resources/map.json";

  //список номеров линий в правильном порядке
  private static final ArrayList<String> sortedNumOfLines = new ArrayList<>();

  //список линий(экземпляров класса Line)
  private static final ArrayList<Line> lines = new ArrayList<>();

  //список станции по номерам линий(объект типа Elements)
  private static Elements stationsByLinesSelect;

  public static void main(String[] args) {

    //парсим страницу Московского метро
    getDataFromSite();

    //формируем JSON-данные для записи в файл
    JsonObject jsonOutput = getJsonData();

    //СОЗДАЁМ И ЗАПИСЫВАЕМ JSON-ФАЙЛ===========================
    try (FileWriter fileWriter
        = new FileWriter(RESULT_FILE)) {
      Gson gson = new GsonBuilder().setPrettyPrinting().create();

      fileWriter.write(gson.toJson(jsonOutput));
    } catch (IOException e) {
      e.printStackTrace();
    }
    //КОНЕЦ СОЗДАНИЯ JSON-ФАЙЛА================================

    try (FileReader jsonReader
        = new FileReader(RESULT_FILE)) {

      //парсинг JSON-файла
      JsonObject parsedJson = JsonParser.parseReader(jsonReader).getAsJsonObject();

      //получаем JSON-массив линий
      JsonArray lines = parsedJson.get("lines").getAsJsonArray();

      //получаем JSON-объект станций
      JsonObject stations = parsedJson.get("stations").getAsJsonObject();

      //вывод результатов в консоль
      System.out.print(getResult(lines, stations));
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  //Парсинг страницы сайта
  private static void getDataFromSite() {
    try {
      //получаем сайт в переменную Document doc
      Document doc = Jsoup.connect(URL).get();

      //получаем строчки с описанием линий метро
      Elements linesSelect = doc.select("#metrodata > div > div > span");

      //заполняем массивы sortedNumOfLines и lines
      linesSelect.forEach(Main::fillLinesAndNumbers);

      //получаем строчки с описанием станций
      stationsByLinesSelect = doc.select("#metrodata > div > div.js-depend > div");
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  //Заполнение списков sortedNumOfLines и lines
  private static void fillLinesAndNumbers(Element element) {
    sortedNumOfLines.add(element.attr("data-line"));
    lines.add(new Line(element.attr("data-line")
        , element.text().replace(" линия", "")));
  }

  //Формирование JSON-данных
  private static JsonObject getJsonData() {
    //создаём JSON-объект со станциями
    JsonObject jsonStations = new JsonObject();

    //заполняем JSON-объект со станциями в порядке нумерации линий
    for (String num : sortedNumOfLines) {
      JsonArray stByLine = new JsonArray();

      getStationsByLineNum(num).forEach(stByLine::add);
      jsonStations.add(num, stByLine);
    }

    //создаём JSON-массив с линиями
    JsonArray jsonLines = new JsonArray();

    //заполняем JSON-массив с линиями объектами JsonObject,
    //созданными на основе элементов массива lines
    for (Line l : lines) {
      JsonObject line = new JsonObject();
      line.addProperty("number", l.number);
      line.addProperty("name", l.name);
      jsonLines.add(line);
    }

    //создаём основной JSON-объект, для записи в файл
    JsonObject jsonOutput = new JsonObject();

    //записываем в него jsonStations и jsonLines
    jsonOutput.add("stations", jsonStations);
    jsonOutput.add("lines", jsonLines);

    return jsonOutput;
  }

  //Получение списка станций по номеру линии
  private static List<String> getStationsByLineNum(String num) {
    List<String> stations = new ArrayList<>();

    Element sl = stationsByLinesSelect.stream()
        .filter(element -> element.attr("data-line").equals(num)).findAny().get();

    sl.select("p > a > span.name")
        .forEach(element -> stations.add(element.text()));

    return stations;
  }

  //Получение строки с результатом, для вывода в консоль
  private static String getResult(JsonArray lines, JsonObject stations) {
    StringBuilder builder = new StringBuilder();

    for (JsonElement line : lines) {
      String index = clearJsonValue(line.getAsJsonObject().get("number"));
      JsonArray stationsOfLine = stations.get(index).getAsJsonArray();

      String lineName = clearJsonValue(line.getAsJsonObject().get("name"));

      builder.append(lineName)
          .append(getEndOfLineName(lineName))
          .append(" - ")
          .append(stationsOfLine.size())
          .append(" ")
          .append(declensionOfOutputWord(stationsOfLine.size()))
          .append(".\n");
    }
    return builder.toString();
  }

  //Очистка строки JSON-значения от кавычек
  private static String clearJsonValue(JsonElement value) {
    return value.toString().replace("\"", "");
  }

  //Добавление слова "линия" к названию линии,
  //если оно того требует
  private static String getEndOfLineName(String name) {
    if (name.matches("[А-Яа-яё\\-]*")) {
      return " линия";
    }
    return "";
  }

  //Склонение слова "станция"(для печати результатов),
  //в зависимости от количества
  private static String declensionOfOutputWord(int count) {
    count = count % 100;

    if (count > 10 && count < 15) {
      return "станций";
    }

    count = count % 10;

    if (count == 1) {
      return "станция";
    } else if (count > 1 && count < 5) {
      return "станции";
    }

    return "станций";
  }

  //Инициализация класса Line,
  //для хранения информации о линии(номер + имя)
  private static class Line {

    private final String number;
    private final String name;

    private Line(String number, String name) {
      this.number = number;
      this.name = name;
    }
  }
}
