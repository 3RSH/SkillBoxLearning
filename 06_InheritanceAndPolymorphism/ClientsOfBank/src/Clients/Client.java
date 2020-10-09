package Clients;

import java.math.BigDecimal;
import java.math.RoundingMode;

abstract class Client {

  protected static final int SCALE = 2; //ограничение количества знаков после запятой
  private static final String ERROR_MESSAGE = "На счёте недостаточно средств!";

  //сумма счёта (изначально, всегда равна нулю!)
  private BigDecimal account = BigDecimal.valueOf(0);

  protected Client(BigDecimal amount) {
    deposit(amount);
  }

  //Вывод в консоль информации о клиенте
  abstract void printInfo();

  //Внесение на счёт
  public void deposit(BigDecimal amount) {
    amount = amount.setScale(SCALE, RoundingMode.HALF_DOWN);
    account = account.add(amount);
  }

  //Снятие со счёта
  public void withdraw(BigDecimal amount) {
    amount = amount.setScale(SCALE, RoundingMode.HALF_DOWN);

    BigDecimal maxValue = account.max(amount);

    if (!account.equals(amount) && maxValue.equals(amount)) {
      System.out.println(ERROR_MESSAGE);
    } else {
      account = account.subtract(amount);
    }
  }

  //Геттер account
  protected BigDecimal getAccount() {
    return account;
  }
}
