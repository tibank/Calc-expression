package hillel.calc.loader;

import hillel.calc.operations.*;
import hillel.calc.utils.CalcException;
import hillel.calc.operations.Operation;
import org.reflections.Reflections;
import org.reflections.scanners.SubTypesScanner;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;

/**
 * This class disigned for loading operation from package 'operations'
 */
public class OperationLoader extends Loader {
    private HashMap<String, Operation> hmOpers;

    /**
     * Load list of allowable operations from package 'operations'
     */
    private boolean loadFromClass(Map<String, Operation> hMap, String nameOper, String codeOper, int priority, int arity) {
        boolean result = true;

        Class aClass;
        Class[] params = {String.class, int.class, int.class};

        try {
            aClass = Class.forName("hillel.calc.operations." + nameOper);
            hMap.put(codeOper, (Operation) aClass.getConstructor(params).newInstance(codeOper, priority, arity));
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            e.printStackTrace();
            result = false;
        }

        return result;
    }

    /**
     * Load list of allowable operations from file and create instances of operation
     */
    @Override
    public HashMap<String, Operation> load(String fileName) throws IOException, CalcException {
        HashMap<String, Operation> hMap = new HashMap<>();
        List<String> lines;
        String[] parseLine; // Contains elements of string from file operations: class name, code operation, priority, arity

        String nameOper, codeOper;
        int priority;
        int arity;

        // check existance of file with operations
        File file = new File(fileName); // getFileName()
        if (!file.exists()) {
            // file is absent, Error
            throw new CalcException("", "Is absent file with list of operation " + fileName);
        } else {
            // get array of string (one string - one operation)
            lines = Files.lines(Paths.get(fileName)).collect(Collectors.toList());

            for (String line : lines) {
                // parsing string and get class name, code operation, priority, arity
                // check size of array - must be at least 4
                parseLine = line.split(";");
                if (parseLine.length < 4) {
                    // miss out this string it's not correct
                    continue;
                }

                // try to load operation
                nameOper = parseLine[0];
                codeOper = parseLine[1];
                priority = Integer.parseInt(parseLine[2]);
                arity = Integer.parseInt(parseLine[3]);
                if (!loadFromClass(hMap, nameOper, codeOper, priority, arity)) {
                    System.out.println("Error load operation called " + nameOper);
                }
            }
        }

        return hMap;
    }

    /**
     * Load list of allowable operations from package 'operations'
     */
    @Override
    public Map<String, Operation> loadOperation() throws IOException, CalcException {
        Map<String, Operation> hMap = new HashMap<>();
        Operation oper = null;

        Reflections reflections = new Reflections("hillel.calc.operations", new SubTypesScanner());

        for (String  className  : reflections.getStore().get(SubTypesScanner.class.getSimpleName()).values()) {
            try {
                Class aClass = Class.forName(className);
                if (aClass.getSuperclass() == Operation.class) {
                    try {
                        oper = (Operation) aClass.getConstructor().newInstance();
                    } catch (InstantiationException|IllegalAccessException|InvocationTargetException|NoSuchMethodException e) {
                        e.printStackTrace();
                    }
                    hMap.put(oper.getValue(), oper);
                }
            } catch (ClassNotFoundException e) {
                throw new RuntimeException("Этого не может быть:)", e);
            }
        }
        return hMap;
    }

    /**
     * Load list of allowable functions from package 'func'
     */
    @Override
    public Map<String, Method> loadFunction() throws IOException, CalcException {
        Map<String, Method> hfMap = new HashMap<>();
        Set<String> hashSetSuperClass = new HashSet<>();  // list of function superclass

        /**
        * list of function superclass for excluding them from list of allowable functions
        */
        try {
            Class aClass = Class.forName("hillel.calc.func.Math");
            Class sClass = aClass.getSuperclass();
            Method[] funcsSuperClass = sClass.getMethods();
            hashSetSuperClass = Arrays.stream(funcsSuperClass).map(Method::getName).collect(Collectors.toSet());

            // get methods of class Math
            Method[] funcs = aClass.getMethods();
            for (Method method : funcs) {
                if (!hashSetSuperClass.contains(method.getName())) {
                    hfMap.put(method.getName(), method);
                }
            }
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("that can't be true :)", e);
        }
        return hfMap;
    }
}
