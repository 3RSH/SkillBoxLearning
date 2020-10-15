package Company;

public class TopManager implements Employee {

  //константа фиксированной части зарплаты
  private static final int topManagerFixSalary = 80000;

  //константы процента Топ-менеджера
  private static final float topManagerPercent = 1.5f;

  //константа порогового значения дохода компании,
  //для начисления бонусов
  private static final long goodIncome = 10000000;

  private Company company; //ссылка на компанию

  //увольнение
  @Override
  public void fire() {
    company = null;
  }

  //получение размера зарплаты
  public Integer getMonthSalary() {
    if (company != null) {
      if (company.getIncome() > goodIncome) {
        company.setIncome(company.getIncome() - (int)(topManagerFixSalary * topManagerPercent));
        return topManagerFixSalary + (int)(topManagerFixSalary * topManagerPercent);
      }
      return topManagerFixSalary;
    }
    return 0;
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
      company.setIncome(company.getIncome() - topManagerFixSalary);
    }
  }
}
