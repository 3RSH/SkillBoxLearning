import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

public class Main {

  private static final String url = "jdbc:mysql://localhost:3306/skillbox?serverTimezone=UTC";
  private static final String user = "root";
  private static final String pass = "testtest";
  private static final String querySQL = "SELECT name, "
      + "COUNT(name) / (MAX(MONTH(subscription_date)) - MIN(MONTH(subscription_date)) + 1) "
      + "AS purchases_per_month "
      + "FROM ("
      + "SELECT name, subscription_date "
      + "FROM courses "
      + "JOIN purchaselist "
      + "ON courses.name = purchaselist.course_name "
      + "WHERE YEAR(subscription_date) = 2018 "
      + "ORDER BY name, subscription_date"
      + ") AS work_table "
      + "GROUP BY name;";

  public static void main(String[] args) {

    StringBuilder output = new StringBuilder();

    try (Connection connection = DriverManager.getConnection(url, user, pass);
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery(querySQL)
    ) {

      while (resultSet.next()) {
        output.append(resultSet.getString(1))
            .append(" - ")
            .append(resultSet.getString(2))
            .append("\n");
      }
    } catch (Exception ex) {
      ex.printStackTrace();
    }

    System.out.println(output.toString());
  }
}
