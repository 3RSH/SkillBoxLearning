import java.util.*;

public class Loader
{

    public static final TreeMap<String, String> phoneBook = new TreeMap<>();

    private static final String NUMBER = "\\+*\\d[-()\\s\\d]+";
    private static final String NAME = "[A-zА-я]+[-._,/()A-zА-я\\s]*";
    private static final String ERROR = "Ошибка! Номер указан некорректно.";

    private static boolean isRun = true;

    public static void main(String[] args)
    {
        Scanner scanner = new Scanner(System.in);
        StringBuilder message = new StringBuilder();

        while (isRun)
        {
            String input = scanner.nextLine();

            // вывод содержимого базы
            if (input.equals("LIST")) {
                printMapByValue();

            //выход из программы
            } else if (input.equals("EXIT")) {
                exit();
            //обработка ввода номера, соответствующего шаблону
            } else if (input.matches(NUMBER)) {
                //приводим к общему формату
                String number = numFormatting(input);

                //если не приводится, выводим ошибку и завершаем итерацию while
                if (number.equals(ERROR)) {
                    System.out.println(number);
                    continue;
                }

                //проверяем наличие номера в базе
                if (hasNumber(number)) {
                    String name = phoneBook.get(number);
                    //выводим все записи базы с именем, с которым связан введённый номер
                    message.append(name).append(" :\n");
                    for (String key : searchKeys(name))
                    {
                        message.append("\t").append(numPrintFormat(key)).append("\n");
                    }
                    System.out.print(message);
                    message.setLength(0);
                    continue;
                }
                //если номера в базе нет, просим указать имя
                System.out.print("Введите имя: ");
                input = scanner.nextLine();

                //добавляем новый элемент базы (номер - имя)
                phoneBook.put(number, input);

            //обработка ввода имени, соответствующего шаблону
            } else if (input.matches(NAME)) {
                //проверяем наличие имени в базе
                if (hasName(input)) {
                    //выводим все записи базы с таким именем
                    message.append(input).append(" :\n");
                    for (String key : searchKeys(input))
                    {
                        message.append("\t").append(numPrintFormat(key)).append("\n");
                    }
                    System.out.print(message);
                    message.setLength(0);
                    continue;
                }
                //если имени в базе нет, просим указать номер
                System.out.print("Введите номер: ");
                //приводим номер к общему формату
                String number = numFormatting(scanner.nextLine());

                //если не приводится, выводим ошибку и завершаем итерацию while
                if (number.equals(ERROR)) {
                    System.out.println(number);
                    continue;
                }

                //проверяем наличие номера в базе
                if (hasNumber(number)) {
                    //если такой номер уже есть в базе, сообщаем об этом,
                    System.out.println("Такой номер уже есть в базе:");

                    //выводим елемент базы с этим номером, и завершаем итерацию while
                    System.out.println("\t" + phoneBook.get(number) + " : " + numPrintFormat(number));
                    continue;
                }

                //добавляем новый элемент базы (номер - имя)
                phoneBook.put(number, input);
            }
        }
    }

    //вывод phoneBook упорядоченным по значениям (НЕ ПО КЛЮЧАМ)
    private static void printMapByValue()
    {
        TreeSet<String> outputSet = new TreeSet<>();
        StringBuilder str = new StringBuilder();

        for (String key : phoneBook.keySet())
        {
            str.append(phoneBook.get(key)).append(" : ").append(numPrintFormat(key));
            outputSet.add(str.toString());
            str.setLength(0);
        }

        for (String member : outputSet)
        {
            System.out.println(member);
        }
    }

    //обработка команды выхода из программы
    private static void exit()
    {
        isRun = false;
    }

    //проверка корректности номера и приведение его к общему формату хранения
    private static String numFormatting (String str)
    {
        str = str.replaceAll("\\D+", "");

        if (str.length() == 11) {
            str = str.substring(1);
        }

        if (str.length() == 10 && str.charAt(0) == '9') {
            return str;
        }

        return ERROR;
    }

    //форматирование номера для вывода
    private static String numPrintFormat (String number)
    {
        return number.replaceAll("(\\d{3})(\\d{3})(\\d{2})(\\d{2})", "+7 ($1) $2-$3-$4");
    }

    //проверка наличия номера в базе
    private static boolean hasNumber(String number)
    {
        for (String key : phoneBook.keySet())
        {
            if (number.equals(key)) {
                return true;
            }
        }
        return false;
    }

    //проверка налиичя имени в базе
    private static boolean hasName(String name)
    {
        for (String key : phoneBook.keySet())
        {
            if (name.equals(phoneBook.get(key))) {
                return true;
            }
        }
        return false;
    }

    //поиск номеров в базе по имени
    private static ArrayList<String> searchKeys (String name)
    {
        ArrayList<String> keys = new ArrayList<>();
        for (String key : phoneBook.keySet())
        {
            if (name.equals(phoneBook.get(key))) {
                keys.add(key);
            }
        }
        return keys;
    }
}
