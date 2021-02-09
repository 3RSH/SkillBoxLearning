import org.redisson.Redisson;
import org.redisson.api.RKeys;
import org.redisson.api.RScoredSortedSet;
import org.redisson.api.RedissonClient;
import org.redisson.client.RedisConnectionException;
import org.redisson.config.Config;

public class RedisStorage {

  //Ключь ScoredSortedSet в Redis
  private static final String KEY = "USERS";

  // Объект для работы с Redis
  private RedissonClient redisson;

  // Объект для работы с Sorted Set-ом
  private RScoredSortedSet<String> users;

  private long score = 0;

  //Инициализация ScoredSortedSet в Redis
  void init() {

    Config config = new Config();
    config.useSingleServer().setAddress("redis://127.0.0.1:6379");

    try {
      redisson = Redisson.create(config);
    } catch (RedisConnectionException Exc) {
      System.out.println("Не удалось подключиться к Redis");
      System.out.println(Exc.getMessage());
    }

    // Объект для работы с ключами
    RKeys rKeys = redisson.getKeys();
    users = redisson.getScoredSortedSet(KEY);
    rKeys.delete(KEY);
  }

  //Генератор Score для ScoredSortedSet
  private long getScore() {
    return score++;
  }

  //Добавление элемента в ScoredSortedSet
  void addUser(String userId) {

    //ZADD USERS
    users.add(getScore(), userId);
  }

  //Получение первого элемента в ScoredSortedSet
  String getFirstUserId() {

    //ZRANGE USERS 0 0
    return users.first();
  }
}
