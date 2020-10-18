package Company;

public class Operator extends AbstractEmployee {

  //константа фиксированной части зарплаты
  private static final int operatorFixSalary = 30000;

  //получение размера зарплаты
  @Override
  public Integer getMonthSalary() {
    return company == null ? 0 : operatorFixSalary;
  }
}
