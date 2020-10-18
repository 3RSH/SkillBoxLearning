package Company;

public class TopManager implements Employee {

  //константа фиксированной части зарплаты
  private static final int topManagerFixSalary = 80000;

  //константа процента Топ-менеджера
  private static final float topManagerPercent = 1.5f;

  //константа порогового значения дохода компании,
  //для начисления бонусов
  private static final long goodIncome = 10000000;

  private Company company; //ссылка на компанию

  private int monthSalary = 0;

  //получение константы фиксированной части зарплаты
  static int getTopManagerFixSalary() {
    return topManagerFixSalary;
  }

  //получение константы процента Топ-менеджера
  static float getTopManagerPercent() {
    return topManagerPercent;
  }

  //получение константы порогового значения дохода компании
  static long getGoodIncome() {
    return goodIncome;
  }

  //увольнение
  @Override
  public void fire() {
    company = null;
    monthSalary = 0;
  }

  //получение размера зарплаты
  @Override
  public Integer getMonthSalary() {
    return monthSalary;
  }

  //получение ссылки на компанию
  @Override
  public Company getCompany() {
    return company;
  }

  //установка ссылки на компанию,
  //и установка зарплаты.
  @Override
  public void setCompany(Company company) {
    if (this.company == null) {
      this.company = company;
      monthSalary = topManagerFixSalary;
    }
  }

  //получение дохода для компании
  @Override
  public int getIncome() {
    return -topManagerFixSalary;
  }

  //сброс зарплаты до фиксированной части
  void resetMonthSalary() {
    monthSalary = topManagerFixSalary;
  }

  //корректировка зарплаты (для начисления бонуса)
  void addMonthSalary(int amount) {
    monthSalary += amount;
  }
}
