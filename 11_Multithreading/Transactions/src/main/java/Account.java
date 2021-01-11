import lombok.Getter;
import lombok.Setter;

//POJO-класс Account
public class Account {

  //номера счёта
  @Getter
  @Setter
  private String accNumber;

  //сумма денег на счету
  @Getter
  @Setter
  private volatile long money;

  //флаг блокировки счёта
  @Getter
  @Setter
  private boolean blocked;
}
