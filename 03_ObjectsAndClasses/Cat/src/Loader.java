
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

        while (!cats[5].getStatus().equals("Exploded"))
        {
            System.out.println("Покормили кошку № 6 на 500 г;");
            cats[5].feed(500.0);
            System.out.println("Вес кошки № 6 стал " + cats[5].getWeight() + ";");
        }

        System.out.println("Кошка № 6 " + cats[5].getStatus() + ";");

        System.out.println("================================================");
        System.out.println("- 4.:");


        while (!cats[0].getStatus().equals("Dead"))
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

        System.out.println("Lesson 3.");
        System.out.println("================================================");

        System.out.println("Общее количество живых кошек: " + Cat.getCount());

        System.out.println("================================================");

        System.out.println("*");

        System.out.println("Статус кошки № 1: " + cats[0].getStatus() + " - она мертва;");
        System.out.println("Вес мёртвой кошки № 1 равен: " + cats[0].getWeight() + ";");
        System.out.print("Мёртвая кошка № 1 говорит: ");
        cats[0].meow();
        System.out.println(";");
        System.out.println("Покормили мёртвую кошку № 1 на 100 г;");
        cats[0].feed(100.0);
        System.out.println("Вес мёртвой кошки № 1: равен " + cats[0].getWeight() + ";");
        System.out.println("Попоили мёртвую кошку № 1 на 100 г;");
        cats[0].drink(100.0);
        System.out.println("Вес мёртвой кошки № 1: равен " + cats[0].getWeight() + ";");
        System.out.print("Пытаемся заставить мёртвую кошку № 1 пойти в туалет: ");
        cats[0].pee();
        System.out.println(";");

        System.out.println("================================================");

        System.out.println("*");

        System.out.println("Статус кошки № 6: " + cats[5].getStatus() + " - она мертва;");
        System.out.println("Вес мёртвой кошки № 6 равен: " + cats[5].getWeight() + ";");
        System.out.print("Мёртвая кошка № 6 говорит: ");
        cats[5].meow();
        System.out.println(";");
        System.out.println("Покормили мёртвую кошку № 6 на 100 г;");
        cats[5].feed(100.0);
        System.out.println("Вес мёртвой кошки № 6: равен " + cats[5].getWeight() + ";");
        System.out.println("Попоили мёртвую кошку № 6 на 100 г;");
        cats[5].drink(100.0);
        System.out.println("Вес мёртвой кошки № 6: равен " + cats[5].getWeight() + ";");
        System.out.print("Пытаемся заставить мёртвую кошку № 6 пойти в туалет: ");
        cats[5].pee();
        System.out.println(";");

        System.out.println("================================================");

        System.out.println("Lesson 5.");
        System.out.println("================================================");

        Cat[] kittens = {getKitten(), getKitten(), getKitten()};
        System.out.println("Создано три котёнка:");

        for (int i = 0; i < kittens.length; i++)
        {
            System.out.println("Вес котёнка № " + (i + 1) + " равен: " +
                    kittens[i].getWeight() + ";");
        }

        System.out.println("================================================");

        System.out.println("Lesson 7.");
        System.out.println("================================================");

        System.out.println("Счётчик живых кошек: " + Cat.getCount() + ";");

        System.out.print("Создаём новую кошку Cat murka, копируя её с Cat vaska, " +
                "через специальный конструктор класса Cat: ");
        Cat murka = new Cat(vaska);
        System.out.println(vaska != murka ? "кошки разные;" : "одна и та же кошка;");
        System.out.println("Счётчик живых кошек: " + Cat.getCount() + ";");
        System.out.print("Перезаписываем в Cat murka, ещё одну копию с Cat vaska, " +
                "через специальный метод класса Loader, который использует геттеры и сеттеры: ");
        murka = copyCat(vaska);
        System.out.println(vaska != murka ? "кошки разные;" : "одна и та же кошка;");
        System.out.println("Счётчик живых кошек: " + Cat.getCount() + ";");

        System.out.println("================================================");

        System.out.println("Теперь у нас в памяти сидит первая копия Cat vaska, " +
                "с которой теперь ничего сделать нельзя. )))");

        System.out.println("================================================");
    }

    private static Cat getKitten()
    {
        return new Cat(1100.0);
    }

    private static Cat copyCat(Cat cat)
    {
        Cat nCat = new Cat();
        nCat.setOriginWeight(cat.getOriginWeight());
        nCat.setWeight(cat.getWeight());
        nCat.setFeedingWeight(cat.getFeedingWeight());
        nCat.setMinWeight(cat.getMinWeight());
        nCat.setMaxWeight(cat.getMaxWeight());
        nCat.setColor(cat.getColor());
        return nCat;
    }
}