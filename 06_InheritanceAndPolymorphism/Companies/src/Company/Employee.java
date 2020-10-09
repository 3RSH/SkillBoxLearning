package Company;

public interface Employee {

  //получение размера зарплаты сотрудника
  Integer getMonthSalary();

  //установка ссылки на компанию, в которой числится сотрудник
  void setCompany(Company company);
}
