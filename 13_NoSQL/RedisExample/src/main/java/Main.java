import java.util.Random;

public class Main {

  private static final int USER_COUNT = 20;
  private static final int PAYMENT_CYCLE = 10;
  private static final String STATUS = "- На главной странице показываем пользователя %s.\n";
  private static final String PAYMENT = "> Пользователь %s оплатил платную услугу.\n";

  private static int cycleCount;
  private static int paymentMarker;
  private static String paidUser;

  public static void main(String[] args) {

    //Инициализируем базу Redis
    RedisStorage redis = new RedisStorage();
    redis.init();

    //Заполняем базу элементами
    for (int i = 1; i <= USER_COUNT; i++) {
      redis.addUser(String.valueOf(i));
    }

    //инициализируем переменные цикла платного действия
    resetPaymentCycle();

    //рабочий цикл
    while (true) {
      if (cycleCount != paymentMarker) {
        action(redis);
      } else {
        paidAction(redis);
      }

      if (cycleCount == PAYMENT_CYCLE) {
        resetPaymentCycle();
      } else {
        cycleCount++;
      }

      try {
        Thread.sleep(1000);
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
    }
  }

  //Сброс переменных цикла платного действия
  private static void resetPaymentCycle() {
    cycleCount = 0;
    paidUser = String.valueOf(1 + new Random().nextInt(USER_COUNT));
    paymentMarker = new Random().nextInt(PAYMENT_CYCLE);
  }

  //Обычное действие
  private static void action(RedisStorage redis) {
    System.out.printf(STATUS, redis.getFirstUserId());
    redis.addUser(redis.getFirstUserId());
  }

  //Платное действие
  private static void paidAction(RedisStorage redis) {
    String user = redis.getUserById(paidUser);
    System.out.printf(PAYMENT + STATUS, user, user);
  }
}
