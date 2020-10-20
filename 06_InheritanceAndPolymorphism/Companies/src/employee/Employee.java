package employee;

import сompany.Company;

public interface Employee {

  //увольнение
  void fire();

  //получение размера зарплаты
  Integer getMonthSalary();

  //получение ссылки на компанию
  Company getCompany();

  //установка ссылки на компанию
  void setCompany(Company company);
}
