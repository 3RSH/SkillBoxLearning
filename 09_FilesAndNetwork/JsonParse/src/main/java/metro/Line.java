package metro;

import java.util.List;

//Класс Line,
//для хранения информации о линии(номер + название)
public class Line {

  private final String number;
  private final String name;

  transient private final List<String> stations;

  public Line(String number, String name, List<String> stations) {
    this.number = number;
    this.name = name;
    this.stations = stations;
  }

  public List<String> getStations() {
    return stations;
  }

  public String getNumber() {
    return number;
  }
}
