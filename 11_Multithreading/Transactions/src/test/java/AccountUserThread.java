//Класс потока, для тестирования класса Account на потокобезоапсность
public class AccountUserThread extends Thread {

  Account account;

  AccountUserThread(Account account) {
    this.account = account;
  }

  @Override
  public void run() {
    account.deposit(100);
    System.out.println(this.toString() + account.getMoney());
    account.writeOff(100);
    System.out.println(this.toString() + account.getMoney());
    account.deposit(100);
    System.out.println(this.toString() + account.getMoney());
    account.writeOff(100);
    System.out.println(this.toString() + account.getMoney());
    account.deposit(100);
    System.out.println(this.toString() + account.getMoney());
    account.writeOff(100);
    System.out.println(this.toString() + account.getMoney());
  }
}
