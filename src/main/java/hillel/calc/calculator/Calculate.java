package hillel.calc.calculator;

import java.lang.reflect.InvocationTargetException;

public interface Calculate {
    double calculate(String str) throws InvocationTargetException, IllegalAccessException;
}
