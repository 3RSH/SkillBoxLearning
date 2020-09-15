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
    public static final Locale LOCALE = new Locale("ru", "RUS");
    public static final String PATTERN_DATE = " - dd.MM.yyyy - EE";

    public static void main(String[] args)
    {

        getBirthdaysByCalendar(DAY, MONTH, YEAR);
        System.out.println("================================================");
        getBirthdaysByJavaTime(DAY, MONTH, YEAR);

    }

    public static void getBirthdaysByCalendar(int day, int month, int year)
    {
        DateFormat dateFormat = new SimpleDateFormat(PATTERN_DATE, LOCALE);

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
        DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern(PATTERN_DATE, LOCALE);

        int age = 0;

        LocalDate birthday = LocalDate.of(year, month, day);
        LocalDate today = LocalDate.now();

//        while (birthday.isBefore(today))
//        {
//            System.out.println(age++ + dateFormat.format(birthday));
//            birthday = birthday.plusYears(1);
//        }

        for (LocalDate date = birthday; date.isBefore(today); date = date.plusYears(1))
        {
            System.out.println(age++ + dateFormat.format(date));
        }
    }
}
