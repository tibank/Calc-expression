package hillel.calc.utils;

import java.util.List;
import java.util.TimerTask;

public class MyTimerTask extends TimerTask {
    private ScannerDirectory scandir;

    public MyTimerTask(ScannerDirectory scandir) {
        this.scandir = scandir;
    }

    @Override
    public void run() {
        System.out.println("Start scanning");
        List<String> list = scandir.checkDir();
        for (int i = 0; i < list.size(); i++) {
            System.out.println(list.get(i));
        }
    }

    public ScannerDirectory getScandir() {
        return scandir;
    }

    public void setScandir(ScannerDirectory scandir) {
        this.scandir = scandir;
    }
}
