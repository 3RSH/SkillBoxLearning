import java.util.Scanner;
import java.util.TreeSet;

public class Main
{
    public static TreeSet<String> eMails = new TreeSet<>() {{
        add("fedor@mail.com");
        add("vasyliy.ivanov@gmail.ru");
    }};
    public static boolean isRun = true;

    public static void main(String[] args)
    {
        Scanner scanner = new Scanner(System.in);

        while (isRun) {
            System.out.print("Введите команду: ");
            String input = scanner.nextLine();
            commandProcessing(input);
        }
    }

    public static void commandProcessing(String cmd)
    {
        //команда LIST
        if (cmd.matches("LIST")) {
            System.out.println("Список адресов:");

            for (String str : eMails) {
                System.out.println("\t" + str);
            }

        //команда ADD с корректными данными
        } else if (cmd.matches("ADD\\s[A-Za-z][-._A-Za-z0-9]{0,28}[A-Za-z0-9]@" +
                "[A-Za-z][-A-Za-z0-9]{0,29}[A-Za-z0-9]\\.[A-Za-z]{2,3}")) {

            String str = cmd.split("\\s")[1];
            eMails.add(str.toLowerCase());

        //команда ADD с некорректными данными
        } else if (cmd.matches("ADD\\s.+")) {
            System.out.println("Формат адреса не соблюдён!");

        //команда EXIT
        } else if (cmd.matches("EXIT")) {
            isRun = false;

        //некорректный ввод комады
        } else {
            System.out.println("Команда введена некорректно!");
        }
    }
}
