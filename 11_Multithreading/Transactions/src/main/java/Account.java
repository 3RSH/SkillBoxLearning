import lombok.Getter;
import lombok.Setter;

public class Account {

  //маска номера счёта (константа)
  private static final String maskOfNumber = "\\d{1,4}";

  //номера счёта с заданным значением(шаблоном)
  @Getter
  private String accNumber = "0000";

  //сумма денег на счету
  private long money;

  //флаг блокировки счёта
  @Getter
  @Setter
  private boolean blocked;

  //Конструктор
  public Account(String accNumber, long money) {

    //задаём номер счёта в формате "четыре цифры"
    if (accNumber.matches(maskOfNumber)) {
      this.accNumber = this.accNumber
          .substring(0, this.accNumber.length() - accNumber.length())
          + accNumber;
    }

    //задаём баланс счёта, если он указан корректно
    if (money > 0) {
      this.money = money;
    }

    //задаём значение флага блокировки
    blocked = false;
  }

  //Геттер баланса счёта
  public long getMoney() {
    return money;
  }

  //Сеттер баланса счёта
  //ПРИВАТНЫЙ, т.к. используется только внутри самого класса счёта
  //, для потокобезопасности
  private void setMoney(long money) {
      this.money = money;
  }

  //Пополнение счёта (synchronized-метод)
  public synchronized boolean deposit(long amount) {
    boolean possibility = !isBlocked() && amount > 0;

    if (possibility) {
      this.setMoney(this.getMoney() + amount);
    }

    return possibility;
  }

  //Списание со счёта (synchronized-метод)
  public synchronized boolean writeOff(long amount) {
    boolean possibility = !isBlocked() && amount > 0 && amount <= this.getMoney();

    if (possibility) {
      this.setMoney(this.getMoney() - amount);
    }

    return possibility;
  }
}
