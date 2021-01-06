import java.util.HashMap;
import java.util.Random;
import lombok.Getter;

public class Bank {

  //шаблон номера "нулевого" счёта, для его исключения (константа)
  //(логически неправильно иметь в наличии счёт с номером "0000")
  private static final String nullNumAcc = "0000";

  //порог суммы транзакции, для подключения Службы Безопасности,
  //(метод isFraud)
  private static final long threshold = 50000;

  //коллекция Map, для хранения счетов банка
  @Getter
  private final HashMap<String, Account> accounts = new HashMap<>();
  private final Random random = new Random();

  //Проверка транзакции на мошенничество
  public synchronized boolean isFraud(String fromAccountNum, String toAccountNum, long amount)
      throws InterruptedException {
    Thread.sleep(1000);
    return random.nextBoolean();
  }

  /**
   * TODO: реализовать метод. Метод переводит деньги между счетами. Если сумма транзакции > 50000,
   * то после совершения транзакции, она отправляется на проверку Службе Безопасности – вызывается
   * метод isFraud. Если возвращается true, то делается блокировка счетов (как – на ваше
   * усмотрение)
   */
  //метод возвращает boolean, для проверки успешности его работы
  //, если понадобиться в дальнейшем
  public boolean transfer(String fromAccountNum, String toAccountNum, long amount) {
    //проверка существования счёта списания
    boolean checkFromAccount = accounts.containsKey(fromAccountNum);

    //проверка существования счёта зачисления
    boolean checkToAccount = accounts.containsKey(toAccountNum);

    //суммарная проверка первых трёх условий
    boolean isPossible = checkFromAccount && checkToAccount;

    //если условие isPossible не выполняется, то метод прерывается
    if (!isPossible) {
      return false;
    }

    //получаем аккаунт списания
    Account from = accounts.get(fromAccountNum);

    //получаем аккаунт зачисления
    Account to = accounts.get(toAccountNum);

    //транзакция (synchronized-блок по коллекции accounts)
    synchronized (accounts) {
      if (from.writeOff(amount)) {
        if (!to.deposit(amount)) {
          return !from.deposit(amount);
        }
      }
    }

    //проверка суммы для Службы Безопасности
    if (amount > threshold) {
      try {
        //проверка транзакции Службой Безопасности
        //(synchronized-блок по коллекции accounts)
        synchronized (accounts) {
          from.setBlocked(isFraud(fromAccountNum, toAccountNum, amount));
          to.setBlocked(from.isBlocked());
        }
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
    }

    return true;
  }

  /**
   * TODO: реализовать метод. Возвращает остаток на счёте.
   */
  public long getBalance(String accountNum) {
    //проверка существования счёта
    if (!accounts.containsKey(accountNum)) {
      return 0;
    }

    //получение остатка по счёту
    //(synchronized-блок по коллекции accounts)
    synchronized (accounts) {
      return accounts.get(accountNum).getMoney();
    }
  }

  //Метод добавления счёта в коллекцию accounts
  public void addAccount(Account account) {

    //проверяем корректность номера счёта
    if (!account.getAccNumber().equals(nullNumAcc)
        && !accounts.containsKey(account.getAccNumber())) {

      //добавляем счёт в коллекцию
      //(synchronized-блок по коллекции accounts)
      synchronized (accounts) {
        accounts.put(account.getAccNumber(), account);
      }
    }
  }
}
