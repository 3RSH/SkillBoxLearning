import java.util.ArrayList;
import java.util.Scanner;

public class Main
{
    private static final ArrayList<String> todoList = new ArrayList<>(){{
        add("Первым делом");
        add("Вторым делом");
        add("Третьим делом");
    }};

    private static boolean isRun = true;

    public static void main(String[] args)
    {

        Scanner scanner = new Scanner(System.in);

        while (isRun)
        {
            System.out.print("Введите команду: ");
            String input = scanner.nextLine();
            commandProcessing(input);
        }
    }

    public static void commandProcessing(String cmd)
    {
        String[] commandParts = cmd.split("\\s", 3);
        int partsCount = commandParts.length;

        //команда LIST
        if (commandParts[0].equals(Actions.LIST.toString())) {

            list();

        //команда ADD
        } else if (commandParts[0].equals(Actions.ADD.toString()) && partsCount > 1) {

            if (commandParts[1].matches("\\d+") && partsCount > 2) {
                add(Integer.parseInt(commandParts[1]), commandParts[2]);
            } else {
                StringBuilder data = new StringBuilder();

                for (int i = 1; i < commandParts.length; i++) {
                    data.append(commandParts[i]).append(" ");
                }

                add(data);
            }

        //команда EDIT
        } else if (commandParts[0].equals(Actions.EDIT.toString()) && partsCount > 2) {

            if (commandParts[1].matches("\\d+")) {

                edit(Integer.parseInt(commandParts[1]), commandParts[2]);

            } else {
                errorMessage();
            }

        //команда DELETE
        } else if (commandParts[0].equals(Actions.values()[3].toString()) && partsCount > 1) {

            if (commandParts[1].matches("\\d+")) {

                delete(Integer.parseInt(commandParts[1]));

            } else {
                errorMessage();
            }

        //команда EXIT
        } else if (commandParts[0].equals(Actions.EXIT.toString())){

            isRun = false;
        } else {

            errorMessage();
        }
    }

    public static void list()
    {
        System.out.println("Список дел:");

        for (int i = 0; i < todoList.size(); i++)
        {
            System.out.println("\t" + i + " - " + todoList.get(i));
        }
    }

    public static void add(int index, String str)
    {
        if (todoList.size() > index) {
            todoList.add(index, str);
        } else {
            todoList.add(str);
        }
    }

    public static void add(StringBuilder str)
    {
        todoList.add(str.toString());
    }

    public static void edit(int index, String str)
    {
        if (todoList.size() > index) {
            todoList.set(index, str);
        } else {
            System.out.println("Элемента с индексом " + index + " в списке нет!");
        }
    }

    public static void delete(int index)
    {
        if (todoList.size() > index) {
            todoList.remove(index);
        } else {
            System.out.println("Элемента с индексом " + index + " в списке нет!");
        }
    }

    public static void errorMessage()
    {
        System.out.println("Команда введена некорректно!");
    }
}
