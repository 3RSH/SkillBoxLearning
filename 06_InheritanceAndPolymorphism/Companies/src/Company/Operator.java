package Company;

public class Operator implements Employee {

  private Company company; //ссылка на компанию
  private int monthSalary = 0; //зарплата

  //установка ссылки на компанию,
  //и установка зарплаты.
  @Override
  public void setCompany(Company company) {
    this.company = company;
    monthSalary = (company != null) //(company == null) при увольнении !!!
        ? company.getOperatorFixSalary()
        : 0; // обнуляем заплату при увольнении
  }

  //получение размера зарплаты сотрудника
  @Override
  public Integer getMonthSalary() {
    return monthSalary;
  }
}
