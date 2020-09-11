public class Main
{
    public static void main(String[] args)
    {
        String safe = searchAndReplaceDiamonds("Номер > кредитной карты <4008 1234 5678> 8912" +
                " пользователя <Петрова> Алексея>", "***");
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
              //если '>' попадается раньше '<', то заменяем его "\n#\n"
            } else if (text.charAt(i) == '>') {
                text = text.replaceFirst(text.substring(i, i + 1), "\n#\n");
            }
        }
        //меняем все "\n#\n" обратно на '>'
        text = text.replaceAll("\n#\n", ">");
        return text;
    }
}
