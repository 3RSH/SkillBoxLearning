public class Loader
{
    public static final int PATIENTS_COUNT = 30;

    public static final float MIN_TEMP = 32.0f;
    public static final float MAX_TEMP = 40.0f;

    public static final float MIN_GOOD_TEMP = 36.2f;
    public static final float MAX_GOOD_TEMP = 36.9f;

    public static void main(String[] args)
    {
        float[] indicators = new float[PATIENTS_COUNT];

        for (int i = 0; i < indicators.length; i++)
        {
            indicators[i] = MIN_TEMP + (MAX_TEMP - MIN_TEMP) * (float) Math.random();
        }

        getStatistic(indicators);
    }

    public static void getStatistic(float[] array)
    {
        float sum = 0;
        float average = 0;
        //степень округления результата для вывода float (1 знак после '.')
        float scale = (float) Math.pow(10, 1);
        int goodCount = 0;

        System.out.print("Температуры пациентов: ");
        for (float f : array)
        {
            if (f > MIN_GOOD_TEMP && f < MAX_GOOD_TEMP) {
                goodCount++;
            }
            sum += f;
            f = Math.round(f * scale) / scale; //округляем float перед выводом
            System.out.print(f + " ");
        }

        System.out.print("\nСредняя температура: ");
        scale = (float) Math.pow(10, 2); //меняем степень округления (2 знака после '.')
        average = Math.round(sum / array.length * scale) / scale; //округляем float перед выводом
        System.out.println(average);

        System.out.println("Количество здоровых: " + goodCount);
    }
}
