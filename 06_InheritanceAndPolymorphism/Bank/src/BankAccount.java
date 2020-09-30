import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;

public class BankAccount {

  private static int count = 0; //счётчик

  private final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("dd.MM.yyyy");

  private String accType = "ПРОСТОЙ"; //тип счёта
  private Calendar dateCreate; //дата создания

  private Calendar dateLastIn; //дата поледнего вложения
  private Calendar dateLastOut; //дата последнего снятия
  private BigDecimal account; //сумма счёта

  //Конструктор
  public BankAccount(BigDecimal account) {
    this.account = account.setScale(2, RoundingMode.HALF_DOWN);
    setDateCreate(new GregorianCalendar());
    if (!account.equals(BigDecimal.valueOf(0))) {
      setDateLastIn(new GregorianCalendar());
    }
    count++;
  }

  //Геттер count
  public static int getCount() {
    return count;
  }

  //Геттер accType
  public String getAccType() {
    return accType;
  }

  //Сеттер accType
  public void setAccType(String accType) {
    this.accType = accType;
  }

  //Геттер account
  public BigDecimal getAccount() {
    return account;
  }

  //Сеттер dateCreate
  public void setDateCreate(Calendar dateCreate) {
    this.dateCreate = dateCreate;
  }

  //Геттер dateLastIn
  public Calendar getDateLastIn() {
    return dateLastIn;
  }

  //Сеттер dateLastIn
  public void setDateLastIn(Calendar dateLastIn) {
    this.dateLastIn = dateLastIn;
  }

  //Сеттер dateLastOut
  public void setDateLastOut(Calendar dateLastOut) {
    this.dateLastOut = dateLastOut;
  }

  //Получение информации по счёту
  public String getInfo() {
    StringBuilder info = new StringBuilder();

    info.append("\tТип счёта: ")
        .append(accType)
        .append(";\n\tСумма: ")
        .append(this.getAccount())
        .append(" руб. ;\n\tДата создания: ")
        .append(DATE_FORMAT.format(dateCreate.getTime()))
        .append(" г. ;\n\tДата последнего пополнения: ");

    if (dateLastIn == null) {
      info.append("- ;\n\tДата последнего снятия: ");
    } else {
      info.append(DATE_FORMAT.format(dateLastIn.getTime()))
          .append(" г. ;\n\tДата последнего снятия: ");
    }

    if (dateLastOut == null) {
      info.append("- ;\n");
    } else {
      info.append(DATE_FORMAT.format(dateLastOut.getTime()))
          .append(" г. ;\n");
    }

    return info.toString();
  }

  //Внесение на счёт
  public void deposit(BigDecimal amount) {
    this.account = this.account.add(amount);
    setDateLastIn(new GregorianCalendar());
  }

  //Снятие со счёта
  public boolean withdraw(BigDecimal amount) {
    if (this.account.compareTo(amount) < 0) {
      System.out.println("На счёте недостаточно средств!");
      return false;
    }

    this.account = this.account.subtract(amount);
    setDateLastOut(new GregorianCalendar());
    return true;
  }

  //перевод на другой счёт
  public boolean send(BankAccount receiver, BigDecimal amount) {
    if (withdraw(amount)) {
      receiver.deposit(amount);
      return true;
    }

    return false;
  }
}
