package Company;

public class Manager implements Employee {

  private Company company; //ссылка на компанию
  private int monthSalary = 0; //зарплата

  //установка ссылки на компанию,
  //генерация заработка для компании,
  //и установка зарплаты.
  @Override
  public void setCompany(Company company) {
    this.company = company;

    if (company != null) { //(company == null) при увольнении !!!

      //генерируем заработок для компании (от 115000 до 140000)
      int income = (int) (Math.random() * 25001) + 115000;

      //передаём заработок в компанию
      company.setIncome(company.getIncome() + income);
      monthSalary = company.getManagerFixSalary() + (int) (company.getManagerPercent() * income);
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
