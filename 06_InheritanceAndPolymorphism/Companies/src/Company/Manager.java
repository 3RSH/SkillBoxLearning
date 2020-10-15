package Company;

public class Manager implements Employee {

  //константа фиксированной части зарплаты
  private static final int managerFixSalary = 40000;

  //константы процента менеджера
  private static final float managerPercent = 0.05f;

  private Company company; //ссылка на компанию
  private int income = 0; //принесённый доход

  //увольнение
  @Override
  public void fire() {
    company = null;
    income = 0;
  }

  //получение размера зарплаты
  @Override
  public Integer getMonthSalary() {
    if (company != null) {
      return managerFixSalary + (int) (managerPercent * income);
    }
    return 0;
  }

  //получение ссылки на компанию
  @Override
  public Company getCompany() {
    return company;
  }

  //установка ссылки на компанию,
  //и обновление заработка для компании
  @Override
  public void setCompany(Company company) {
    if (this.company == null) {
      this.company = company;
      income = (int) (Math.random() * 25001) + 115000;
      company.setIncome(company.getIncome() + income);
      company.setIncome(company.getIncome() - getMonthSalary());
    }
  }
}
