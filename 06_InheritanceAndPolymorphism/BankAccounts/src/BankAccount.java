import java.math.BigDecimal;
import java.math.RoundingMode;

class BankAccount {

  private static final int SCALE = 2; //ограничение количества знаков после запятой
  private static final String ERROR_MESSAGE = "На счёте недостаточно средств!";

  private static int count = 0; //счётчик (для формирования номера счёта)
  private BigDecimal account; //сумма счёта

  //Конструктор
  protected BankAccount(BigDecimal account) {
    this.account = account.setScale(SCALE, RoundingMode.HALF_DOWN);
    count++;
  }

  //Внесение на счёт
  protected void deposit(BigDecimal amount) {
    amount = amount.setScale(SCALE, RoundingMode.HALF_DOWN);
    account = account.add(amount);
  }

  //Снятие со счёта
  protected boolean withdraw(BigDecimal amount) {
    amount = amount.setScale(SCALE, RoundingMode.HALF_DOWN);

    BigDecimal maxValue = account.max(amount);

    if (!account.equals(amount) && maxValue.equals(amount)) {
      System.out.println(ERROR_MESSAGE);
      return false;
    }

    account = account.subtract(amount);
    return true;
  }

  //перевод на другой счёт
  protected boolean send(BankAccount receiver, BigDecimal amount) {
    if (withdraw(amount)) {
      receiver.deposit(amount);
      return true;
    }

    return false;
  }

  //Геттер account
  protected BigDecimal getAccount() {
    return account;
  }

  //Геттер count
  protected static int getCount() {
    return count;
  }
}
