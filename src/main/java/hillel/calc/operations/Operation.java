package hillel.calc.operations;

import hillel.calc.tokens.Token;
import hillel.calc.tokens.TypeToken;

public class Operation extends Token implements Operate {

    private int priority;
    private int arity;      // арность операции

    public Operation(String value, int priority, int arity) {
        super(value);
        this.priority = priority;
        this.arity = arity;
        this.setTypeToken(TypeToken.OPERATION);
    }

    public Operation() {
    }

    public Operation(String value) {
        super(value);
    }

    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

    public int getArity() {
        return arity;
    }

    public void setArity(int arity) {
        this.arity = arity;
    }

    @Override
    public double action(double... operands) {
        return 0;
    }
}
