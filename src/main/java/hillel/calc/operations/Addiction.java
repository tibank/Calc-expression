package hillel.calc.operations;

public class Addiction extends Operation {
    final private String value = "+";
    final private int priority = 2;
    final private int arity = 2;

    @Override
    public double action(double... operands) {
        return operands[0] + operands[1];
    }

    @Override
    public String getValue() {
        return value;
    }

    public int getPriority() {
        return priority;
    }

    public int getArity() {
        return arity;
    }

}
