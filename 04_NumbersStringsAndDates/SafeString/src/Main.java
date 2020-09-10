public class Main
{
    public static void main(String[] args)
    {
        String safe = searchAndReplaceDiamonds("Номер кредитной карты <4008 1234 5678> 8912" +
                " пользователя <Петрова> Алексея", "***");
        System.out.println(safe);
    }

    public static String searchAndReplaceDiamonds(String text, String placeholder)
    {
        for (int i = 0; i < text.length(); i++) {
            if (text.charAt(i) == '<' && text.contains(">")) {
                int beginIndex = text.indexOf('<');
                int endIndex = text.indexOf('>') + 1;

                if (beginIndex < endIndex) {
                    text = text.replaceAll(text.substring(beginIndex, endIndex), placeholder);
                }
            }
        }
        return text;
    }
}
