import com.skillbox.airport.*;
import com.skillbox.airport.Flight.Type;
import java.util.Comparator;
import java.util.Date;

public class Main {

  public static void main(String[] args) {
    Airport airPort = Airport.getInstance();

    System.out.println("\nСамолёты, вылетающие в ближайшие два часа:\n");

    final long ONE_HOUR_IN_MILLIS = 3600000;

    Date startTime = new Date();
    Date endTime = new Date(System.currentTimeMillis() + 2 * ONE_HOUR_IN_MILLIS);

    airPort.getTerminals().stream()
        .flatMap(t -> t.getFlights().stream())
        .filter(f -> f.getType().equals(Type.ARRIVAL))
        .filter(flight -> flight
            .getDate()
            .after(startTime))
        .filter(flight -> flight
            .getDate()
            .before(endTime))
        .sorted(Comparator.comparing(Flight::getDate))
        .forEach(f -> System.out.println(f + " / " + f.getAircraft()));
  }
}
