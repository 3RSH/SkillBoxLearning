import com.skillbox.airport.*;
import java.util.Comparator;
import java.util.Date;

public class Main
{
    public static void main(String[] args)
    {

        int coutAircrafts = 0;

        Airport airPort = Airport.getInstance();

        for (Terminal t : airPort.getTerminals())
        {
            coutAircrafts += t.getParkedAircrafts().size();

            System.out.println("================================================");
            System.out.println("================================================");
            System.out.println("- Терминал " + t.getName() + ":");
            System.out.println("================================================");

            for (int i = 0; i < t.getParkedAircrafts().size(); i++)
            {
                if ((i + 1)/10 == 0)
                {
                    System.out.print("-0" + (i + 1) + " ");
                    System.out.println(t.getParkedAircrafts().get(i) + ";");
                } else {
                    System.out.print("-" + (i + 1) + " ");
                    System.out.println(t.getParkedAircrafts().get(i) + ";");
                }
            }
        }

        System.out.println("================================================");
        System.out.println("================================================");
        System.out.println("Количество (число) самолётов в аэропорту: " + coutAircrafts + ";");

        System.out.println("\nСамолёты, вылетающие в ближайшие два часа:\n");

        final long ONE_HOUR_IN_MILLIS = 3600000;

        Date startTime = new Date();
        Date endTime = new Date(System.currentTimeMillis() + 2 * ONE_HOUR_IN_MILLIS);

        for (Terminal t : airPort.getTerminals()) {
            System.out.println("- Терминал " + t.getName() + ":");

            t.getFlights().stream()
                .filter(flight -> flight
                    .getDate()
                    .after(startTime))
                .filter(flight -> flight
                    .getDate()
                    .before(endTime))
                .sorted(Comparator.comparing(Flight::getDate))
                .forEach(flight -> System.out.println(flight + " / " + flight.getAircraft()));
        }
    }
}
