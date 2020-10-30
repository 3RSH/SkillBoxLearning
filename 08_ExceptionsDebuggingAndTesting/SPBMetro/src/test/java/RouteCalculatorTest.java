import static org.junit.Assert.assertNotEquals;

import core.Line;
import core.Station;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import junit.framework.TestCase;

public class RouteCalculatorTest extends TestCase {

  StationIndex sIndex;
  List<Station> route;
  RouteCalculator routeCalculator;
  Station from;
  Station to;

  @Override
  protected void setUp() throws Exception {
    sIndex = new StationIndex();
    route = new ArrayList<>();

    //==========ТЕСТОВАЯ СХЕМА:===========
    //
    // -line1-                     -line3-
    //  (1-1)                       (1-3)
    //    |          -line2-          |
    //  (2-1)(1-2) -- (2-2) -- (3-2)(2-3)
    //    |                           |
    //  (3-1)                       (3-3)
    //
    //====================================

    Line line1 = new Line(1, "Первая");
    Line line2 = new Line(2, "Вторая");
    Line line3 = new Line(3, "Третья");

    Station station1L1 = new Station("1-1", line1);
    Station station2L1 = new Station("2-1", line1);
    Station station3L1 = new Station("3-1", line1);

    line1.addStation(station1L1);
    line1.addStation(station2L1);
    line1.addStation(station3L1);

    Station station1L2 = new Station("1-2", line2);
    Station station2L2 = new Station("2-2", line2);
    Station station3L2 = new Station("3-2", line2);

    line2.addStation(station1L2);
    line2.addStation(station2L2);
    line2.addStation(station3L2);

    Station station1L3 = new Station("1-3", line3);
    Station station2L3 = new Station("2-3", line3);
    Station station3L3 = new Station("3-3", line3);

    line3.addStation(station1L3);
    line3.addStation(station2L3);
    line3.addStation(station3L3);

    List<Station> connectionL1L2 = Arrays.asList(station2L1, station1L2);
    List<Station> connectionL2L3 = Arrays.asList(station3L2, station2L3);

    List<Station> stations = Arrays.asList(station1L1, station1L2, station1L3
        , station2L1, station2L2, station2L3
        , station3L1, station3L2, station3L3);
    List<Line> lines = Arrays.asList(line1, line2, line3);

    stations.forEach(sIndex::addStation);
    lines.forEach(sIndex::addLine);
    sIndex.addConnection(connectionL1L2);
    sIndex.addConnection(connectionL2L3);

    //Экземпляр RouteCalculator() для тестов
    routeCalculator = new RouteCalculator(sIndex);
  }

  public void testGetShortestRoute() {
    from = sIndex.getStation("1-1");
    to = sIndex.getStation("2-2");

    List<Station> actual = routeCalculator.getShortestRoute(from, to);

    List<Station> expected = new ArrayList<>();
    expected.add(sIndex.getStation("1-1"));
    expected.add(sIndex.getStation("2-1"));
    expected.add(sIndex.getStation("3-1"));

    assertNotEquals(expected, actual);

    from = sIndex.getStation("1-1");
    to = sIndex.getStation("3-1");
    actual = routeCalculator.getShortestRoute(from, to);

    assertEquals(expected, actual);

    from = sIndex.getStation("3-1");
    to = sIndex.getStation("1-1");
    actual = routeCalculator.getShortestRoute(from, to);
    Collections.reverse(expected);

    assertEquals(expected, actual);

    from = sIndex.getStation("1-1");
    to = sIndex.getStation("2-2");
    actual = routeCalculator.getShortestRoute(from, to);

    Collections.reverse(expected);

    assertNotEquals(expected, actual);

    from = sIndex.getStation("1-1");
    to = sIndex.getStation("3-3");
    actual = routeCalculator.getShortestRoute(from, to);

    expected.clear();
    expected.add(sIndex.getStation("1-1"));
    expected.add(sIndex.getStation("2-1"));
    expected.add(sIndex.getStation("1-2"));
    expected.add(sIndex.getStation("2-2"));
    expected.add(sIndex.getStation("3-2"));
    expected.add(sIndex.getStation("2-3"));
    expected.add(sIndex.getStation("3-3"));

    assertEquals(expected, actual);

  }

  public void testCalculateDuration() {

    from = sIndex.getStation("1-1");
    to = sIndex.getStation("3-3");
    route = routeCalculator.getShortestRoute(from, to);

    double actual = RouteCalculator.calculateDuration(route);
    double expected = 17.0;

    assertEquals(expected, actual);
  }

  @Override
  protected void tearDown() throws Exception {
    sIndex = null;
    route = null;
    routeCalculator = null;
    from = null;
    to = null;
  }
}
