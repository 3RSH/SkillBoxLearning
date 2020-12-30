import Entities.Course;
import Entities.LinkedPurchase;
import Entities.LinkedPurchase.Key;
import Entities.Purchase;
import Entities.Student;
import Entities.Teacher;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaDelete;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
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

    try (SessionFactory sessionFactory = METADATA.getSessionFactoryBuilder().build()) {

      Session session = sessionFactory.openSession();

      //домашняя работа 10.2
      getCourseObjectNameAndStudentsCount(session);

      //домашняя работа 10.2*
      getTeachers(session);

      //домашняя работа 10.3(пример работы сущностей)
      showExampleOfEntitiesConnections(session);

      //домашняя работа 10.4 ======================================

      //очистка таблицы LinkedPurchaseList (для повторного заполнения)
      eraseLinkedPurchaseList(session);

      //создаём список для сущностей типа LinkedPurchase
      List<LinkedPurchase> linkedPurchases = new ArrayList<>();

      //подготавливаем запрос всех сущностей типа Purchase
      CriteriaBuilder builder = session.getCriteriaBuilder();
      CriteriaQuery<Purchase> criteriaQuery = builder.createQuery(Purchase.class);
      criteriaQuery.from(Purchase.class);

      //делаем запрос и создаём список всех сущностей типа Purchase
      List<Purchase> purchases = session.createQuery(criteriaQuery).getResultList();

      //обработка списка сущностей purchases
      for (Purchase purchase : purchases) {

        //ОПРЕДЕЛЯЕМ ID СТУДЕНТА ==================================

        //получаем имя студента из сущности типа Purchase
        String studentName = purchase.getStudentName();

        //подготавливаем запрос сущности типа Student, с именем студента из сущности Purchase
        CriteriaQuery<Student> studentCriteriaQuery = builder.createQuery(Student.class);
        Root<Student> studentRoot = studentCriteriaQuery.from(Student.class);
        studentCriteriaQuery.select(studentRoot)
            .where(builder.equal(studentRoot.get("name"), studentName));

        //делаем запрос и инициализируем сущность типа Student
        Student student = session.createQuery(studentCriteriaQuery).getSingleResult();

        //получаем id из сущности типа Student
        int studentId = student.getId();

        //ОПРЕДЕЛЯЕМ ID КУРСА ======================================

        //получаем имя курса из сущности типа Purchase
        String courseName = purchase.getCourseName();

        //подготавливаем запрос сущности типа Course, с именем курса из сущности Purchase
        String hql = "from " + Course.class.getSimpleName() + " where name = '" + courseName + "'";

        //делаем запрос и инициализируем сущность типа Course
        Course course = (Course) session.createQuery(hql).getSingleResult();

        //получаем id из сущности типа Course
        int courseId = course.getId();

        //ЗАПОЛНЯЕМ СПИСОК СУЩНОСТЕЙ ТИПА LinkedPurchase ===========

        //создаём и инициализируем сущьность типа LinkedPurchase
        //, на основании полученных id студента, и id курса
        LinkedPurchase linkedPurchase = new LinkedPurchase();
        linkedPurchase.setId(new Key(studentId, courseId));

        //добавляем список сущностей типа LinkedPurchase в список
        linkedPurchases.add(linkedPurchase);
      }

      //начинаем/открываем транзакцию
      Transaction transaction = session.beginTransaction();

      //занесение элементов списка linkedPurchases в таблицу
      linkedPurchases.forEach(session::save);

      //проводим транзакцию
      transaction.commit();
    }
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

  private static void getCourseObjectNameAndStudentsCount(Session session) {

    //получаем объект класса Entities.Course
    Course course = session.get(Course.class, 1);

    System.out.println(course.getName()
        + ". Количество студентов:"
        + course.getStudentsCount());
  }

  private static void getTeachers(Session session) {
    CriteriaBuilder builder = session.getCriteriaBuilder();
    CriteriaQuery<Teacher> criteria = builder.createQuery(Teacher.class);
    criteria.from(Teacher.class);

    //получаем список объектов класса Entities.Teacher
    List<Teacher> teachers = session.createQuery(criteria).getResultList();

    //выводим всех учителей с врзрастом и зарплатой
    teachers.forEach(System.out::println);
  }

  private static void showExampleOfEntitiesConnections(Session session) {
    //получаем сущность типа Course, с ID = 3
    Course course = session.get(Course.class, 3);

    //выводим имя курса
    System.out.println(course.getName());

    //выводим список студенов, подписанных на данный курс
    //, используя связь @OneToMany в сущности Course
    course.getSubscriptions()
        .forEach(s -> System.out.println(" - " + s.getStudent().getName()));
  }

  private static void eraseLinkedPurchaseList(Session session) {

    //инициализируем CriteriaBuilder(сборщик критерия)
    CriteriaBuilder builder = session.getCriteriaBuilder();

    //инициализируем CriteriaDelete(удалятор) из CriteriaBuilder
    CriteriaDelete<LinkedPurchase> criteriaDelete = builder
        .createCriteriaDelete(LinkedPurchase.class);

    //инициализируем Root
    //(БЕЗ НЕГО ТРАНЗАКЦИЯ НЕ РАБОТАЕТ: UPDATE/DELETE criteria must name root entity)
    Root<LinkedPurchase> root = criteriaDelete.from(LinkedPurchase.class);

    //начинаем/открываем транзакцию
    Transaction transaction = session.beginTransaction();

    session.createQuery(criteriaDelete).executeUpdate();

    //проводим транзакцию
    transaction.commit();
  }
}
