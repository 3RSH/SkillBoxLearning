import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import metro.Connection;
import metro.Line;

public class JsonResponse {

  private final Map<String, List<String>> stations = new HashMap<>();
  private final List<List<Connection>> connections = new ArrayList<>();
  private List<Line> lines = new ArrayList<>();

  public void setStations(List<Line> lines) {
    lines.forEach(line -> stations.put(line.getNumber(), line.getStations()));
  }

  public void setConnections(List<Connection> connections) {
    for (int i = 0; i < connections.size(); i++) {
      List<Connection> fullConnection = new ArrayList<>();

      Connection connect = connections.get(i);

      fullConnection.add(connect);

      Connection mirrorConnect = new Connection(connect.getToLine()
          , connect.getToStation(), null, null);

      fullConnection.add(mirrorConnect);

      //дополняем переход (в блоке while) другими переходами с этой-же станцией
      while (i < connections.size() - 1) {

        Connection nextConnect = connections.get(i + 1);

        if (connect.getLine().equals(nextConnect.getLine())
            && connect.getStation().equals(nextConnect.getStation())) {
          mirrorConnect = new Connection(nextConnect.getToLine()
              , nextConnect.getToStation(), null, null);

          fullConnection.add(mirrorConnect);
          i++;
        } else {
          break;
        }
      }

      this.connections.add(fullConnection);
    }
  }

  public void setLines(List<Line> lines) {
    this.lines = lines;
  }
}
