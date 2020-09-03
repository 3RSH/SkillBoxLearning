import static java.lang.Math.*;

public class GeometryCalculator {
    // метод должен использовать абсолютное значение radius
    public static double getCircleSquare(double radius)
    {
        return (PI * (pow(abs(radius), 2)));
    }

    // метод должен использовать абсолютное значение radius
    public static double getSphereVolume(double radius)
    {
        return ((4 * PI * (pow(abs(radius), 3))) / 3);
    }

    public static boolean isTriangleRightAngled(double a, double b, double c)
    {
        return ((a + b) > c) && ((a + c) > b) && ((b + c) > a);
    }

    // перед расчетом площади рекомендуется проверить возможен ли такой треугольник
    // методом isTriangleRightAngled, если невозможен вернуть -1.0
    public static double getTriangleSquare(double a, double b, double c)
    {
        if (!isTriangleRightAngled(a, b, c)) { return -1; }

        double p = (a + b + c) / 2;
        return sqrt(p * (p - a) * (p - b) * (p - c));
    }
}
