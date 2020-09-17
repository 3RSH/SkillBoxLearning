public class Main
{
    public static void main(String[] args)
    {
        String text = "Каждый охотник желает знать, где сидит фазан.";
        String[] colors = text.split("[^А-я]\\s*");

        System.out.print("Первоначальный массив: [ ");

        //прямой проход по массиву
        for (int i = 0; i < colors.length; i++)
        {
            System.out.print(colors[i] + " ");
        }
        System.out.println("]");

        //переворот массива
        upendArray(colors);

        System.out.print("Полученный массив: [ ");

        //прямой проход по массиву
        for (int i = 0; i < colors.length; i++)
        {
            System.out.print(colors[i] + " ");
        }
        System.out.println("]");
    }

    public static void upendArray (String[] array)
    {
        for (int i = 0; i < array.length/2; i++)
        {
            String buffer = array[i];
            array[i] = array[(array.length - 1) - i];
            array[(array.length - 1) - i] = buffer;
        }
    }
}
