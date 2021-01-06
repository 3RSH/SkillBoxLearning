import java.util.ArrayList;
import java.util.List;
import junit.framework.TestCase;

//Тест класса Bank
public class BankTest extends TestCase {

  Bank bank;

  @Override
  public void setUp() {

    //инициализируем экземпляр класса Bank
    bank = new Bank();

    //добавляем 4-е счёта в коллекцию
    for (int i = 0; i < 5; i++) {
      bank.addAccount(new Account(String.valueOf(i), 500000));
    }
  }

  //Тестирование работы класса в многопоточной среде
  public void testMultiThread() {

    //считаем и получаем начальный баланс банка
    List<Long> balancesStart = new ArrayList<>();
    bank.getAccounts().forEach((s, account) -> balancesStart.add(bank.getBalance(s)));

    long expected = balancesStart.stream().mapToLong(Long::longValue).sum();

    //стартуем 60 потоков, которые переводят 500 со счёта "0004" на счёт "0001"
    //, и выводят баланс этих счетов после транзакции
    for (int i = 0; i < 60; i++) {
      new Thread(() -> {
        bank.transfer("0004", "0001", 500);
        System.out.println("Thread_500_4-1: " + bank.getBalance("0004")
            + " " + bank.getBalance("0001"));
      }).start();
    }

    //стартуем 35 потоков, которые переводят 20000 со счёта "0001" на счёт "0002"
    //, и выводят баланс этих счетов после транзакции
    for (int i = 0; i < 35; i++) {
      new Thread(() -> {
        bank.transfer("0001", "0002", 20000);
        System.out.println("Thread_2000_1-2: " + bank.getBalance("0001")
            + " " + bank.getBalance("0002"));
      }).start();
    }

    //стартуем 5 потоков, которые переводят 51000(! c проверкой Службой Бехзопасности)
    //со счёта "0004" на счёт "0001", и выводят баланс этих счетов после транзакции
    for (int i = 0; i < 5; i++) {
      new Thread(() -> {
        bank.transfer("0002", "0003", 51000);
        System.out.println("Thread_51000_2-3: " + bank.getBalance("0002")
            + " " + bank.getBalance("0003"));
      }).start();
    }

    //ждём завершения их работы
    try {
      Thread.sleep(10000);
    } catch (InterruptedException e) {
      e.printStackTrace();
    }

    //считаем и получаем конечный баланс банка
    List<Long> balancesEnd = new ArrayList<>();
    bank.getAccounts().forEach((s, account) -> balancesEnd.add(bank.getBalance(s)));

    long actual = balancesEnd.stream().mapToLong(Long::longValue).sum();

    //выводим балансы счетов, с указанием статусов их блокировки
    System.out.println("0001 " + bank.getBalance("0001") + " "
        + bank.getAccounts().get("0001").isBlocked());

    System.out.println("0002 " + bank.getBalance("0002") + " "
        + bank.getAccounts().get("0002").isBlocked());

    System.out.println("0003 " + bank.getBalance("0003") + " "
        + bank.getAccounts().get("0003").isBlocked());

    System.out.println("0004 " + bank.getBalance("0004") + " "
        + bank.getAccounts().get("0004").isBlocked());

    //сравниваем начальный баланс банка с конечным
    assertEquals(expected, actual);
  }
}
