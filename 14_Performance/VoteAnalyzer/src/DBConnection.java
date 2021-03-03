import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DBConnection {

  private static final String DB_NAME = "learn";
  private static final String DB_USER = "root";
  private static final String DB_PASS = "testtest";

  private static final String VOTERS_TAB = "voters";
  private static final String DUPLICATE_VOTERS_TAB = "duplicate_voters";
  private static final String STATIONS_TAB = "stations_visits";
  private static final String STATIONS_WORK_TIMES_TAB = "stations_work_times";

  private static final StringBuilder insertVotersQuery = new StringBuilder();
  private static final StringBuilder insertStationsQuery = new StringBuilder();

  private static Connection connection;

  public static void getConnection() throws SQLException {
    if (connection == null) {
      connection = initDb();
    }
  }

  private static Connection initDb() throws SQLException {
    Connection connection = DriverManager.getConnection(
        "jdbc:mysql://localhost:3306/?serverTimezone=UTC", DB_USER, DB_PASS);

    connection.createStatement().execute("DROP DATABASE IF EXISTS " + DB_NAME);
    connection.createStatement().execute("CREATE DATABASE " + DB_NAME);
    connection.createStatement().execute("USE " + DB_NAME);

    connection.createStatement().execute("SET GLOBAL max_allowed_packet = 5000000");
    connection.createStatement().execute("SET GLOBAL innodb_buffer_pool_size = 16000000");

    connection.createStatement().execute("DROP TABLE IF EXISTS " + VOTERS_TAB);
    String query = "CREATE TABLE "
        + VOTERS_TAB
        + "(id INT NOT NULL AUTO_INCREMENT, "
        + "name TINYTEXT NOT NULL, "
        + "birthDate DATE NOT NULL, "
        + "PRIMARY KEY(id))";
    connection.createStatement().execute(query);

    connection.createStatement().execute("DROP TABLE IF EXISTS " + STATIONS_TAB);
    query = "CREATE TABLE "
        + STATIONS_TAB
        + "(id INT NOT NULL AUTO_INCREMENT, "
        + "`index` SMALLINT NOT NULL, "
        + "date DATE NOT NULL, "
        + "time TIME NOT NULL, "
        + "PRIMARY KEY(id))";
    connection.createStatement().execute(query);

    return connection;
  }


  public static void countVoter(String name, String birthDay) throws SQLException {
    birthDay = birthDay.replace('.', '-');

    insertVotersQuery.append(insertVotersQuery.length() == 0 ? "" : ",")
        .append("('").append(name).append("', '").append(birthDay).append("')");
  }

  public static void countStationVisit(short index, String time) throws SQLException {
    String[] dayTime = time.split(" ");
    dayTime[0] = time.replace('.', '-');

    insertStationsQuery.append(insertStationsQuery.length() == 0 ? "" : ",")
        .append("('").append(index).append("', '").append(dayTime[0]).append("', '")
        .append(dayTime[1]).append("')");
  }

  public static void executeMultiInsert() throws SQLException {
    if (insertVotersQuery.isEmpty()) {
      return;
    }

    String sqlQueryInsertVoters = "INSERT INTO "
        + VOTERS_TAB
        + "(name, birthDate) VALUES"
        + insertVotersQuery;
    connection.createStatement().execute(sqlQueryInsertVoters);
    insertVotersQuery.setLength(0);

    String sqlQueryInsertStations = "INSERT INTO "
        + STATIONS_TAB
        + "(`index`, date, time) VALUES"
        + insertStationsQuery;
    connection.createStatement().execute(sqlQueryInsertStations);
    insertStationsQuery.setLength(0);
  }

  public static void createDuplicateVotersTab() throws SQLException {
    String query = "CREATE TABLE "
        + DUPLICATE_VOTERS_TAB
        + " SELECT name, birthDate, COUNT(*) AS `count` FROM "
        + VOTERS_TAB
        + " GROUP BY name, birthDate "
        + "HAVING `count` > 1";

    connection.createStatement().execute(query);
  }

  public static void createStationsWorkTimesTab() throws SQLException {
    String query = "CREATE TABLE "
        + STATIONS_WORK_TIMES_TAB
        + " SELECT `index`, date, min(time) AS start, max(time) AS end FROM "
        + STATIONS_TAB
        + " GROUP BY `index`, date ORDER BY `index`";

    connection.createStatement().execute(query);
  }

  public static void printVoterCounts() throws SQLException {
    StringBuilder output = new StringBuilder();

    String query = "SELECT * FROM " + DUPLICATE_VOTERS_TAB;
    ResultSet rs = connection.createStatement().executeQuery(query);

    while (rs.next()) {
      output.append("\t").append(rs.getString("name")).append(" (")
          .append(rs.getString("birthDate").replaceAll("-", "."))
          .append(") - ").append(rs.getShort("count")).append("\n");
    }

    rs.close();
    System.out.print(output);
  }

  public static void printStationWorkTimeCounts() throws SQLException {
    StringBuilder output = new StringBuilder();
    short stationIndex = 0;

    String query = "SELECT * FROM " + STATIONS_WORK_TIMES_TAB;
    ResultSet rs = connection.createStatement().executeQuery(query);

    while (rs.next()) {
      if (rs.getShort("index") != stationIndex) {
        output.append(((stationIndex != 0) ? "\n" : "")).append("\t")
            .append(rs.getShort("index")).append(" - ");
        stationIndex = rs.getShort("index");
      } else {
        output.append(" ");
      }

      output.append(rs.getString("date").replaceAll("-", "."))
          .append(" ").append(rs.getString("start"), 0, 5)
          .append("-").append(rs.getString("end"), 0, 5);
    }

    rs.close();
    System.out.print(output);
  }
}