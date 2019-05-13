package hillel.calc.utils;

import hillel.calc.utilcmd.ArgsCmd;

import java.util.HashMap;
import java.util.Map;

/**
 * Get map of param from command line.
 *
 */
public class Service {

    public static void showHelp() {
        System.out.println("HELP!!!!!!!!!!!");

    }

    public static void abortCalc(String str, int i) {
        System.out.println(str);
        System.exit(1);
    }

}
