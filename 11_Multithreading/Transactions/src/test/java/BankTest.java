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
      Account account = new Account();
      account.setMoney(500000);
      bank.addAccount(account, String.valueOf(i));
    }
  }

  //Тестирование работы класса в многопоточной среде
  public void testMultiThread() {

    //считаем и получаем начальный баланс банка
    List<Long> balancesStart = new ArrayList<>();
    bank.getAccounts().forEach((s, account) -> balancesStart.add(bank.getBalance(s)));

    long expected = balancesStart.stream().mapToLong(Long::longValue).sum();

    //стартуем 95 потоков, которые переводят 1 с рандомно выбранного счёта
    //на другой рандомно выбранный счёт, 1000 раз
    //, и выводят баланс этих счетов после транзакции
    for (int i = 0; i < 95; i++) {
      new Thread(() -> {
        String from = "000" + ((int) (Math.random() * 4) + 1);
        String to = "000" + ((int) (Math.random() * 4) + 1);

        for (int j = 0; j < 1000; j++) {
          bank.transfer(from, to, 1);
          System.out.println(Thread.currentThread().getName() + "_"
              + from + "-" + to + ": " + bank.getBalance(from)
              + " " + bank.getBalance(to));
        }
      }).start();
    }

    //стартуем 5 (5% от общего числа) потоков, которые переводят 51000
    //с рандомно выбранного счёта
    //на другой рандомно выбранный счёт, 10 раз
    //, и выводят баланс этих счетов после транзакции
    for (int i = 0; i < 5; i++) {
      new Thread(() -> {
        String from = "000" + ((int) (Math.random() * 4) + 1);
        String to = "000" + ((int) (Math.random() * 4) + 1);

        for (int j = 0; j < 10; j++) {
          bank.transfer(from, to, 51000);
          System.out.println(Thread.currentThread().getName() + "_"
              + from + "-" + to + ": " + bank.getBalance(from)
              + " " + bank.getBalance(to));
        }
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
    bank.getAccounts().forEach((num, account) -> System.out.println(num
        + " " + bank.getBalance(num) + " "
        + account.isBlocked()));

    //сравниваем начальный баланс банка с конечным
    assertEquals(expected, actual);
  }
}
