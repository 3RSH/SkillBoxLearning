import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "teachers")
public class Teacher {

  @Id
  private int id;
  private String name;
  private int salary;
  private int age;

  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public int getSalary() {
    return salary;
  }

  public void setSalary(int salary) {
    this.salary = salary;
  }

  public int getAge() {
    return age;
  }

  public void setAge(int age) {
    this.age = age;
  }

  @Override
  public String toString() {
    StringBuilder output = new StringBuilder()
        .append(name)
        .append(". Возраст: ")
        .append(age)
        .append(" лет. Зарплата: ")
        .append(salary)
        .append(" USD.");

    return output.toString();
  }
}
