package hillel.calc.operations;

public class SignPlus extends Operation {
    final private String value = "+U";
    final private int priority = 4;
    final private int arity = 1;

    @Override
    public double action(double... operands) {
        return operands[0];
    }

    @Override
    public String getValue() {
        return value;
    }

    @Override
    public int getPriority() {
        return priority;
    }

    @Override
    public int getArity() {
        return arity;
    }
}
