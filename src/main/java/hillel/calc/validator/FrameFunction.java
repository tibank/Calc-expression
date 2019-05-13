package hillel.calc.validator;

import java.lang.reflect.Method;
import java.util.List;

public class FrameFunction {
    private Method method;
    private String name;
    private int countParams;
    private List<String> listParams;

    public FrameFunction(Method method, String name, int countParams, List<String> listParams) {
        this.method = method;
        this.name = name;
        this.countParams = countParams;
        this.listParams = listParams;
    }

    public String getName() {
        return name;
    }

    public int getCountParams() {
        return countParams;
    }

    public List<String> getListParams() {
        return listParams;
    }
}
