import java.util.Scanner;

public class Loader
{
    public static void main(String[] args)
    {
        Scanner scanner = new Scanner(System.in);
        String phoneNumber;

        while (true)
        {
            System.out.print("Введите номер телефона: ");
            String input = scanner.nextLine();
            phoneNumber = input.replaceAll("[^\\d]+", "");

            if (phoneNumber.length() == 10 && phoneNumber.matches("9\\d{9}")) {
                phoneNumber = phoneNumber.replaceAll("(\\d{9})", "7$1");
                break;
            } else if (phoneNumber.length() == 11 && (phoneNumber.matches("89\\d{9}") ||
            phoneNumber.matches("79\\d{9}"))) {
                phoneNumber = phoneNumber.replaceAll("\\d(\\d{9})", "7$1");
                break;
            }
            System.out.println(errorMessage());
        }
        System.out.println(phoneNumber);
    }

    public static String errorMessage()
    {
        return "Неверный формат номера";
    }
}
