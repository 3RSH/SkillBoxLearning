import com.skillbox.airport.*;

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

    }
}
