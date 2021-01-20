import java.util.HashMap;
import java.util.Random;
import lombok.Getter;

public class Bank {

  //маска номера счёта (константа)
  private static final String MASK_OF_NUMBER = "\\d{1,4}";

  //шаблон номера счёта, и образец "нулевого" счёта для его исключения (константа)
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

    //проверяем корректность данных для проведения транзакции
    if (!transferIsPossible(fromAccountNum, toAccountNum, amount)) {
      return false;
    }

    //получаем счёт списания
    Account from = accounts.get(fromAccountNum);

    //получаем счёт зачисления
    Account to = accounts.get(toAccountNum);

    //сравниваем номера счетов для задания очерёдности синхронизации
    Object lowSyncAccount = compareAccNumbers(fromAccountNum, toAccountNum) ? from : to;
    Object topSyncAccount = lowSyncAccount.equals(from) ? to : from;

    //синхронизируемся по счетам в заданной последовательности
    synchronized (lowSyncAccount) {
      synchronized (topSyncAccount) {

        //проверяем возможность проведения транзакции
        if (from.isBlocked() || to.isBlocked() || amount > from.getMoney()) {
          return false;
        }

        //списываем сумму со счёта from
        from.setMoney(from.getMoney() - amount);

        //зачисляем сумму на счёт to
        to.setMoney(to.getMoney() + amount);

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
  public void addAccount(Account account, String accNumber) {

    //проверяем и форматируем номер счёта
    if (accNumber.matches(MASK_OF_NUMBER)) {
      accNumber = NULL_NUM_ACC
          .substring(0, NULL_NUM_ACC.length() - accNumber.length())
          + accNumber;

      //синхронизируемся по коллекции accounts
      synchronized (accounts) {

        //проверяем возможность добавления счёта в коллекцию
        if (account != null && !accNumber.equals(NULL_NUM_ACC)
            && !accounts.containsKey(accNumber) && !(account.getMoney() < 0)) {

          //задаём номер счёта
          account.setAccNumber(accNumber);

          //добавляем счёт в коллекцию
          accounts.put(account.getAccNumber(), account);
        }
      }
    }
  }

  //Проверка корректности данных для проведения транзакции
  private boolean transferIsPossible(String fromAccountNum, String toAccountNum
      , long amount) {

    //проверяем наличие счетов в коллеции, и корректность суммы
    if (isAllAccountsExist(fromAccountNum, toAccountNum) && amount > 0) {
      Account from = accounts.get(fromAccountNum);
      Account to = accounts.get(toAccountNum);

      //проверяем счета на EQUALS и возвращаем результат (счета не совпадают - OK)
      return !from.equals(to);
    }

    return false;
  }

  //Проверка существования счёта в коллекции, по номеру
  private boolean isAccountExist(String accNumber) {
    return accounts.containsKey(accNumber);
  }

  //Проверка существования счетов в коллекции, по номерам
  private boolean isAllAccountsExist(String... accNumbers) {
    for (String accNumber : accNumbers) {
      if (!isAccountExist(accNumber)) {
        return false;
      }
    }
    return true;
  }

  //Сравнение номеров счетов
  private boolean compareAccNumbers(String fromAccNum, String toAccNum) {
    return fromAccNum.compareTo(toAccNum) > 0;
  }
}
