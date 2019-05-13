package hillel.calc.validator;

import hillel.calc.tokens.Token;

import java.lang.reflect.Method;
import java.util.*;
import java.util.stream.Collectors;

public class CheckerFunctions {
    private final String nameClassMath; // полный путь к классу Math с функциями
    private final Class clazz;                // сам класс Math
    /*
    список функций со структурой из рефлексии, нужен для того,
    чтобы повторно не обращаться к ReflectAPI для одной и той же функции
    */
    Map<String, FrameFunction> mapFrame;

    /*===============================================================
    // создаем checker функций и заполняем Map информацией о функциях
    ===============================================================*/
    public CheckerFunctions(String nameClassMath) throws ClassNotFoundException {
        Class[] paramTypes;

        this.nameClassMath = nameClassMath;
        mapFrame = new HashMap<>();

        // получаем список всех функция класса
        clazz = Class.forName(nameClassMath);
        Method[] methods = clazz.getDeclaredMethods();
        // сохраняем структуру функций (имя, кол-во параметров, тип параметров)
        for (Method method : methods) {
            paramTypes = method.getParameterTypes();
            mapFrame.put(method.getName(),
                    new FrameFunction(method, method.getName(), method.getParameterCount(),
                            Arrays.stream(paramTypes).map(Class::getSimpleName).collect(Collectors.toList())));

        }
    }

    /* ==========================================
    check counts of parameters
     ========================================== */
    private boolean checkCountOfParams(String name, List<Token> list) {
        boolean result = false;

        int countParams = getCountParams(name);
        List<String> params = getListOfParams(name);
        if (countParams >= 0) {
            if (countParams == list.size()) {
                result = true;
            } else {
                // check, may be last param of function is VarArgs
                if (countParams > 0 && params.get(params.size() - 1).indexOf("[]") > -1) {
                    // one of the param is array
                    result = true;
                }
            }
        }

        return result;
    }

    /* ==========================================
    check type of parameters. Try catch an error
    when type param is one of integer and calling
    function with float or double
     ========================================== */
    private boolean checkTypeOfParams(String name, List<Token> list) {
        boolean result = true;
        String param = null;

        Deque<String> params = new ArrayDeque<>(getListOfParams(name));
        // check every param type with pass throw value
        for (Token token : list) {
            if (params.size() > 0) {
                param = params.pollFirst();
            }

            // check operand if param type one of the integer
            if (param.toLowerCase().startsWith("int") || param.toLowerCase().startsWith("long")) {
                // if there is point or symbol F it means double or float
                if (token.getValue().indexOf(".") > -1 || token.getValue().trim().toLowerCase().endsWith("f")) {
                    result = false;
                    break;
                }
            }
        }

        return result;
    }

    /* ==========================================
    check function
    ========================================== */
    public boolean checkFunction(String name, List<Token> list) throws NoSuchMethodException {
        boolean result = true;
        if (mapFrame.get(name) == null) {
            // throw exception
            throw new NoSuchMethodException("No found method: " + name);
        }

        // check counts of parameters
        if (!checkCountOfParams(name, list)) {
            result = false;
        }

        // check counts of parameters
        if (getCountParams(name) != 0) {
            if (!checkTypeOfParams(name, list)) {
                result = false;
            }
        }

        return result;
    }

    /*======================================
    count of parameters certain function
    ======================================*/
    private int getCountParams(String nameFunc) {
        int result = -1;
        FrameFunction frame = mapFrame.get(nameFunc);
        if (frame != null) {
            result = frame.getCountParams();
        }
        return result;
    }

    /*======================================
    get list of parameters certain function
    ======================================*/
    private List<String> getListOfParams(String nameFunc) {
        List<String> result = new ArrayList<>();
        FrameFunction frame = mapFrame.get(nameFunc);
        if (frame != null) {
            result = frame.getListParams();
        }
        return result;
    }

}
