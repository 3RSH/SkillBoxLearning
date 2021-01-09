import junit.framework.TestCase;

//Тест класса Account
public class AccountTest extends TestCase {

  Account account;

  @Override
  public void setUp() throws Exception {
    account = new Account("1", 1000);
  }

  //Тестирование работы класса в многопоточной среде
  public void testMultiThread() {

    //стартуем 1000 потоков
    for (int i = 0; i < 500; i++) {
      AccountUserThreadDeposit accountUserIn = new AccountUserThreadDeposit(account);
      accountUserIn.start();
    }

    for (int i = 0; i < 500; i++) {
      AccountUserThreadWriteOff accountUserOut = new AccountUserThreadWriteOff(account);
      accountUserOut.start();
    }

    //ждём завершения их работы
    try {
      Thread.sleep(2000);
    } catch (InterruptedException e) {
      e.printStackTrace();
    }

    /*
      Потоки зачисляют и снимают деньги со счёта,
      в равном количестве. По итогу, сумма счёта
      не должна измениться(сумма счёта была равна
      нулю).
     */

    long expected = 1000;
    long actual = account.getMoney();

    //сравниваем ожидаемый результат с полученным
    assertEquals(expected, actual);
  }
}
