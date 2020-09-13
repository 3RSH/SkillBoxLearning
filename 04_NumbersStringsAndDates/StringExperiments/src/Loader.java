import static java.lang.Integer.parseInt;

public class Loader
{
    public static void main(String[] args)
    {
        String text = "Вася заработал 5000 рублей, Петя - 7563 рубля, а Маша - 30000 рублей";

        System.out.println(text);
        System.out.println("Общая сумма чисел в строке: " + getSum(text));

        System.out.println("Общая сумма чисел в строке: " + getSumRegex(text));
    }

    //метод извлекает ВСЕ числа из строки, не зависимо от содержимого
    public static int getSum(String str)
    {
        int sum = 0;
        int beginIndex = -1;
        int endIndex = -1;
        String sumString;

        for (int i = 0; i < str.length(); i++) {

            int code = str.charAt(i);

            if (code > 47 && code < 58) {
                if (i == 0 || beginIndex == -1) {
                    beginIndex = i;
                }
                endIndex = i + 1;

            } else {
                if (beginIndex != -1){
                    sumString = str.substring(beginIndex, endIndex);
                    sum += parseInt(sumString);
                }

                beginIndex = -1;
                endIndex = -1;
            }

            //корректное чтение чисел в конце строки
            if (i == (str.length() - 1) && beginIndex != -1) {
                sumString = str.substring(beginIndex);
                sum += parseInt(sumString);
            }
        }
        return sum;
    }

    public static int getSumRegex (String str)
    {
        int sum = 0;
        String[] strings = str
                .replaceAll("[^0-9]+", " ") // заменяем все нечисла на пробелы
                .trim() // убираем пробелы с начала и конца
                .split("\\s+"); // разделяем строку по пробелам

        for (String s : strings) {
            sum += Integer.parseInt(s);
        }
        return sum;
    }
}