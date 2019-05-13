package hillel.calc.netcalc;

import hillel.calc.calculator.Calculator;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class ServerCalc {

    public static final int PORT = 8085;

    public static  void startServerCalc(Calculator calc) throws IOException{
        ServerSocket serverSocket = new ServerSocket(PORT);
        System.out.println("Started: " + serverSocket);
        while (true) {
            Socket socket = null;
            while (socket == null) {
                socket = serverSocket.accept();
            }
            new SocketCalc(socket, calc);
        }
    }
}
