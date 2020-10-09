package Clients;

import java.math.BigDecimal;

public class SoleProprietor extends Client {

  //Процент коммисии (0.01 = 1%)
  private static final BigDecimal onePercent = BigDecimal.valueOf(0.01);
  //Процент коммисии (0.005 = 0.5%)
  private static final BigDecimal halfPercent = BigDecimal.valueOf(0.005);
  //Пороговое значение суммы, для корректировки процента коммисии
  private static final BigDecimal threshold = BigDecimal.valueOf(1000);

  public SoleProprietor(BigDecimal amount) {
    super(amount);
  }

  //Вывод в консоль информации о клиенте (переопределено)
  @Override
  public void printInfo() {
    StringBuilder info = new StringBuilder()
        .append(getClass().getSimpleName())
        .append("\n\tПополнение:\n")
        .append("\t\tкомиссия 1%, ")
        .append("если сумма меньше 1000 руб. ;\n")
        .append("\t\tкомиссия 0,5%, ")
        .append("если сумма больше либо равна 1000 руб. ;\n")
        .append("\tСнятие происходит без комиссии.\n")
        .append("\tСумма счёта: ")
        .append(getAccount())
        .append(" руб.\n");

    System.out.println(info);
  }

  //Внесение на счёт (переопределено)
  @Override
  public void deposit(BigDecimal amount) {

    BigDecimal percent = amount.compareTo(threshold) < 0
        ? onePercent
        : halfPercent;

    amount = amount.subtract(amount.multiply(percent));
    super.deposit(amount);
  }
}
