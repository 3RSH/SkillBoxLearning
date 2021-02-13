import lombok.Getter;
import lombok.Setter;

public class Student {

  @Getter
  @Setter
  private String name;

  @Getter
  @Setter
  private int age;

  @Getter
  @Setter
  private String[] courses;
}
