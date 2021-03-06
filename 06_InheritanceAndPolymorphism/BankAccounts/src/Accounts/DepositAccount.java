package Accounts;

import java.math.BigDecimal;
import java.time.LocalDate;

public class DepositAccount extends BankAccount {

  private static final int TERM_IN_MONTHS = 1; //срок депозита (кол-во месяцев)
  private static final String ERROR_MESSAGE = "Срок депозита ещё не вышел - снятие невозможно!";

  private LocalDate inputDate; //дата последнего взноса на счёт

  //Конструктор
  public DepositAccount(BigDecimal account) {
    super(account);
    inputDate = LocalDate.now();
  }

  //Внесение на счёт (переопределено)
  @Override
  public void deposit(BigDecimal amount) {
    super.deposit(amount);
    inputDate = LocalDate.now();
  }

  //Снятие со счёта (переопределено)
  @Override
  public boolean withdraw(BigDecimal amount) {
    if (LocalDate.now().isAfter(inputDate.plusMonths(TERM_IN_MONTHS))) {
      return super.withdraw(amount);
    }

    System.out.println(ERROR_MESSAGE);
    return false;
  }
}
