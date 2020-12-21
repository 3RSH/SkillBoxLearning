import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.Metadata;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;

public class Main {

  private static final String URL = "jdbc:mysql://localhost:3306/skillbox?serverTimezone=UTC";
  private static final String USER = "root";
  private static final String PASS = "testtest";
  private static final String QUERY_SQL = "SELECT name, "
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

  private static final StandardServiceRegistry REGISTRY = new StandardServiceRegistryBuilder()
      .configure("hibernate.cfg.xml")
      .build();
  private static final Metadata METADATA = new MetadataSources(REGISTRY).getMetadataBuilder()
      .build();
  
  public static void main(String[] args) {

    //домашняя работа 10.1
    getBuyingStatisticOfCourses();

    //домашняя работа 10.2
    getCourseObjectNameAndStudentsCount();
  }

  private static void getBuyingStatisticOfCourses() {
    StringBuilder output = new StringBuilder();

    try (Connection connection = DriverManager.getConnection(URL, USER, PASS);
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery(QUERY_SQL)
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

  private static void getCourseObjectNameAndStudentsCount() {
    //инициализируем SessionFactory
    SessionFactory sessionFactory = METADATA.getSessionFactoryBuilder().build();
    Session session = sessionFactory.openSession();

    //получаем объект класса Course
    Course course = session.get(Course.class, 1);

    //закрываем SessionFactory
    sessionFactory.close();

    StringBuilder output = new StringBuilder()
        .append(course.getName())
        .append(". Количество студентов:")
        .append(course.getStudentsCount());

    System.out.println(output.toString());
  }
}
