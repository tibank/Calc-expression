package hillel.calc.utilcmd;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.HashMap;
import java.util.Map;

import static hillel.calc.utils.Service.abortCalc;

/*
класс предназначени для обработки параметров командной строки и формировании на основе их
объекта configurator
все параметры в командной строке идут парами ключ (-key) -> значение(value)
если после ключевого параметра идет другой ключевой параметр, значит первый ключевой параметр
получает значение по умолчанию "1", обозначающее true
если после не ключевого параметра идет другой не ключевой параметр, это ошибка и процедура
parsingCmd возвращает false, иначе true
 */
public class ArgsCmd {
    private String[] strCmd;    //array args
    private HashMap<String, String> params; // table with key->value for class configurator

    public ArgsCmd(String[] strCmd) {
        this.strCmd = strCmd;
    }

    public Map<String, String> getParams() {
        return params;
    }

    public void setParams(HashMap<String, String> params) {
        this.params = params;
    }

    public boolean parsing() throws Exception {
        boolean result = true;
        HashMap<String, String> hm = new HashMap<>();
        Deque<String> stack = new ArrayDeque<>();   // предназначени для внесения ключа
        String key;

        for (int i = 0; i < strCmd.length; i++) {
            // проверяем стек
            if (!stack.isEmpty()) {
                key = stack.pollFirst();
                if (key == null) {
                    System.out.println("Неожиданная ошибка получения из стека ключа при обработке параметров cmd");
                    result = false;
                    break;
                }
            } else {
                key = "";
            }

            if (strCmd[i].startsWith("-")) {
                // это ключевой параметр
                if (!key.isEmpty()) {
                    // в стеке ключевой параметр без значения, добавляем в Map  со значением "1"
                    // исключение, если в стеке параметр exp, за ним следует выражение, которое может начинаться
                    // со знака '-'
                    if (key.equals("exp")) {
                        hm.put(key, strCmd[i]);
                        continue;
                    } else {
                        hm.put(key, String.valueOf(1));
                    }
                }
                stack.addFirst(strCmd[i].substring(1)); // пропускаем первый символ "-" признака ключевого параметра
            } else {
                // это параметр значение, проверим стэк, есть ли для него ключ
                if (!key.isEmpty()) {
                    // в стеке ключевой параметр, добавляем в Map
                    hm.put(key, strCmd[i]);
                } else {
                    // получили параметр "значение" без ключа - это ошибка
                    result = false;
                }
            }
        }

        if (result) {
            setParams(hm);
        }

        return result;
    }

    /**
     * Get map of param from command line.
     *
     * @param  args the array of params of command line
     * @return Hashmap, where key is name of param and value is a value of it
     * @throws ClassCastException if the type of the specified element
     *         is incompatible with elements of this collection
     */
    public static Map<String, String>  getParamsCmd(String[] args) {
        Map<String, String> hashMap = new HashMap<>();
        ArgsCmd paramCmd = new ArgsCmd(args);   // обработка параметров командной строки
        try {
            if (!paramCmd.parsing()) {
                abortCalc("Ошибка обработки параметров командной строки", 1);
            } else {
                hashMap = paramCmd.getParams();
            }
        } catch (Exception e) {
            abortCalc(e.getMessage(), 1);
        }
        return hashMap;
    }
}
