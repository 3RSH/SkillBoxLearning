import java.util.Scanner;

public class Main
{
    public static void main(String[] args)
    {
        Scanner scanner = new Scanner(System.in);
        String[] name = {"", "", ""};
        String fullName = scanner.nextLine();
        String[] SplitName = fullName.split(" ", 3);

        for (int i = 0; i < SplitName.length; i++) {
            name[i] = SplitName[i];
        }

        System.out.println("Фамилия: " + name[0] + "\nИмя: " + name[1] + "\nОтчество: " + name[2]);
    }
}
