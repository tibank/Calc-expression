package hillel.calc.netcalc;

import hillel.calc.calculator.Calculator;

import java.io.*;
import java.net.Socket;

public class SocketCalc extends Thread {
    private Socket socket;
    private Calculator calc;

    public SocketCalc(Socket socket, Calculator calc) {
        this.socket = socket;
        this.calc = calc;
        this.start();
    }

    /**
     * remove invisible symbol and correctly process press key 'Backspace'
     */
    private String cleanStringExpression(String str) {
        StringBuilder stringBuilder = new StringBuilder();
        int code;
        int index = 0;

        for (int i = 0; i < str.length(); i++) {
            code = Character.codePointAt(str, i);
            if (code < 32 || code == 127) {
                // it's invisible symbol - skip only check Backspase
                System.out.println(code);
                if (code == 27) {
                    //skip two symbol special symbols after code='27'
                    i += 2;
                } else if (code == 8) {
                    index--;
                    stringBuilder.deleteCharAt(index);
                }
            } else {
                stringBuilder.append(str.charAt(i));
                index++;
            }
        }

        return stringBuilder.toString();
    }

    @Override
    public void run() {

        double result=0;
        try (BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
             PrintWriter out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(socket.getOutputStream())), true)) {

            while (true) {
                out.println("Input expression: ");
                String str = in.readLine();
                if (str.equals("END")) {
                    break;
                }

                str = cleanStringExpression(str);
                result = calc.calculate(str);
                out.println("Answer: " + str + " = " + result);
            }
        } catch (IOException e) {
            System.out.println("Fail creating buffer");
            e.printStackTrace();
        }
        try {
            this.socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
