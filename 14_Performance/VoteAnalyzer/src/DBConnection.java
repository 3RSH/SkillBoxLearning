import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DBConnection {

  private static final String dbName = "learn";
  private static final String dbUser = "root";
  private static final String dbPass = "testtest";
  private static final String tabVoter = "voter_count";
  private static final String tabStations = "vote_station_work_times";
  private static final StringBuilder insertVotersQuery = new StringBuilder();
  private static final StringBuilder insertStationsQuery = new StringBuilder();

  private static Connection connection;

  public static Connection getConnection() throws SQLException {
    if (connection == null) {
      connection = initDb();
    }
    return connection;
  }

  private static Connection initDb() throws SQLException {
    Connection connection = DriverManager.getConnection(
        "jdbc:mysql://localhost:3306/?serverTimezone=UTC", dbUser, dbPass);

    connection.createStatement().execute("DROP DATABASE IF EXISTS " + dbName);
    connection.createStatement().execute("CREATE DATABASE " + dbName);
    connection.createStatement().execute("USE " + dbName);

    StringBuilder sqlQuery = new StringBuilder();

    connection.createStatement().execute("DROP TABLE IF EXISTS " + tabVoter);
    sqlQuery.append("CREATE TABLE ").append(tabVoter)
        .append("(id INT NOT NULL AUTO_INCREMENT, ")
        .append("name TINYTEXT NOT NULL, ")
        .append("birthDate DATE NOT NULL, ")
        .append("`count` TINYINT NOT NULL, ")
        .append("PRIMARY KEY(id), KEY(birthDate, name(50)), ")
        .append("UNIQUE KEY name_date(name(50), birthDate))");
    connection.createStatement().execute(sqlQuery.toString());

    sqlQuery.setLength(0);

    connection.createStatement().execute("DROP TABLE IF EXISTS " + tabStations);
    sqlQuery.append("CREATE TABLE ").append(tabStations)
        .append("(`index` SMALLINT NOT NULL, ")
        .append("date DATE NOT NULL, ")
        .append("start TIME NOT NULL, ")
        .append("end TIME NOT NULL, ")
        .append("PRIMARY KEY(`index`, date))");
    connection.createStatement().execute(sqlQuery.toString());

    return connection;
  }

  public static void executeMultiInsert() throws SQLException {
    StringBuilder sqlQuery = new StringBuilder();

    sqlQuery.append("INSERT INTO ").append(tabVoter)
        .append("(name, birthDate,`count`) ")
        .append("VALUES").append(insertVotersQuery)
        .append(" ON DUPLICATE KEY UPDATE `count`=`count` + 1");
    DBConnection.getConnection().createStatement().execute(sqlQuery.toString());
    insertVotersQuery.setLength(0);

    sqlQuery.setLength(0);

    sqlQuery.append("INSERT INTO ").append(tabStations)
        .append("(`index`, date, start, end) ")
        .append("VALUES").append(insertStationsQuery)
        .append(" ON DUPLICATE KEY UPDATE start = IF (start > VALUES(start),")
        .append(" VALUES(start), start), end = IF (end < VALUES(end), VALUES(end), end)");
    DBConnection.getConnection().createStatement().execute(sqlQuery.toString());
    insertStationsQuery.setLength(0);
  }

  public static void countVoter(String name, String birthDay) throws SQLException {
    birthDay = birthDay.replace('.', '-');

    insertVotersQuery.append(insertVotersQuery.length() == 0 ? "" : ",")
        .append("('").append(name).append("', '").append(birthDay).append("', 1)");
  }

  public static void printVoterCounts() throws SQLException {
    String sql = "SELECT name, birthDate, `count` FROM " + tabVoter + " WHERE `count` > 1";

    try (ResultSet rs = DBConnection.getConnection().createStatement().executeQuery(sql)) {
      StringBuilder output = new StringBuilder();

      while (rs.next()) {
        output.append("\t").append(rs.getString("name")).append(" (")
            .append(rs.getString("birthDate").replaceAll("-", "."))
            .append(") - ").append(rs.getShort("count")).append("\n");
      }

      System.out.print(output);
    }
  }

  public static void countWorkTimeStation(short index, String time) throws SQLException {
    String[] dayTime = time.split(" ");

    insertStationsQuery.append(insertStationsQuery.length() == 0 ? "" : ",")
        .append("('").append(index).append("', '").append(dayTime[0]).append("', '")
        .append(dayTime[1]).append("', '").append(dayTime[1]).append("')");

  }

  public static void printStationWorkTimeCounts() throws SQLException {
    String sql = "SELECT `index`, date, start, end FROM " + tabStations + " ORDER BY `index`";

    try (ResultSet rs = DBConnection.getConnection().createStatement().executeQuery(sql)) {
      StringBuilder output = new StringBuilder();
      short stationIndex = 0;

      while (rs.next()) {
        if (rs.getShort("index") == stationIndex) {
          output.append(" ").append(rs.getString("date")).append(" ")
              .append(rs.getString("start"), 0, 5).append("-")
              .append(rs.getString("end"), 0, 5);
          continue;
        }

        output.append(((stationIndex != 0) ? "\n" : "")).append("\t")
            .append(rs.getShort("index")).append(" - ")
            .append(rs.getString("date")).append(" ")
            .append(rs.getString("start"), 0, 5).append("-")
            .append(rs.getString("end"), 0, 5);

        stationIndex = rs.getShort("index");
      }
      System.out.println(output);
    }
  }
}
