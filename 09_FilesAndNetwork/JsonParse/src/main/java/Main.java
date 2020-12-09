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
  private static final List<String> sortedNumOfLines = new ArrayList<>();

  //список линий(экземпляров класса Line)
  private static final List<Line> lines = new ArrayList<>();

  //список переходов типа Станция A <-> Станция B
  private static final List<Connection> stToStConnections = new ArrayList<>();

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

      //получаем JSON-массив переходов
      JsonArray connections = parsedJson.get("connections").getAsJsonArray();

      //получаем JSON-объект станций
      JsonObject stations = parsedJson.get("stations").getAsJsonObject();

      //вывод результатов в консоль
      System.out.println(getResult(lines, connections, stations));
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

      //заполняем списки sortedNumOfLines и lines
      linesSelect.forEach(Main::fillLinesAndNumbers);

      //получаем строчки с описанием станций
      stationsByLinesSelect = doc.select("#metrodata > div > div.js-depend > div");

      //заполняем список stToStConnections
      sortedNumOfLines.forEach(Main::fillStToStConnections);

      //чистим список stToStConnections от повторов
      clearStToStConnections();

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

  //Заполнение списка stToStConnections по номеру линии
  private static void fillStToStConnections(String lineNum) {

    Element stationsList = stationsByLinesSelect.stream()
        .filter(element -> element.attr("data-line").equals(lineNum)).findAny().get();

    Elements stations = stationsList.select("div > p:has(span[class^=t-icon-metroln])");

    for (Element station : stations) {
      Elements connectElement = station.select("p > a > span[class^=t-icon-metroln]");

      connectElement.forEach(connection -> {
        int startIndexSt = connection.attr("title").indexOf("«") + 1;
        int endIndexSt = connection.attr("title").indexOf("»");

        stToStConnections.add(new Connection(lineNum, station.select("a > span.name").text()
            , connection.className().replaceAll("t-icon-metroln ln-", "")
            , connection.attr("title")
            .substring(startIndexSt, endIndexSt)));
      });
    }
  }

  //Чистка списка stToStConnections от повторов
  private static void clearStToStConnections() {
    List<Connection> emptyConnects = new ArrayList<>();

    for (Connection connectFrom : stToStConnections) {
      for (Connection connectTo : stToStConnections) {
        if (connectFrom.toLine.equals(connectTo.line)
            && connectFrom.toStation.equals(connectTo.station)) {
          connectTo.toLine = "";
          connectTo.toStation = "";
          emptyConnects.add(connectTo);
        }
      }
    }

    stToStConnections.removeAll(emptyConnects);
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

    //создаём JSON-массив с переходами
    JsonArray jsonConnections = new JsonArray();

    //заполняем JSON-массив с переходами
    for (int i = 0; i < stToStConnections.size(); i++) {
      JsonArray connection = new JsonArray();

      Connection connect = stToStConnections.get(i);

      JsonObject aToB = new JsonObject();
      aToB.addProperty("line", connect.line);
      aToB.addProperty("station", connect.station);
      connection.add(aToB);

      JsonObject bToA = new JsonObject();
      bToA.addProperty("line", connect.toLine);
      bToA.addProperty("station", connect.toStation);
      connection.add(bToA);

      //дополняем переход (в блоке while) другими переходами с этой-же станцией
      while (i < stToStConnections.size() - 1) {

        Connection nextConnect = stToStConnections.get(i + 1);

        if (connect.line.equals(nextConnect.line)
            && connect.station.equals(nextConnect.station)) {
          bToA = new JsonObject();
          bToA.addProperty("line", nextConnect.toLine);
          bToA.addProperty("station", nextConnect.toStation);
          connection.add(bToA);
          i++;
        } else {
          break;
        }
      }

      jsonConnections.add(connection);
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
    jsonOutput.add("connections", jsonConnections);
    jsonOutput.add("lines", jsonLines);

    return jsonOutput;
  }

  //Получение списка станций по номеру линии
  private static List<String> getStationsByLineNum(String lineNum) {
    List<String> stations = new ArrayList<>();

    Element stationsOfLine = stationsByLinesSelect.stream()
        .filter(element -> element.attr("data-line").equals(lineNum)).findAny().get();

    stationsOfLine.select("p > a > span.name")
        .forEach(element -> stations.add(element.text()));

    return stations;
  }

  //Получение строки с результатом, для вывода в консоль
  private static String getResult(JsonArray lines, JsonArray connections, JsonObject stations) {
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

    builder.append("Количество переходов: ")
        .append(connections.size())
        .append(".");

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
  //для хранения информации о линии(номер + название)
  private static class Line {

    private final String number;
    private final String name;

    private Line(String number, String name) {
      this.number = number;
      this.name = name;
    }
  }

  //Инициализация класса Connection,
  //для хранения информации о переходе:
  //  (номер линии, имя станции, номер линии перехода, имя станции перехода,)
  private static class Connection {

    private final String line;
    private final String station;
    private String toLine;
    private String toStation;

    public Connection(String line, String station, String toLine, String toStation) {
      this.line = line;
      this.station = station;
      this.toLine = toLine;
      this.toStation = toStation;
    }
  }
}
