package metro;

//Класс Connection,
//для хранения информации о переходе:
//  (номер линии, имя станции, номер линии перехода, имя станции перехода,)
public class Connection {

  private final String line;
  private final String station;


  transient private String toLine;
  transient private String toStation;

  public Connection(String line, String Station, String toLine, String toStation) {
    this.line = line;
    this.station = Station;
    this.toLine = toLine;
    this.toStation = toStation;
  }


  public String getLine() {
    return line;
  }

  public String getStation() {
    return station;
  }

  public String getToLine() {
    return toLine;
  }

  public void setToLine(String toLine) {
    this.toLine = toLine;
  }

  public String getToStation() {
    return toStation;
  }

  public void setToStation(String toStation) {
    this.toStation = toStation;
  }
}
