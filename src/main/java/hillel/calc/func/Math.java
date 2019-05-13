package hillel.calc.func;

public class Math {
    final static double EPS = 1.E-10;

    // возвращает целую часть числа
    public static int intDigit(double x) {
        int result;
        String str = Double.toString(x);
        int pos =  str.indexOf(".");

        result = pos == -1 ? (int) x : Integer.parseInt(str.substring(0,pos));

        return result;
    }

    /**
     * Calculate Pi by formula David Bailey, Peter Borwein, Simon Plouffe
     */
    public static double Pi() {
        double result=0;
        double xi;
        int i = 0;
        double i8;
        int i16 = 1;    // 16 degree i

        do {
            i8 = 8 * i;
            xi = (4 / (i8 + 1) - 2 / (i8 + 4) - 1 / (i8 + 5) - 1 / (i8 + 6)) / i16;
            i++;
            i16 *= 16;
            result += xi;
        } while (xi > EPS);

        return result;
    }

    /**
     * Calculate module of number
     */
    public static double Abs(double x) {
        double result = x;
        if (x < 0) {
            result = -x;
        }
        return result;
    }

    // выисление целых степеней
    public static double powInt(double x, int n) {
        double result = 1;
        for (int i = 1; i <= n; i++) {
            result = result * x;
        }

        return result;
    }

    /**
     * Correct big argument for trigonometric function
     * if absolute value х greater then 2*Pi, reduce them to range 0-2*Pi
     */
    private static double correctArgument(double x) {
        double pi2 = 2 * Pi();
        return Abs(x) < pi2 ? x : x - intDigit(x / pi2) * pi2;
    }

    /**
     * Calculate cos
     */
    public static double Cos(double x) {
        double xr = correctArgument(x); // correct big argument

        double result = 1;
        double xi = 1;
        double x2 = xr * xr;   // for increase speed of calculating

        int i = 0;
        int sign = -1;

        while (Abs(xi) > EPS) {
            xi *= x2 / (++i) / (++i);
            result = result + sign * xi;
            sign = -sign;
        }

        return result;
    }

    /**
     * Calculate sin
     */
    public static double Sin(double x) {
        double xr = correctArgument(x); // корректируем большой аргумент

        double result = xr;
        double xi = xr;
        double x2 = xr * xr;   // для увеличения скорости расчета (уменьшаем на одну операцию умножения

        int i = 1;
        int sign = -1;

        while (Abs(xi) > EPS) {
            xi *= x2 / (++i) / (++i);
            result = result + sign * xi;
            sign = -sign;
        }
        return result;
    }

    /**
     * Calculate square root
     */
    public static double mySqrt(double x) {
        double result = 1;
        double xi;

        while (true) {
            xi = (result + x / result) / 2;
            if (Abs(result - xi) < EPS) break; //точность достигнута
            result = xi;
        }

        return result;
    }
}
