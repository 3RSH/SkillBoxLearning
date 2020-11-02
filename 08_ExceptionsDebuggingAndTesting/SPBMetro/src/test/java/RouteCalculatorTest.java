import core.Line;
import core.Station;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import junit.framework.TestCase;

public class RouteCalculatorTest extends TestCase {

  StationIndex sIndex;
  List<Station> route;
  RouteCalculator routeCalculator;
  Station from;
  Station to;

  @Override
  protected void setUp() {
    sIndex = new StationIndex();
    route = new ArrayList<>();

    //==========ТЕСТОВАЯ СХЕМА:=============================
    //
    //            -line1-                -line3-
    //             (1-1)                  (3-1)
    // -line2-       |                      |
    //  (2-1) -- (1-2)(2-2) -- (2-3) -- (2-4)(3-2)
    //               |                      |
    //             (1-3)                  (3-3)
    //               |        -line4-       |
    //           (1-4)(4-1) ----------- (3-4)(4-2) -- (4-3)
    //
    //======================================================

    Line line1 = new Line(1, "Первая");
    Line line2 = new Line(2, "Вторая");
    Line line3 = new Line(3, "Третья");
    Line line4 = new Line(4, "Чертвёртая");

    Station station1L1 = new Station("1-1", line1);
    Station station2L1 = new Station("1-2", line1);
    Station station3L1 = new Station("1-3", line1);
    Station station4L1 = new Station("1-4", line1);

    line1.addStation(station1L1);
    line1.addStation(station2L1);
    line1.addStation(station3L1);
    line1.addStation(station4L1);

    Station station1L2 = new Station("2-1", line2);
    Station station2L2 = new Station("2-2", line2);
    Station station3L2 = new Station("2-3", line2);
    Station station4L2 = new Station("2-4", line2);

    line2.addStation(station1L2);
    line2.addStation(station2L2);
    line2.addStation(station3L2);
    line2.addStation(station4L2);

    Station station1L3 = new Station("3-1", line3);
    Station station2L3 = new Station("3-2", line3);
    Station station3L3 = new Station("3-3", line3);
    Station station4L3 = new Station("3-4", line3);

    line3.addStation(station1L3);
    line3.addStation(station2L3);
    line3.addStation(station3L3);
    line3.addStation(station4L3);

    Station station1L4 = new Station("4-1", line4);
    Station station2L4 = new Station("4-2", line4);
    Station station3L4 = new Station("4-3", line4);

    line4.addStation(station1L4);
    line4.addStation(station2L4);
    line4.addStation(station3L4);

    List<Station> connectionL1L2 = Arrays.asList(station2L1, station2L2);
    List<Station> connectionL1L4 = Arrays.asList(station4L1, station1L4);
    List<Station> connectionL2L3 = Arrays.asList(station4L2, station2L3);
    List<Station> connectionL3L4 = Arrays.asList(station4L3, station2L4);

    List<Station> stations = Arrays.asList(station1L1, station2L1
        , station3L1, station4L1, station1L2, station2L2
        , station3L2, station4L2, station1L3, station2L3
        , station3L3, station4L3, station1L4, station2L4
        , station3L4);
    List<Line> lines = Arrays.asList(line1, line2, line3, line4);

    stations.forEach(sIndex::addStation);
    lines.forEach(sIndex::addLine);
    sIndex.addConnection(connectionL1L2);
    sIndex.addConnection(connectionL1L4);
    sIndex.addConnection(connectionL2L3);
    sIndex.addConnection(connectionL3L4);

    //Экземпляр RouteCalculator() для тестов
    routeCalculator = new RouteCalculator(sIndex);
  }

  //testGetShortestRoute (кейс_1): одна станция отправления и назначения
  public void testGetShortestRoute_route_to_same_station() {
    from = sIndex.getStation("2-1");
    to = sIndex.getStation("2-1");

    List<Station> actual = routeCalculator.getShortestRoute(from, to);

    List<Station> expected = new ArrayList<>();
    expected.add(sIndex.getStation("2-1"));

    assertEquals(expected, actual);
  }

  //testGetShortestRoute (кейс_2): соседние станции
  public void testGetShortestRoute_stations_next_to_each_other_on_single_line() {
    from = sIndex.getStation("1-2");
    to = sIndex.getStation("1-3");

    List<Station> actual = routeCalculator.getShortestRoute(from, to);

    List<Station> expected = new ArrayList<>();
    expected.add(sIndex.getStation("1-2"));
    expected.add(sIndex.getStation("1-3"));

    assertEquals(expected, actual);
  }

  //testGetShortestRoute (кейс_3): соседние станции в обратном направлении
  public void testGetShortestRoute_stations_next_to_each_other_on_single_line_reverse() {
    from = sIndex.getStation("3-3");
    to = sIndex.getStation("3-2");

    List<Station> actual = routeCalculator.getShortestRoute(from, to);

    List<Station> expected = new ArrayList<>();
    expected.add(sIndex.getStation("3-3"));
    expected.add(sIndex.getStation("3-2"));

    assertEquals(expected, actual);
  }

  //testGetShortestRoute (кейс_4): станции на одной линии
  public void testGetShortestRoute_opposite_stations_on_single_line() {
    from = sIndex.getStation("1-1");
    to = sIndex.getStation("1-3");

    List<Station> actual = routeCalculator.getShortestRoute(from, to);

    List<Station> expected = new ArrayList<>();
    expected.add(sIndex.getStation("1-1"));
    expected.add(sIndex.getStation("1-2"));
    expected.add(sIndex.getStation("1-3"));

    assertEquals(expected, actual);
  }

  //testGetShortestRoute (кейс_5): станции на одной линии в обратном направлении
  public void testGetShortestRoute_opposite_stations_on_single_line_reverse() {
    from = sIndex.getStation("1-4");
    to = sIndex.getStation("1-1");

    List<Station> actual = routeCalculator.getShortestRoute(from, to);

    List<Station> expected = new ArrayList<>();
    expected.add(sIndex.getStation("1-4"));
    expected.add(sIndex.getStation("1-3"));
    expected.add(sIndex.getStation("1-2"));
    expected.add(sIndex.getStation("1-1"));

    assertEquals(expected, actual);
  }

  //testGetShortestRoute (кейс_6): соседние станции (1 переход)
  public void testGetShortestRoute_stations_next_to_each_other_with_one_transfer() {
    from = sIndex.getStation("1-2");
    to = sIndex.getStation("2-2");

    List<Station> actual = routeCalculator.getShortestRoute(from, to);

    List<Station> expected = new ArrayList<>();
    expected.add(sIndex.getStation("1-2"));
    expected.add(sIndex.getStation("2-2"));

    assertEquals(expected, actual);
  }

  //testGetShortestRoute (кейс_7): путь с одним переходом
  public void testGetShortestRoute_opposite_stations_with_one_transfer() {
    from = sIndex.getStation("4-1");
    to = sIndex.getStation("3-3");

    List<Station> actual = routeCalculator.getShortestRoute(from, to);

    List<Station> expected = new ArrayList<>();
    expected.add(sIndex.getStation("4-1"));
    expected.add(sIndex.getStation("4-2"));
    expected.add(sIndex.getStation("3-4"));
    expected.add(sIndex.getStation("3-3"));

    assertEquals(expected, actual);
  }

  //testGetShortestRoute (кейс_8): путь с двумя переходами
  public void testGetShortestRoute_opposite_stations_with_two_transfer() {
    from = sIndex.getStation("2-1");
    to = sIndex.getStation("4-3");

    List<Station> actual = routeCalculator.getShortestRoute(from, to);

    List<Station> expected = new ArrayList<>();
    expected.add(sIndex.getStation("2-1"));
    expected.add(sIndex.getStation("2-2"));
    expected.add(sIndex.getStation("1-2"));
    expected.add(sIndex.getStation("1-3"));
    expected.add(sIndex.getStation("1-4"));
    expected.add(sIndex.getStation("4-1"));
    expected.add(sIndex.getStation("4-2"));
    expected.add(sIndex.getStation("4-3"));

    assertEquals(expected, actual);
  }

  //testCalculateDuration (кейс_1): одна станция отправления и назначения
  public void testCalculateDuration_route_to_same_station() {
    from = sIndex.getStation("3-3");
    to = sIndex.getStation("3-3");
    route = routeCalculator.getShortestRoute(from, to);

    double actual = RouteCalculator.calculateDuration(route);

    //route: (3-3)
    double expected = 0.0;

    assertEquals(expected, actual);
  }

  //testCalculateDuration (кейс_2): соседние станции
  public void testCalculateDuration_stations_next_to_each_other_on_single_line() {
    from = sIndex.getStation("2-3");
    to = sIndex.getStation("2-4");
    route = routeCalculator.getShortestRoute(from, to);

    double actual = RouteCalculator.calculateDuration(route);

    //route: (2-3) -> (2-4)
    double expected = 2.5;

    assertEquals(expected, actual);
  }

  //testCalculateDuration (кейс_3): соседние станции в обратном направлении
  public void testCalculateDuration_stations_next_to_each_other_on_single_line_reverse() {
    from = sIndex.getStation("4-2");
    to = sIndex.getStation("4-1");
    route = routeCalculator.getShortestRoute(from, to);

    double actual = RouteCalculator.calculateDuration(route);

    //route: (4-2) -> (4-1)
    double expected = 2.5;

    assertEquals(expected, actual);
  }

  //testCalculateDuration (кейс_4): станции на одной линии
  public void testCalculateDuration_opposite_stations_on_single_line() {
    from = sIndex.getStation("1-1");
    to = sIndex.getStation("1-3");
    route = routeCalculator.getShortestRoute(from, to);

    double actual = RouteCalculator.calculateDuration(route);

    //route: (1-1) -> (1-2) -> (1-3)
    double expected = 2.5 + 2.5;

    assertEquals(expected, actual);
  }

  //testCalculateDuration (кейс_5): станции на одной линии в обратном направлении
  public void testCalculateDuration_opposite_stations_on_single_line_reverse() {
    from = sIndex.getStation("3-4");
    to = sIndex.getStation("3-1");
    route = routeCalculator.getShortestRoute(from, to);

    double actual = RouteCalculator.calculateDuration(route);

    //route: (3-4) -> (3-3) -> (3-2) -> (3-1)
    double expected = 2.5 + 2.5 + 2.5;

    assertEquals(expected, actual);
  }

  //testCalculateDuration (кейс_6): соседние станции (1 переход)
  public void testCalculateDuration_stations_next_to_each_other_with_one_transfer() {
    from = sIndex.getStation("2-4");
    to = sIndex.getStation("3-2");
    route = routeCalculator.getShortestRoute(from, to);

    double actual = RouteCalculator.calculateDuration(route);

    //route: (2-4)(3-2)
    double expected = 3.5;

    assertEquals(expected, actual);
  }

  //testCalculateDuration (кейс_7): путь с одним переходом
  public void testCalculateDuration_opposite_stations_with_one_transfer() {
    from = sIndex.getStation("2-2");
    to = sIndex.getStation("3-1");
    route = routeCalculator.getShortestRoute(from, to);

    double actual = RouteCalculator.calculateDuration(route);

    //route: (2-2) -> (2-3) -> (2-4)(3-2) -> (3-1)
    double expected = 2.5 + 2.5 + 3.5 + 2.5;

    assertEquals(expected, actual);
  }

  //testCalculateDuration (кейс_8): путь с двумя переходами
  public void testCalculateDuration_opposite_stations_with_two_transfer() {
    from = sIndex.getStation("1-3");
    to = sIndex.getStation("3-1");
    route = routeCalculator.getShortestRoute(from, to);

    double actual = RouteCalculator.calculateDuration(route);

    //route: (1-3) -> (1-2)(2-2) -> (2-3) -> (2-4)(3-2) -> (3-1)
    double expected = 2.5 + 3.5 + 2.5 + 2.5 + 3.5 + 2.5;

    assertEquals(expected, actual);
  }

  @Override
  protected void tearDown() {
    sIndex = null;
    route = null;
    routeCalculator = null;
    from = null;
    to = null;
  }
}
