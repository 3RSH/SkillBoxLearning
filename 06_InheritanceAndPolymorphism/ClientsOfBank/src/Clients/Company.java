package Clients;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class Company extends Client {

  //Процент коммисии (0.01 = 1%)
  private static final BigDecimal percent = BigDecimal.valueOf(0.01);

  //Конструктор
  public Company(BigDecimal amount) {
    account = amount.setScale(SCALE, RoundingMode.HALF_DOWN);
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
