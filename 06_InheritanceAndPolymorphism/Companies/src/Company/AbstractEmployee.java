package Company;

public abstract class AbstractEmployee implements Employee {

  Company company; //ссылка на компанию

  //получение размера зарплаты
  @Override
  public abstract Integer getMonthSalary();

  //увольнение
  @Override
  public void fire() {
    company = null;
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
}
