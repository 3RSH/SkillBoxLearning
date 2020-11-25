import java.time.LocalDate;

class Movement {

  private final LocalDate date;
  private final String name;
  private final double income;
  private final double expense;
  private final String mcc;

  public Movement(LocalDate date, String name, double income, double expense,
      String mcc) {
    this.date = date;
    this.name = name;
    this.income = income;
    this.expense = expense;
    this.mcc = mcc;
  }

  public String getName() {
    return name;
  }

  public double getIncome() {
    return income;
  }

  public double getExpense() {
    return expense;
  }

  public LocalDate getDate() {
    return date;
  }

  public String getMcc() {
    return mcc;
  }
}
