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

  private static final StringBuilder sqlQuery = new StringBuilder();
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
        "jdbc:mysql://localhost:3306/?serverTimezone=UTC", DB_USER, DB_PASS);

    connection.createStatement().execute("DROP DATABASE IF EXISTS " + DB_NAME);
    connection.createStatement().execute("CREATE DATABASE " + DB_NAME);
    connection.createStatement().execute("USE " + DB_NAME);

    connection.createStatement().execute("DROP TABLE IF EXISTS " + VOTERS_TAB);
    sqlQuery.append("CREATE TABLE ").append(VOTERS_TAB)
        .append("(id INT NOT NULL AUTO_INCREMENT, ")
        .append("name TINYTEXT NOT NULL, ")
        .append("birthDate DATE NOT NULL, ")
        .append("PRIMARY KEY(id))");
    connection.createStatement().execute(sqlQuery.toString());
    sqlQuery.setLength(0);

    connection.createStatement().execute("DROP TABLE IF EXISTS " + DUPLICATE_VOTERS_TAB);
    sqlQuery.append("CREATE TABLE ").append(DUPLICATE_VOTERS_TAB)
        .append("(id INT NOT NULL AUTO_INCREMENT, ")
        .append("name TINYTEXT NOT NULL, ")
        .append("birthDate DATE NOT NULL, ")
        .append("`count` TINYINT NOT NULL, ")
        .append("PRIMARY KEY(id), ")
        .append("UNIQUE KEY name_date(name(50), birthDate))");
    connection.createStatement().execute(sqlQuery.toString());
    sqlQuery.setLength(0);

    connection.createStatement().execute("DROP TABLE IF EXISTS " + STATIONS_TAB);
    sqlQuery.append("CREATE TABLE ").append(STATIONS_TAB)
        .append("(id INT NOT NULL AUTO_INCREMENT, ")
        .append("`index` SMALLINT NOT NULL, ")
        .append("time DATETIME NOT NULL, ")
        .append("PRIMARY KEY(id))");
    connection.createStatement().execute(sqlQuery.toString());
    sqlQuery.setLength(0);

    connection.createStatement().execute("DROP TABLE IF EXISTS " + STATIONS_WORK_TIMES_TAB);
    sqlQuery.append("CREATE TABLE ").append(STATIONS_WORK_TIMES_TAB)
        .append("(`index` SMALLINT NOT NULL, ")
        .append("date DATE NOT NULL, ")
        .append("start TIME NOT NULL, ")
        .append("end TIME NOT NULL, ")
        .append("PRIMARY KEY(`index`, date))");
    connection.createStatement().execute(sqlQuery.toString());
    sqlQuery.setLength(0);

    return connection;
  }

  private static void executeQuery() throws SQLException {
    connection.createStatement().execute(sqlQuery.toString());
    sqlQuery.setLength(0);
  }

  private static ResultSet getResultSet() throws SQLException {
    ResultSet rs = connection.createStatement().executeQuery(sqlQuery.toString());
    sqlQuery.setLength(0);
    return rs;
  }

  public static void countVoter(String name, String birthDay) throws SQLException {
    birthDay = birthDay.replace('.', '-');

    insertVotersQuery.append(insertVotersQuery.length() == 0 ? "" : ",")
        .append("('").append(name).append("', '").append(birthDay).append("')");
  }

  public static void countStationVisit(short index, String time) throws SQLException {
    time = time.replace('.', '-');

    insertStationsQuery.append(insertStationsQuery.length() == 0 ? "" : ",")
        .append("('").append(index).append("', '").append(time).append("')");
  }

  public static void executeMultiInsert() throws SQLException {
    if (insertVotersQuery.length() != 0) {
      sqlQuery.append("INSERT INTO ").append(VOTERS_TAB).append("(name, birthDate) ")
          .append("VALUES").append(insertVotersQuery);
      executeQuery();
      insertVotersQuery.setLength(0);

      sqlQuery.append("INSERT INTO ").append(STATIONS_TAB).append("(`index`, time) ")
          .append("VALUES").append(insertStationsQuery);
      executeQuery();
      insertStationsQuery.setLength(0);
    }
  }

  public static void createIndexVoter() throws SQLException {
    sqlQuery.append("CREATE INDEX date_name ON ").append(VOTERS_TAB)
        .append(" (birthDate, name(50))");
    executeQuery();
  }

  private static void countDuplicateVoter(String name, String birthDay, int count) {
    insertVotersQuery.append(insertVotersQuery.length() == 0 ? "" : ",")
        .append("('").append(name).append("', '").append(birthDay)
        .append("', '").append(count).append("')");
  }

  public static void insertDuplicateVoters() throws SQLException {
    sqlQuery.append("SELECT id FROM ").append(VOTERS_TAB).append(" ORDER BY id DESC LIMIT 1");
    ResultSet rs = getResultSet();

    rs.next();
    int sizeTab = rs.getInt("id");
    rs.close();

    String name;
    String birthDate;

    for (int i = 0; i < sizeTab; ) {
      sqlQuery.append("SELECT birthDate, name FROM ").append(VOTERS_TAB)
          .append(" WHERE id = '").append(++i).append("'");
      rs = getResultSet();

      rs.next();
      name = rs.getString("name");
      birthDate = rs.getString("birthDate");
      rs.close();

      sqlQuery.append("SELECT birthDate, name, COUNT(*) AS `count` FROM ").append(VOTERS_TAB)
          .append(" WHERE birthDate = '").append(birthDate)
          .append("' AND name = '").append(name).append("'");
      rs = getResultSet();

      rs.next();
      short count = rs.getShort("count");
      rs.close();

      if (count > 1) {
        countDuplicateVoter(name, birthDate, count);
      }
    }

    sqlQuery.append("INSERT IGNORE INTO ").append(DUPLICATE_VOTERS_TAB)
        .append(" (name, birthDate, `count`) VALUES ").append(insertVotersQuery);
    executeQuery();
  }

  private static void countStationWorkTime(short index, String time) {
    String[] dayTime = time.split(" ");

    insertStationsQuery.append(insertStationsQuery.length() == 0 ? "" : ",")
        .append("('").append(index).append("', '").append(dayTime[0]).append("', '")
        .append(dayTime[1]).append("', '").append(dayTime[1]).append("')");
  }

  public static void insertStationsWorkTimes() throws SQLException {
    sqlQuery.append("SELECT id FROM ").append(STATIONS_TAB).append(" ORDER BY id DESC LIMIT 1");
    ResultSet rs = getResultSet();

    rs.next();
    int sizeTab = rs.getInt("id");
    rs.close();

    short index;
    String time;

    for (int i = 0; i < sizeTab; ) {
      sqlQuery.append("SELECT `index`, time FROM ").append(STATIONS_TAB)
          .append(" WHERE id = '").append(++i).append("'");
      rs = getResultSet();

      rs.next();
      index = rs.getShort("index");
      time = rs.getString("time");
      rs.close();

      countStationWorkTime(index, time);
    }

    sqlQuery.append("INSERT INTO ").append(STATIONS_WORK_TIMES_TAB)
        .append("(`index`, date, start, end) ").append("VALUES").append(insertStationsQuery)
        .append(" ON DUPLICATE KEY UPDATE start = IF (start > VALUES(start),")
        .append(" VALUES(start), start), end = IF (end < VALUES(end), VALUES(end), end)");
    executeQuery();
    insertStationsQuery.setLength(0);
  }

  public static void printVoterCounts() throws SQLException {
    StringBuilder output = new StringBuilder();

    sqlQuery.append("SELECT * FROM ").append(DUPLICATE_VOTERS_TAB);
    ResultSet rs = getResultSet();

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

    sqlQuery.append("SELECT * FROM ").append(STATIONS_WORK_TIMES_TAB);
    ResultSet rs = getResultSet();

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
