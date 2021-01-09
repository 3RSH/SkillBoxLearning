//Класс потока, для тестирования класса Account на потокобезоапсность
public class AccountUserThreadDeposit extends Thread {

  Account account;

  AccountUserThreadDeposit(Account account) {
    this.account = account;
  }

  @Override
  public void run() {
    for (int i = 0; i < 6; i++) {
      account.deposit(100);
      System.out.println(this.toString() + account.getMoney());
    }
  }
}
