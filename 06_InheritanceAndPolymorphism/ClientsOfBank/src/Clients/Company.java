package Clients;

import java.math.BigDecimal;

public class Company extends Client {

  //Процент коммисии (0.01 = 1%)
  private static final BigDecimal percent = BigDecimal.valueOf(0.01);

  public Company(BigDecimal amount) {
    super(amount);
  }

  //Вывод в консоль информации о клиенте (переопределено)
  @Override
  public void printInfo() {
    StringBuilder info = new StringBuilder()
        .append(getClass().getSimpleName())
        .append("\n\tПополнение происходит без комиссии, снятие - с комиссией 1%.\n")
        .append("\tСумма счёта: ")
        .append(getAccount())
        .append(" руб.\n");

    System.out.println(info);
  }

  //Снятие со счёта (переопределено)
  @Override
  public void withdraw(BigDecimal amount) {
    amount = amount.add(amount.multiply(percent));
    super.withdraw(amount);
  }
}
