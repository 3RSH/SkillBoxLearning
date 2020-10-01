import java.math.BigDecimal;
import java.math.RoundingMode;

public class CardAccount extends BankAccount {

  //Конструктор
  public CardAccount(BigDecimal account) {
    super(account);
    setAccType("КАРТОЧНЫЙ");
  }

  //Снятие со счёта (переопределено)
  @Override
  public boolean withdraw(BigDecimal amount) {

    BigDecimal percent = BigDecimal.valueOf(0.01); //процент от суммы снятия
    amount = amount.add(amount.multiply(percent))
        .setScale(2, RoundingMode.HALF_DOWN); //сумма снятия

    return super.withdraw(amount);
  }

  //перевод на другой счёт (переопределён)
  @Override
  public boolean send(BankAccount receiver, BigDecimal amount) {
    if (withdraw(amount)) {
      receiver.deposit(amount);
      return true;
    }

    return false;
  }
}
