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

        //Первичное решение:
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

        System.out.println("=======================================");
        System.out.println();
        System.out.println("=======================================");

        //Альтернативное решение, с использованием остатка отделения ( % )
        altSolution(boxCount);

    }

    public static void altSolution(int boxCount) {
        if (boxCount > 0) {

            int truckCount = boxCount / (TRUCK_CAPACITY * CONTAINER_CAPACITY);
            int containerCount = boxCount / CONTAINER_CAPACITY;

            int boxCountHalfTr = boxCount % (TRUCK_CAPACITY * CONTAINER_CAPACITY);
            int boxCountHalfCon = boxCount % CONTAINER_CAPACITY;

            int counterT = 1;
            int counterC = 1;
            int counterB = 1;

            while (counterT <= truckCount) {
                System.out.println("\nГрузовик " + counterT + ":");

                for (int i = 0; i < TRUCK_CAPACITY; i++) {
                    System.out.println("Контейнер " + counterC + ":");

                    for (int j = 0; j < CONTAINER_CAPACITY; j++) {
                        System.out.println("\tЯщик " + counterB);
                        counterB++;
                    }

                    counterC++;
                }

                counterT++;
            }

            System.out.println("\nГрузовик " + counterT + ":");

            while (counterC <= containerCount) {
                System.out.println("Контейнер " + counterC + ":");

                for (int j = 0; j < CONTAINER_CAPACITY; j++) {
                    System.out.println("\tЯщик " + counterB);
                    counterB++;
                }

                counterC++;
            }

            System.out.println("Контейнер " + counterC + ":");

            for (int i = 0; i < boxCountHalfCon; i++) {
                System.out.println("\tЯщик " + counterB);
                counterB++;
            }

            if (boxCountHalfTr == 0) {
                System.out.println("=======================================" +
                        "\nНеобходимо:\nгрузовиков - " + (counterT - 1) + " шт." +
                        "\nконтейнеров - " + (counterC - 1) + " шт.");
            } else if (boxCountHalfCon == 0) {
                System.out.println("=======================================" +
                        "\nНеобходимо:\nгрузовиков - " + counterT + " шт." +
                        "\nконтейнеров - " + (counterC - 1) + " шт.");
            } else {
                System.out.println("=======================================" +
                        "\nНеобходимо:\nгрузовиков - " + counterT + " шт." +
                        "\nконтейнеров - " + counterC + " шт.");
            }
        } else {
            System.out.println("=======================================" +
                    "\nНеобходимо:\nгрузовиков - 0 шт." +
                    "\nконтейнеров - 0 шт.");
        }
    }
}
