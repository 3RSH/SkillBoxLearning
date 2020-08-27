
public class Loader
{
    public static void main(String[] args)
    {
        System.out.println("Lesson 1.");
        System.out.println("================================================");

        Cat[] cats = {new Cat(), new Cat(),new Cat(),new Cat(),new Cat(),new Cat()};
        System.out.println("Создано " + (cats.length) + " кошек.");

        System.out.println("================================================");
        System.out.println("- 1.:");

        for (int i = 0; i < cats.length; i++)
        {
            System.out.println("Вес кошки № " + (i + 1) + " равен: " +
                    cats[i].getWeight() + ";");
        }

        System.out.println("================================================");
        System.out.println("- 2.:");

        System.out.println("Покормили кошку № 2 на 100 г;");
        cats[1].feed(100.0);
        System.out.println("Покормили кошку № 5 на 120 г;");
        cats[4].feed(120.0);

        System.out.println("------------------------------------------------");

        for (int i = 0; i < cats.length; i++)
        {
            System.out.println("Вес кошки № " + (i + 1) + " равен: " +
                    cats[i].getWeight() + ";");
        }

        System.out.println("================================================");
        System.out.println("- 3.:");

        while (cats[5].getStatus() != "Exploded")
        {
            System.out.println("Покормили кошку № 6 на 500 г;");
            cats[5].feed(500.0);
            System.out.println("Вес кошки № 6 стал " + cats[5].getWeight() + ";");
        }

        System.out.println("Кошка № 6 " + cats[5].getStatus() + ";");

        System.out.println("================================================");
        System.out.println("- 4.:");


        while (cats[0].getStatus() != "Dead")
        {
            System.out.print("Кошка № 1 говорит: ");
            cats[0].meow();
            System.out.println("Вес кошки № 1 стал " + cats[0].getWeight() + ";");
        }

        System.out.println("Кошка № 1 " + cats[0].getStatus() + ";");

        System.out.println("================================================");

        System.out.println("Lesson 2.");
        System.out.println("================================================");

        System.out.println("- 1.:");

        Cat vaska = new Cat();
        System.out.println("Создана новая кошка: Cat vaska;");
        System.out.println("Вес кошки Cat vaska равен: " + vaska.getWeight() + ";");

        System.out.println("================================================");
        System.out.println("- 2.:");

        vaska.feed(150.0);
        System.out.println("Покормили Cat vaska на 150 г;");
        System.out.println("Вес Cat vaska равен: " + vaska.getWeight() + ";");

        System.out.println("================================================");
        System.out.println("- 3.:");

        System.out.println("Cat vaska пошёл в туалет:");
        for (int i = 0; i < 3; i++) {
            vaska.pee();
        }

        System.out.println("Вес Cat vaska равен: " + vaska.getWeight() + ";");

        System.out.println("================================================");
        System.out.println("- 4.:");

        System.out.println("Cat vaska всего съел " + vaska.getFeedingWeight() + " г;");

        System.out.println("================================================");


    }
}