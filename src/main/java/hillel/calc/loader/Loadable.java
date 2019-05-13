package hillel.calc.loader;

import hillel.calc.operations.Operation;
import hillel.calc.utils.CalcException;

import java.io.IOException;
import java.lang.reflect.Method;
import java.util.Map;

public interface Loadable {
    Map<String, Operation> load(String file) throws IOException, CalcException;
    Map<String, Operation> loadOperation() throws IOException, CalcException;
    Map<String, Method> loadFunction() throws IOException, CalcException;
}
