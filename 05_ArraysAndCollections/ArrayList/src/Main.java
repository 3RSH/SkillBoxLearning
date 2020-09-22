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
        String[] commandParts = cmd.split("\\s+", 3);

        //команда LIST
        if (cmd.matches(Actions.LIST.getRegex())) {
            list();

        //команда ADD
        } else if (cmd.matches(Actions.ADD_TO_INDEX.getRegex())) {
            String data = commandParts[2];
            int index = Integer.parseInt(commandParts[1]);

            add(index, data);
        } else if (cmd.matches(Actions.ADD.getRegex())) {
            StringBuilder data = new StringBuilder();

            for (int i = 1; i < commandParts.length; i++) {
                data.append(commandParts[i]).append(" ");
            }

            add(data);

        //команда EDIT
        } else if (cmd.matches(Actions.EDIT.getRegex())) {
            edit(Integer.parseInt(commandParts[1]), commandParts[2]);

        //команда DELETE
        } else if (cmd.matches(Actions.DELETE.getRegex())) {
            delete(Integer.parseInt(commandParts[1]));

        //команда EXIT
        } else if (cmd.matches(Actions.EXIT.getRegex())){
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
        if (index >= todoList.size()) {
            todoList.add(str);
            return;
        }

            todoList.add(index, str);
    }

    public static void add(StringBuilder str)
    {
        todoList.add(str.toString());
    }

    public static void edit(int index, String str)
    {
        if  (index >= todoList.size()) {
            System.out.println("Элемента с индексом " + index + " в списке нет!");
            return;
        }

        todoList.set(index, str);
    }

    public static void delete(int index)
    {
        if  (index >= todoList.size()) {
            System.out.println("Элемента с индексом " + index + " в списке нет!");
            return;
        }
        
        todoList.remove(index);
    }

    public static void errorMessage()
    {
        System.out.println("Команда введена некорректно!");
    }
}
