package Clients;

import java.math.BigDecimal;

public class SoleProprietor extends Client {

  //Процент коммисии (0.01 = 1%)
  private static final BigDecimal mainPercent = BigDecimal.valueOf(0.01);
  //Корректировка процента (множитель)
  private static final BigDecimal percentCorrection = BigDecimal.valueOf(0.5);
  //Пороговое значение суммы, для корректировки процента коммисии
  private static final BigDecimal threshold = BigDecimal.valueOf(1000);

  //Конструктор
  public SoleProprietor(BigDecimal amount) {
    account = BigDecimal.valueOf(0);
    deposit(amount);
  }

  //Вывод в консоль информации о клиенте (переопределено)
  @Override
  public void printInfo() {
    StringBuilder info = new StringBuilder()
        .append(getClass().getSimpleName())
        .append("\n\tПополнение:\n")
        .append("\t\tкомиссия 1%, ")
        .append("если сумма меньше 1000 руб. ;\\n")
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
    BigDecimal percent = mainPercent;

    if (amount.compareTo(threshold) >= 0) {
      percent = percent.multiply(percentCorrection);
    }

    amount = amount.subtract(amount.multiply(percent));
    super.deposit(amount);
  }
}
