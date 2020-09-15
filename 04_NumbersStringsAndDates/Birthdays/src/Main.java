import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Locale;

public class Main
{
    public static final int DAY = 5;
    public static final int MONTH = 11;
    public static final int YEAR = 1984;

    public static void main(String[] args)
    {

        getBirthdaysByCalendar(DAY, MONTH, YEAR);
        System.out.println("================================================");
        getBirthdaysByJavaTime(DAY, MONTH, YEAR);

    }

    public static void getBirthdaysByCalendar(int day, int month, int year)
    {
        Locale locale = new Locale("ru", "RUS");
        DateFormat dateFormat = new SimpleDateFormat(" - dd.MM.yyyy - EE", locale);

        int age = 0;

        Calendar calendarNow = Calendar.getInstance();
        Calendar calendarBirth = Calendar.getInstance();
        calendarBirth.set(Calendar.YEAR, year);
        calendarBirth.set(Calendar.MONTH, --month);
        calendarBirth.set(Calendar.DATE, day);

        while (calendarBirth.before(calendarNow))
        {
            System.out.println(age++ + dateFormat.format(calendarBirth.getTime()));
            calendarBirth.set(Calendar.YEAR, ++year);
        }
    }

    public static void getBirthdaysByJavaTime(int day, int month, int year)
    {
        Locale locale = new Locale("ru", "RUS");
        DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern(" - dd.MM.yyyy - E", locale);

        int age = 0;

        Year currentYear = Year.now();

        while (year < currentYear.getValue())
        {
            System.out.println(age++ + dateFormat.format(LocalDate.of(year, Month.of(month), day)));
            year++;
        }

    }

}
