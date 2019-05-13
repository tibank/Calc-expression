package hillel.calc.tokens;

/**
 * this class is a min piece of expression(operand, operator, function or brackets)
 * for examle, expression 2 * 3 - 1 will be convert a list of tokens 2,*,3,-,1
 */
public class Token {
    private String value;
    private TypeToken typeToken;

    public Token(String value) {
        this.value = value;
    }

    public Token() {
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public TypeToken getTypeToken() {
        return typeToken;
    }

    public void setTypeToken(TypeToken typeToken) {
        this.typeToken = typeToken;
    }
}
