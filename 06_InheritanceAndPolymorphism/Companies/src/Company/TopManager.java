package Company;

public class TopManager extends AbstractEmployee {

  //константа фиксированной части зарплаты
  private static final int topManagerFixSalary = 60000;

  //константа процента Топ-менеджера
  private static final float topManagerPercent = 1.5f;

  //константа порогового значения дохода компании,
  //для начисления бонусов
  private static final long goodIncome = 10000000;

  //получение размера зарплаты
  @Override
  public Integer getMonthSalary() {
    if (company == null) {
      return 0;
    }
    int bonus = company.getIncome() > goodIncome
        ? (int)(topManagerPercent * topManagerFixSalary)
        : 0;
    return topManagerFixSalary + bonus;
  }
}
