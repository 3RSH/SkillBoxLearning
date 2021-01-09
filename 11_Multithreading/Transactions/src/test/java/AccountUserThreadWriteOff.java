//Класс потока, для тестирования класса Account на потокобезоапсность
public class AccountUserThreadWriteOff extends Thread {

  Account account;

  AccountUserThreadWriteOff(Account account) {
    this.account = account;
  }

  @Override
  public void run() {
    for (int i = 0; i < 6; i++) {
      account.writeOff(100);
      System.out.println(this.toString() + account.getMoney());
    }
  }
}
