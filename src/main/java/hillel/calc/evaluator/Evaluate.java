package hillel.calc.evaluator;

import hillel.calc.operations.Operation;
import hillel.calc.tokens.Token;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;

public interface Evaluate {
    double evaluate(List<Token> list, Map<String, Operation> map, Map<String, Method> mapf) throws InvocationTargetException, IllegalAccessException;
}
