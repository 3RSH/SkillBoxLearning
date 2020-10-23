import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;


public class Main {

  private static String staffFile = "07_AdvancedOOPFeatures/LambdaExpressions/data/staff.txt";
  private static String dateFormat = "dd.MM.yyyy";

  public static void main(String[] args) {
    ArrayList<Employee> staff = loadStaffFromFile();

    Collections.sort(staff, (e1, e2) -> {

      if (e1.getSalary().compareTo(e2.getSalary()) == 0) {
        return e1.getName().compareTo(e2.getName());
      }

      return e1.getSalary().compareTo(e2.getSalary());
    });

    for (Employee e : staff) {
      System.out.println(e.toString());
    }

    System.out.println();

    staff.stream()
        .filter(e -> e.toString().matches(".+2017"))
        .max(Comparator.comparing(Employee::getSalary))
        .ifPresent(System.out::println);
  }

  private static ArrayList<Employee> loadStaffFromFile() {
    ArrayList<Employee> staff = new ArrayList<>();

    try {
      List<String> lines = Files.readAllLines(Paths.get(staffFile));

      for (String line : lines) {
        String[] fragments = line.split("\t");

        if (fragments.length != 3) {
          System.out.println("Wrong line: " + line);
          continue;
        }

        staff.add(new Employee(
            fragments[0],
            Integer.parseInt(fragments[1]),
            (new SimpleDateFormat(dateFormat)).parse(fragments[2])));
      }
    } catch (Exception ex) {
      ex.printStackTrace();
    }

    return staff;
  }
}