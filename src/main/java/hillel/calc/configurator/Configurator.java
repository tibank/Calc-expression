package hillel.calc.configurator;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/*
данный класс хранить информацию о конфигурации калькулятора (какой класс использовать для парсинга,
валидации, расчета, из какого файла подгружать список доступных операций и т.д.)
 */
public class Configurator {
    private String parser;      // имя класса Parser
    private String validator;   // имя класса validator
    private String evaluator;   // имя класса Evaluator (алгоритм расчета (RPN, )
    private String operfile;    // файл со списком доступных операций
    private String direxpression;  // директорий для файлов с выражениями

    public void loadParams(Map<String, String> hmParams) throws Exception {
        // get list of all fields of this class and put it into Set
        Set<String> hsFields = Arrays.stream(getClass().getDeclaredFields()).map(Field::getName).collect(Collectors.toSet());
        // iterate parameter that was got from command line arguments. If param's name matches with some field of this configurator
        // set param value to a this field.
        for (String elem : hsFields) {
            if (hmParams.containsKey(elem.toLowerCase())) {
                setField(this, elem, hmParams.get(elem));
            }
        }
    }

    // ============================================================================
    // универсальная процедура установки значений полей, использующая Reflect API
    // при добавлении новых полей в класс не нужно создавать новые getter и setter
    // ============================================================================
    public void setField(Object obj, String fieldName, Object value) throws Exception {
        Field field = obj.getClass().getDeclaredField(fieldName);
        field.setAccessible(true);
        field.set(obj, value);
    }

    // ============================================================================
    // геттер сделан только для String
    // MUST DO IT other type
    // ============================================================================
    public String getField(String fieldName) throws Exception {
        Field field = getClass().getDeclaredField(fieldName);
        return field.getType().getSimpleName().equals("String") ? field.get(this).toString() : "";
    }
}
