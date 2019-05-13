package hillel.calc.calculator;

import hillel.calc.loader.Loader;
import hillel.calc.operations.Operation;
import hillel.calc.configurator.Configurator;
import hillel.calc.evaluator.Evaluator;
import hillel.calc.parser.Parser;
import hillel.calc.tokens.Token;
import hillel.calc.utils.CalcException;
import hillel.calc.validator.Validator;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;

public class Calculator implements Calculate {
    private Configurator conf;
    private Parser parser;
    private Validator validator;
    private Evaluator evaluator;

    @Override
    public double calculate(String str) {
        double result;

        List<Token> list = (List<Token>) getParser().parsing(str); // parsing string and get list of token
        for (Token token : list) {
            System.out.println(token.getValue() + "   " + token.getClass().getSimpleName());
        }

        getValidator().setListExp(list);    // put list of tokens into validator

        // check can we calculate our expression
        if (getValidator().validate()) {
            List<Token> validList = getValidator().getListExp();
            System.out.println("Valid list");
            for (Token token: validList) {
                System.out.println(token.getValue() + "   " + token.getClass().getSimpleName());
            }

            try {
                result = getEvaluator().evaluate(getValidator().getListExp(), getValidator().getMapOper(), getValidator().getMapFunc());
            } catch (InvocationTargetException|IllegalAccessException e) {
                e.printStackTrace();
                result = Double.NaN;
            }
        } else {
            // expression is not valid, return NaN
            result = Double.NaN;
        }

        return result;
    }

    /**
     * create part of calculator
     * parser, validator, evaluator
     */
    private static <T> T createPartOfCalc(String nameClass, String nameBaseClass) throws CalcException {
        Class aClass;
        T obj = null;

        try {
            aClass = Class.forName("hillel.calc."  + nameBaseClass.toLowerCase() + "." + nameClass + nameBaseClass);
            obj = (T) aClass.getConstructor().newInstance();
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException ex) {
            throw new CalcException(ex.getMessage(), ex, "Error creating instance " + nameClass + " from package " + nameBaseClass.toLowerCase());
        }
        return obj;
    }

    /**
    * create calculate
    */
    //public static calculator createCalculator(Map<String, String> hmParams) throws CalcException,IOException {
    public static Calculator createCalculator(Configurator conf) throws CalcException,IOException {
        Calculator calc = new Calculator();
        calc.setConf(conf);

        /**
         * create parser
         */
        String nameAttr = null;
        try {
            nameAttr = conf.getField("parser");
        } catch (Exception e) {
            throw new CalcException("","There is no param 'parser' in config file");
        }
        calc.setParser(createPartOfCalc(nameAttr, "Parser"));

        /**
         * create loader of operations and load them
         */
        //Map<String, Operation> hmOper = ((loader)createPartOfCalc("Operation", "loader")).load(nameAttr);
        Loader loaderOper = (Loader)createPartOfCalc("Operation", "Loader");
        Map<String, Operation> hmOper = loaderOper.loadOperation();
        Map<String, Method> hmFunc = loaderOper.loadFunction();

        /**
         * create validator
         */
        try {
            nameAttr = conf.getField("validator");
        } catch (Exception e) {
            throw new CalcException("","There is no param validator in config file");
        }

        calc.setValidator(createPartOfCalc(nameAttr, "Validator"));
        calc.getValidator().setMapOper(hmOper);
        calc.getValidator().setMapFunc(hmFunc);

        /**
         * create evaluator
         */
        try {
            nameAttr = conf.getField("evaluator");
        } catch (Exception e) {
            throw new CalcException("","There is no param 'evaluator' in config file");
        }
        calc.setEvaluator(createPartOfCalc(nameAttr, "Evaluator"));

        return calc;
    }

    public Configurator getConf() {
        return conf;
    }

    public void setConf(Configurator conf) {
        this.conf = conf;
    }

    public Parser getParser() {
        return parser;
    }

    public void setParser(Parser parser) {
        this.parser = parser;
    }

    public Validator getValidator() {
        return validator;
    }

    public void setValidator(Validator validator) {
        this.validator = validator;
    }

    public Evaluator getEvaluator() {
        return evaluator;
    }

    public void setEvaluator(Evaluator evaluator) {
        this.evaluator = evaluator;
    }
}
