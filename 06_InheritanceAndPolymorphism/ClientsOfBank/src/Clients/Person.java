package Clients;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class Person extends Client {

  //Конструктор
  public Person(BigDecimal amount) {
    account = amount.setScale(SCALE, RoundingMode.HALF_DOWN);
  }

  //Вывод в консоль информации о клиенте
  @Override
  public void printInfo() {
    StringBuilder info = new StringBuilder()
        .append(getClass().getSimpleName())
        .append("\n\tПополнение и снятие происходит без комиссии.\n")
        .append("\tСумма счёта: ")
        .append(getAccount())
        .append(" руб.\n");

    System.out.println(info);
  }
}
