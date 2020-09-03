import java.util.Scanner;

public class Main
{
    public static final int TRUCK_CAPACITY = 12;
    public static final int CONTAINER_CAPACITY = 27;

    public static void main(String[] args)
    {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Введите количество ящиков: ");
        int boxCount = Integer.parseInt(scanner.next());
        int truckCount = 0, containerCount = 0;

        if (boxCount > 0) {
            for (int i = 1; i <= boxCount;) {
                truckCount++;
                System.out.println("\nГрузовик " + truckCount + ":");

                for (int j = TRUCK_CAPACITY; j > 0; j--) {
                    containerCount++;
                    System.out.println("Контейнер " + containerCount + ":");

                    for (int k = CONTAINER_CAPACITY; k > 0; k--) {
                        System.out.println("\tЯщик " + i);
                        i++;
                        if (i > boxCount) { break;}
                    }

                    if (i > boxCount) { break;}
                }
            }
        }

        System.out.println("=======================================" +
                "\nНеобходимо:\nгрузовиков - " + truckCount + " шт." +
                "\nконтейнеров - " + containerCount + " шт.");

    }
}
