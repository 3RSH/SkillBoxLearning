package Company;

public class Manager extends AbstractEmployee implements EmployeeGenerateIncome {

  //константа фиксированной части зарплаты
  private static final int managerFixSalary = 40000;

  //константы процента менеджера
  private static final float managerPercent = 0.05f;

  //генерация дохода
  @Override
   public int getGeneratedIncome() {
    return (int)(Math.random() * 25001) + 115000;
  }

  //получение размера зарплаты
  @Override
  public Integer getMonthSalary() {
    return company == null ? 0
        : managerFixSalary + (int) (managerPercent * getGeneratedIncome());
  }
}
