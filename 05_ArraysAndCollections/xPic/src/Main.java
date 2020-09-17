public class Main
{
    public static void main(String[] args)
    {
        int sizePic = 7; //задаём размер массива (фигуры)
        String[][] pic = new String[sizePic][sizePic];

        //заполняем массив
        for (int i = 0; i < pic.length; i++)
        {
            for (int j = 0; j < pic[i].length; j++)
            {
                if(i == j || i + j == (sizePic-1)) {
                    pic[i][j] = "X";
                } else {
                    pic[i][j] = " ";
                }
            }
        }

        //рисуем фигуру (выводим массив)
        for (int i = 0; i < pic.length; i++)
        {
            for (int j = 0; j < pic[i].length; j++)
            {
                System.out.print(pic[i][j]);
            }
            System.out.println();
        }
    }
}
