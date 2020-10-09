package Company;

public class TopManager implements Employee {

  private Company company; //ссылка на компанию
  private int monthSalary = 0; //зарплата

  //установка ссылки на компанию,
  //и установка зарплаты.
  @Override
  public void setCompany(Company company) {
    this.company = company;

    if (company != null) { //(company == null) при увольнении !!!

      //зарплата с бонусом
      int salaryWithBonus = company.getTopManagerFixSalary()
          + (int) (company.getTopManagerPercent() * company.getTopManagerFixSalary());

      //условие начисления бонуса
      boolean hasBonus = company.getIncome() > company.getGoodIncome();

      monthSalary = hasBonus
          ? salaryWithBonus
          : company.getTopManagerFixSalary();
    } else {
      monthSalary = 0; //обнуляем заплату при увольнении
    }
  }

  //получение размера зарплаты сотрудника
  @Override
  public Integer getMonthSalary() {
    return monthSalary;
  }
}
