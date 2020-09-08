public class Main
{
    public static void main(String[] args)
    {
        String safe = searchAndReplaceDiamonds("Номер кредитной карты <4008 1234 5678> 8912", "***");
        System.out.println(safe);
    }

    public static String searchAndReplaceDiamonds(String text, String placeholder)
    {
        int beginIndex = text.indexOf('<');
        int endIndex = text.lastIndexOf('>') + 1;

        if (beginIndex < endIndex) {
            text = text.replaceAll(text.substring(beginIndex, endIndex), placeholder);
        }
        return text;
    }
}
