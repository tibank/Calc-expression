package hillel.calc.netcalc;

import hillel.calc.calculator.Calculator;

import java.io.IOException;

public class StartServerCalc implements Runnable {
    private Calculator calc;
    public StartServerCalc(Calculator calc) {
        this.calc = calc;
    }

    @Override
    public void run() {
        ServerCalc serverCalc = new ServerCalc();

        try {
            ServerCalc.startServerCalc(getCalc());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Calculator getCalc() {
        return calc;
    }
}
