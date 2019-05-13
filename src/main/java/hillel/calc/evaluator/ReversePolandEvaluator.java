package hillel.calc.evaluator;

import hillel.calc.operations.Operation;
import hillel.calc.tokens.Token;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ReversePolandEvaluator extends Evaluator {

    /* =================================================================
    convert expression for calculating by PRN
     ==================================================================*/
    private List<Token> reversePolish(List<Token> list, Map<String, Operation> map, Map<String, Method> mapf) {
        ArrayList<Token> listPoland = new ArrayList<>();
        ArrayDeque<Token> stack = new ArrayDeque<>();
        Token lastElemInStack;
        String typeToken;

        for (Token elem : list) {
            typeToken = elem.getClass().getSimpleName();
            if (typeToken.equals("Operand")) {
                listPoland.add(elem);
            } else if (typeToken.equals("Token")) {
                if (elem.getValue().equals("(")) {
                    stack.addLast(elem);
                } else if (elem.getValue().equals(")")) {
                    while (!stack.isEmpty() && !(stack.getLast().getValue().equals("("))) {
                        listPoland.add(stack.removeLast());
                    }
                    stack.removeLast(); // remove open bracket
                    // check may be the function is in stack
                    if (!stack.isEmpty() && mapf.containsKey(stack.getLast().getValue())) {
                        listPoland.add(stack.removeLast()); // remove function from stack
                    }

                    // check, may be exists UNARY OPERATION "+" or "-"
                    if (!stack.isEmpty()) {
                        lastElemInStack = stack.getLast();
                        typeToken = lastElemInStack.getClass().getSimpleName();
                        if (typeToken.equals("SignMinus") || typeToken.equals("SignPlus")) {
                            listPoland.add(stack.removeLast());
                        }
                    }
                }
            } else if (map.containsKey(elem.getValue())) {
                while (!stack.isEmpty()) {
                    lastElemInStack = stack.getLast();
                    if (map.containsKey(lastElemInStack.getValue())) {
                        // это операция
                        if (((Operation)elem).getPriority() <= ((Operation)lastElemInStack).getPriority()) {
                            listPoland.add(stack.removeLast());
                        } else if (mapf.containsKey(lastElemInStack.getValue())) {
                            listPoland.add(stack.removeLast());
                        } else {
                            break;
                        }
                    } else {
                        break;
                    }
                }
                stack.addLast(elem);
            } else if (mapf.containsKey(elem.getValue())) {
                stack.addLast(elem);
            }
        }

        // get rest operation from stack
        while (!stack.isEmpty()) {
            listPoland.add(stack.removeLast());
        }
        return listPoland;
    }


    private Double[] convertToDouble(double[] x) {
        Double[] result = new Double[x.length];
        for (int i = 0; i < x.length; i++) {
            result[i] = Double.valueOf(x[i]);
        }
        return result;
    }

    private double evalFunction(Method method, List<Token> list, int index) throws InvocationTargetException, IllegalAccessException {
        double result = Double.NaN;
        Object[] arguments = null;
        String nameOfTypeParam;

        int countParams = method.getParameterCount();
        Class[] types = method.getParameterTypes();

        if (countParams > 0) {
            arguments = new Object[countParams];
            for (int i = 0; i < countParams; i++) {
                System.out.println(types[i].getSimpleName());
                nameOfTypeParam = types[i].getSimpleName();
                if (nameOfTypeParam.equals("double")) {
                    arguments[i] = Double.valueOf(list.get(index - countParams + i).getValue());
                } if (nameOfTypeParam.equals("int")) {
                    arguments[i] = Integer.valueOf(list.get(index - countParams + i).getValue());
                }
            }
        }

        result = (double) method.invoke(null, arguments);

        return result;
    }

    private double evalFunction2(Method method, List<Token> list, int index) throws InvocationTargetException, IllegalAccessException {
        int countParams=0;
        double result = Double.NaN;

        int count = method.getParameterCount();
        // получаем массив аргументов функции типа double
        double[]  arrDouble = list.subList(index - count, index).stream().mapToDouble(i -> Double.valueOf(i.getValue())).toArray();
        // конвертируем его в Double

        Object[] arguments = count != 0 ? convertToDouble(arrDouble)  : null;
        result = (double) method.invoke(null, arguments);

        return result;
    }
    /* =================================================================
    вычисление выражения
    ==================================================================*/
    private double evalPolish(List<Token> list,  Map<String, Operation> map, Map<String, Method> mapf) throws InvocationTargetException, IllegalAccessException {
        double[] operands;
        Operation oper;
        Method method;   // количество параметров функции
        int index = 0, arity = 0;
        int countParams=0;

        double tempResult;

        while (list.size() > 1) {
            oper = null;
            method = null;
            for (int i = 0; i < list.size(); i++) {
                oper = map.get(list.get(i).getValue());
                if (oper != null) {
                    // это операция, получаем арность операции
                    arity = ((Operation)list.get(i)).getArity();
                    index = i;
                    break;
                } else {
                    method = mapf.get(list.get(i).getValue());
                    if (method != null) {
                        index = i;
                        break;
                    }
                }
            }

            if (oper != null) {
                // получаем массив операндов и преобразовіваем из типа Token  в double
                operands = list.subList(index - arity, index).stream().mapToDouble(i -> Double.parseDouble(i.getValue())).toArray();
                tempResult =  oper.action(operands);
                // помещаем полученное значение от выполнения операции на место крайнего левого операнда
                list.get(index - arity).setValue(Double.toString(tempResult));
                // удаляем остальные операнды
                for (int j = index;  j > index - arity; j--) {
                    list.remove(j);
                }
            }  else if (method != null) {
                // вычисляем найденную функцию
                tempResult = evalFunction(method, list, index);
                // получаем количество параметров функции
                countParams = method.getParameterCount();
                //operands = list.subList(index - countParams, index).stream().mapToDouble(i -> Double.parseDouble(i.getValue())).toArray();
                list.get(index - countParams).setValue(Double.toString(tempResult));
                // удаляем остальные операнды
                for (int i = index;  i > index - countParams; i--) {
                    list.remove(i);
                }
            } else {
                System.out.println("Ошибка - есть операнды, но нет операции!");
                return Double.NaN;
            }
        }
        return Double.parseDouble(list.get(0).getValue());
    }

    @Override
    public double evaluate(List<Token> list, Map<String, Operation> map, Map<String, Method> mapFunc) throws InvocationTargetException, IllegalAccessException {
        List<Token> listPolish = reversePolish(list, map, mapFunc);
        int i = 0;

        System.out.println("PBN");
        for (Token token : listPolish) {
            System.out.println(token.getValue() + "   " + token.getClass().getSimpleName());
        }

        return evalPolish(listPolish, map, mapFunc);
    }
}
