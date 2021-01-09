import java.util.HashMap;
import java.util.Random;
import lombok.Getter;

public class Bank {

  //шаблон номера "нулевого" счёта, для его исключения (константа)
  //(логически неправильно иметь в наличии счёт с номером "0000")
  private static final String NULL_NUM_ACC = "0000";

  //порог суммы транзакции, для подключения Службы Безопасности,
  //(метод isFraud)
  private static final long THRESHOLD = 50000;

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

    //проверка isNotNull и isNotEquals
    if (from == null || to == null || from.equals(to)) {
      return false;
    }

    //маркер успешности операции
    //, для выхода из цикла попыток
    boolean result = false;

    //цикл попыток выолнения транзакции
    //, для ожидания работы конкурирующего потока
    while (!result) {

      //попытка занять монитор from
      //, если не получилось - следующий проход цикла
      if (from.getLock().tryLock()) {

        //попытка занять монитор to
        //, если не получилось - освобождаем монитор from
        //, и следующий проход цикла
        if (to.getLock().tryLock()) {
          //здесь мониторы from и to захвачены
          //, и сними теперь поток может спокойно работать

          //пробуем сделать списание с from
          //, если успешно, то
          if (from.writeOff(amount)) {

            //пробуем сделать зачисление на to
            //, если НЕ успешно, то
            if (!to.deposit(amount)) {

              //делаем зачисление обратно на from
              //, и возвращаем false (инвертированный результат
              //обратного зачисления на from - он будет true)
              return !from.deposit(amount);
            }

            //проверка суммы для Службы Безопасности
            if (amount > THRESHOLD) {
              try {
                //проверка транзакции Службой Безопасности
                from.setBlocked(isFraud(fromAccountNum, toAccountNum, amount));
                to.setBlocked(from.isBlocked());
              } catch (InterruptedException e) {
                e.printStackTrace();
              }
            }
          }
          //освобождаем монитор to
          to.getLock().unlock();

          //меняем маркер выхода из цикла
          result = true;
        }
        //освобождаем монитор from
        from.getLock().unlock();
      }
    }

    //возвращаем true
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
    //(synchronized-блок по счёту)
    synchronized (accounts.get(accountNum)) {
      return accounts.get(accountNum).getMoney();
    }
  }

  //Метод добавления счёта в коллекцию accounts
  public synchronized void addAccount(Account account) {

    //проверяем корректность номера счёта
    if (!account.getAccNumber().equals(NULL_NUM_ACC)
        && !accounts.containsKey(account.getAccNumber())) {

      //добавляем счёт в коллекцию
      accounts.put(account.getAccNumber(), account);

    }
  }
}
