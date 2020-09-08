public class Main
{
    public static void main(String[] args)
    {
        String alphabet = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
        getCharCode(alphabet);

        String alphabetRus = "абвгдеёжзийклмнопрстуфхцчшщъыьэюяАБВГДЕЁЖЗИЙКЛМНОПРСТУФХЦЧШЩЪЫЬЭЮЯ";
        getCharCode(alphabetRus);

    }

    public static void getCharCode(String str)
    {
        for (int i = 0; i < str.length(); i++) {
            char c = str.charAt(i);
            System.out.println(c + " - " + (int) c);
        }
        System.out.println();
    }
}