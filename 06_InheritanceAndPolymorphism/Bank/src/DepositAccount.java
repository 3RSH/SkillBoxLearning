import java.math.BigDecimal;
import java.util.Calendar;
import java.util.GregorianCalendar;

public class DepositAccount extends BankAccount {

  //Конструктор
  public DepositAccount(BigDecimal account) {
    super(account);
    setAccType("ДЕПОЗИТ");
  }

  //Снятие со счёта (переопределено)
  @Override
  public boolean withdraw(BigDecimal amount) {

    Calendar currentDate = new GregorianCalendar();

    int day = currentDate.get(Calendar.DAY_OF_MONTH);
    int depositDay = this.getDateLastIn().get(Calendar.DAY_OF_MONTH);

    int month = currentDate.get(Calendar.MONTH);
    int depositMonth = this.getDateLastIn().get(Calendar.MONTH);

    int year = currentDate.get(Calendar.YEAR);
    int depositYear = this.getDateLastIn().get(Calendar.YEAR);

    boolean condition1 = year != depositYear;
    boolean condition2 = day >= depositDay;
    boolean condition3 = month > depositMonth;

    boolean situate1 = condition1 && condition2;
    boolean situate2 = condition2 && condition3;

    if (situate1 || situate2) {
      return super.withdraw(amount);
    }

    System.out.println("Срок депозита ещё не вышел - снятие невозможно!");
    return false;
  }
}
