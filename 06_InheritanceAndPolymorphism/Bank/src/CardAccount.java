import java.math.BigDecimal;

public class CardAccount extends BankAccount {

  //процент (от 0.00 до 1.00)
  private static final BigDecimal percent = BigDecimal.valueOf(0.01);

  //Конструктор
  public CardAccount(BigDecimal account) {
    super(account);
  }

  //Снятие со счёта (переопределено)
  @Override
  public boolean withdraw(BigDecimal amount) {
    amount = amount.add(amount.multiply(percent)); //сумма снятия
    return super.withdraw(amount);
  }
}
