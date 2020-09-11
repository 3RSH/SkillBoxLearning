import java.util.Scanner;

public class Loader
{
    public static void main(String[] args)
    {
        Scanner scanner = new Scanner(System.in);
        StringBuilder phoneNumber = new StringBuilder();

        while (true)
        {
            System.out.print("Введите номер телефона: ");
            String input = scanner.nextLine();
            phoneNumber.delete(0, phoneNumber.length());

            String[] phone = input.split("[^0-9]");

            for (int i = 0; i < phone.length; i++) {
                phoneNumber.append(phone[i]);
            }

            //проверки для ввода 11-ти цифр
            boolean checkLength11 = phoneNumber.length() == 11;
            boolean checkFirstNum11 = phoneNumber.charAt(0) == '8' || phoneNumber.charAt(0) == '7';
            boolean checkSecondNum11 = phoneNumber.charAt(1) == '9';

            if (checkLength11 && checkFirstNum11 && checkSecondNum11) {
                phoneNumber.replace(0, 1 , "7");
                break;
            }

            //проверки для ввода 10-ти цифр
            boolean checkLength10 = phoneNumber.length() == 10;
            boolean checkFirstNum10 = phoneNumber.charAt(0) == '9';

            if (checkLength10 && checkFirstNum10) {
                phoneNumber.insert(0, 7);
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
