package Clients;

import java.math.BigDecimal;

public class Person extends Client {

  public Person(BigDecimal amount) {
    super(amount);
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
