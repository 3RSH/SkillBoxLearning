import java.util.HashMap;

public class CustomerStorage
{
    private static final String ADDING_FORMAT_MESSAGE = "Correct format example:\n"
        + "add Василий Петров vasily.petrov@gmail.com +79215637722";
    private static final String NAME_FORMAT = "[A-ZА-Я].+";
    private static final String MAIL_FORMAT = "[a-z][-._a-z0-9]{0,28}[a-z0-9]@"
        + "[a-z][-a-z0-9]{0,29}[a-z0-9]\\.[a-z]{2,3}";
    private static final String PHONE_FORMAT = "\\+79\\d{9}";

    private HashMap<String, Customer> storage;

    public CustomerStorage()
    {
        storage = new HashMap<>();
    }

    public void addCustomer(String data)
    {
        String[] components = data.split("\\s+");

        //проверка количества введённых данных
        if (components.length != 4) {
            throw new IllegalArgumentException("Wrong adding format! "
                + ADDING_FORMAT_MESSAGE);
        }

        //проверка правильности ввода Имени-Фамилии
        if (!components[0].matches(NAME_FORMAT) || !components[1].matches(NAME_FORMAT)) {
            throw new IllegalArgumentException("Wrong adding name format! "
                + ADDING_FORMAT_MESSAGE);
        }

        //проверка правильности ввода почты
        if (!components[2].matches(MAIL_FORMAT)) {
            throw new IllegalArgumentException("Wrong adding e-mail format! "
                + ADDING_FORMAT_MESSAGE);
        }

        //проверка правильности ввода номера
        if (!components[3].matches(PHONE_FORMAT)) {
            throw new IllegalArgumentException("Wrong adding number format! "
                + ADDING_FORMAT_MESSAGE);
        }

        String name = components[0] + " " + components[1];
        storage.put(name, new Customer(name, components[3], components[2]));
    }

    public void listCustomers()
    {
        storage.values().forEach(System.out::println);
    }

    public void removeCustomer(String name)
    {
        storage.remove(name);
    }

    public int getCount()
    {
        return storage.size();
    }
}