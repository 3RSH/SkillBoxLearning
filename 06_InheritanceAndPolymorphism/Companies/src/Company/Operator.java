package Company;

public class Operator implements Employee {

  //константа фиксированной части зарплаты
  private static final int operatorFixSalary = 20000;

  private Company company; //ссылка на компанию

  //увольнение
  @Override
  public void fire() {
    company = null;
  }

  //получение размера зарплаты
  @Override
  public Integer getMonthSalary() {
    if (company != null) {
      return operatorFixSalary;
    }

    return 0;
  }

  //получение ссылки на компанию
  @Override
  public Company getCompany() {
    return company;
  }

  //установка ссылки на компанию
  @Override
  public void setCompany(Company company) {
    if (this.company == null) {
      this.company = company;
    }
  }

  //получение дохода для компании
  @Override
  public int getIncome() {
    return -getMonthSalary();
  }
}
