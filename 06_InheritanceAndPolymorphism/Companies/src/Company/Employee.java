package Company;

public interface Employee {

  //увольнение
  void fire();

  //получение размера зарплаты
  Integer getMonthSalary();

  //получение ссылки на компанию
  Company getCompany();

  //получение дохода для компании
  int getIncome();

  //установка ссылки на компанию
  void setCompany(Company company);
}
