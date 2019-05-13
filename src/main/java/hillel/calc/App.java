package hillel.calc;

import hillel.calc.calculator.Calculator;
import hillel.calc.configurator.Configurator;
import hillel.calc.netcalc.StartServerCalc;
import hillel.calc.utils.CalcException;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static hillel.calc.utilcmd.ArgsCmd.getParamsCmd;
import static hillel.calc.utils.ProcessFiles.LoadFromXML;
import static hillel.calc.utils.Service.abortCalc;
import static hillel.calc.utils.Service.showHelp;

public class App {

    public static void main(String[] args) {
        List<String> strExpressions = new ArrayList<>(); // array for expression for calculating
        List<String> strResult = new ArrayList<>(); //  array for storing results of calculating
        String strConfig = "config.xml";
        double result;

        // check existence of params of command line
        if (!(args.length == 0)) {
            Map<String, String> hmParms = getParamsCmd(args);    // список параметров калькулятора (в командной строке)
            // check if there is a param -help, if exists then nothing calculate
            // show on help screen
            if (hmParms.containsKey("help")) {
                showHelp();
                abortCalc("Finished", 0);
            }
            // try to get name of config file
            strConfig = hmParms.getOrDefault("config", "config.xml");
        }

        // create object configurator and load into it
        Configurator conf = new Configurator();
        try {
            conf.loadParams(LoadFromXML(strConfig));
        } catch (Exception e) {
            abortCalc("Error of load params of calculator", 1);
        }

        Calculator calc = null;
        try {
            calc = Calculator.createCalculator(conf);
        } catch (CalcException | IOException e) {
            if (e.getClass() == CalcException.class) {
                System.out.println(((CalcException) e).getDetailMsg() + "\n" + e.getMessage());
            }
            e.printStackTrace();
            System.exit(1);
        }

        System.out.println(calc.calculate("(33+7)*2-(11+7)/3"));

        // Run server-calc in particular thread
        Thread serverCalc = new Thread(new StartServerCalc(calc), "ServerCalc");
        serverCalc.start();
    }
}
