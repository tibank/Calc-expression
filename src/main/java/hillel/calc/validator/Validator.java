package hillel.calc.validator;

import hillel.calc.operations.Operation;
import hillel.calc.tokens.Token;

import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;

public abstract class Validator implements Validate {
    private List<Token> listExp;
    private Map<String,Operation> mapOper;
    private Map<String, Method> mapFunc;

    public List<Token> getListExp() {
        return listExp;
    }

    public void setListExp(List<Token> listExp) {
        this.listExp = listExp;
    }

    public Map<String,Operation> getMapOper() {
        return mapOper;
    }

    public void setMapOper(Map<String,Operation> mapOper) {
        this.mapOper = mapOper;
    }

    public Map<String, Method> getMapFunc() {
        return mapFunc;
    }

    public void setMapFunc(Map<String, Method> mapFunc) {
        this.mapFunc = mapFunc;
    }
}
