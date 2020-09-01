public class Main
{
    public static void main(String[] args)
    {
        Container container = new Container();
        container.count += 7843;

        System.out.println(sumDigits(12345));
        System.out.println(sumDigits(10));
        System.out.println(sumDigits(5059191));
    }

    public static Integer sumDigits(Integer number)
    {
        //@TODO: write code here
        Integer sum = 0;
        String str = Integer.toString(number);
        int[] arr = new int[str.length()];

        for (int i = 0; i < str.length(); i++)
        {
            arr[i] = Integer.parseInt(String.valueOf(str.charAt(i)));
        }


        for (int i: arr)
        {
            sum += i;
        }


        return sum;
    }
}
