import java.util.Scanner;

public class Main
{
    public static void main(String[] args)
    {
        //originalSolution();
        regexSolution();
    }

    public static void originalSolution()
    {
        Scanner scanner = new Scanner(System.in);
        String[] name = {"", "", ""};

        while (true) {
            System.out.print("Введите Фамилию Имя Отчество: ");
            String fullName = scanner.nextLine();
            int spaceCount = 0;

            for (int i = 0; i < fullName.length(); i++) {
                if (fullName.charAt(i) == ' ') {
                    spaceCount++;
                }
            }

            //проверка количества пробелов
            if (spaceCount == 2) {
                int enterSpace = fullName.indexOf(' ');
                int exitSpace = fullName.lastIndexOf(' ');
                //проверка_1 - последовательность пробелов
                boolean check1 = (exitSpace - enterSpace) > 1;
                //проверка_2 - пробел в начале строки
                boolean check2 = enterSpace > 0;
                //проверка_3 - пробел в конце строки
                boolean check3 = exitSpace < (fullName.length()-1);

                if (check1 && check2 && check3) {
                    int code1 = fullName.charAt(0);
                    int code2 = fullName.charAt(enterSpace + 1);
                    int code3 = fullName.charAt(enterSpace + 1);
                    //проверка 1-го слова на наличие первой заглавной буквы
                    boolean checkCode1 = ((code1 > 64) && (code1 < 91)) || ((code1 > 1039) && (code1 < 1072));
                    //проверка 2-го слова на наличие первой заглавной буквы
                    boolean checkCode2 = ((code2 > 64) && (code2 < 91)) || ((code2 > 1039) && (code2 < 1072));
                    //проверка 3-го слова на наличие первой заглавной буквы
                    boolean checkCode3 = ((code3 > 64) && (code3 < 91)) || ((code3 > 1039) && (code3 < 1072));

                    if (checkCode1 && checkCode2 && checkCode3) {
                        name[0] = fullName.substring(0, enterSpace);
                        name[1] = fullName.substring(enterSpace + 1, exitSpace);
                        name[2] = fullName.substring(exitSpace + 1);
                        break;
                    }
                    System.out.println(errorMessage());
                    continue;
                }
                System.out.println(errorMessage());
                continue;
            }
            System.out.println(errorMessage());
        }
        System.out.println("Фамилия: " + name[0] + "\nИмя: " + name[1] + "\nОтчество: " + name[2]);
    }

    public static void regexSolution()
    {
        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.print("Введите Фамилию Имя Отчество: ");
            String fullName = scanner.nextLine();

            if (!fullName.matches("([A-ZА-Я][A-zА-я]*\\s){2}[A-ZА-Я][A-zА-я]*")) {
                System.out.println(errorMessage());
                continue;
            }

            String[] name = fullName.split("\\s");
            System.out.println("Фамилия: " + name[0] + "\nИмя: " + name[1] + "\nОтчество: " + name[2]);
            break;
        }
    }

    public static String errorMessage()
    {
        return "Данные введены некорректно!";
    }
}
