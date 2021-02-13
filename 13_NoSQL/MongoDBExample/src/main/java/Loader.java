import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import org.bson.Document;

public class Loader {

  private static final String CSV_FILE = "13_NoSQL/MongoDBExample/src/main/resources/mongo.csv";

  private static final String QUERY_COUNT_STUDENTS = "Общее количество студентов в базе: ";
  private static final String QUERY_COUNT_STUDENTS_OVER_40_YO
      = "Количество студентов старше 40 лет: ";
  private static final String QUERY_YOUNGEST_STUDENT_NAME = "Имя самого молодого студента: ";
  private static final String QUERY_COURSES_OF_OLDEST_STUDENT
      = "Список курсов самого старого студента: ";

  public static void main(String[] args) {

    //Формирование базы MongoDB из CSV-файла
    try (BufferedReader reader = new BufferedReader(new FileReader(CSV_FILE))
        ; MongoClient mongoClient = new MongoClient("127.0.0.1", 27017)) {

      MongoDatabase database = mongoClient.getDatabase("local");
      MongoCollection<Document> collection = database.getCollection("students");

      collection.drop();

      collection.insertMany(mapStudentsToDocuments(mapFileToStudents(reader)));

    } catch (IOException e) {
      e.printStackTrace();
    }

    //Работа с базой (выполнение запросов)
    try (MongoClient mongoClient = new MongoClient("127.0.0.1", 27017)) {

      MongoDatabase database = mongoClient.getDatabase("local");
      MongoCollection<Document> collection = database.getCollection("students");

      //ЗАПРОС: db.students.count()
      System.out.println(QUERY_COUNT_STUDENTS + collection.countDocuments());

      //ЗАПРОС: db.students.find({"age" : { $gt: 40}}).count()
      BasicDBObject query = new BasicDBObject();
      query.put("age", new BasicDBObject("$gt", 40));

      MongoCursor<Document> cursor = collection.find(query).cursor();

      int i = 0;
      while (cursor.hasNext()) {
        i++;
        cursor.next();
      }

      System.out.println(QUERY_COUNT_STUDENTS_OVER_40_YO + i);

      //ЗАПРОС: db.students.find({}, {age: 0, courses: 0}).sort({age: 1}).limit(1)
      query = new BasicDBObject();
      query.put("age", 1);
      Document student = collection.find().sort(query).first();
      System.out.println(QUERY_YOUNGEST_STUDENT_NAME + student.get("name"));

      //ЗАПРОС: db.students.find({}, {name: 0, age: 0}).sort({age: -1}).limit(1)
      query = new BasicDBObject();
      query.put("age", -1);
      student = collection.find().sort(query).first();
      System.out.println(QUERY_COURSES_OF_OLDEST_STUDENT + student.get("courses"));
    }
  }

  private static List<Student> mapFileToStudents(BufferedReader reader) {
    return reader.lines().map(Loader::mapLineToStudent).collect(Collectors.toList());
  }

  private static Student mapLineToStudent(String line) {
    Student student = new Student();

    String nestedData = line.substring(line.indexOf("\"") + 1
        , line.lastIndexOf("\""));
    String data = line.substring(0, line.indexOf(nestedData));

    student.setName(data.split(",")[0]);
    student.setAge(Integer.parseInt(data.split(",")[1]));
    student.setCourses(nestedData.split(","));

    return student;
  }

  private static List<Document> mapStudentsToDocuments(List<Student> students) {
    return students.stream().map(Loader::mapStudentToDocument)
        .collect(Collectors.toList());
  }

  private static Document mapStudentToDocument(Student student) {
    BasicDBList nestedDocument = new BasicDBList();

    nestedDocument.addAll(Arrays.asList(student.getCourses()));

    return new Document()
        .append("name", student.getName())
        .append("age", student.getAge())
        .append("courses", nestedDocument);
  }
}