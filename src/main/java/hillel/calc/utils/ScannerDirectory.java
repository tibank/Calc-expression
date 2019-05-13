package hillel.calc.utils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class ScannerDirectory {
    private String scanDir;
    long period; // period to scan in seconds

    public ScannerDirectory(String scanDir, long period) {
        this.scanDir = scanDir;
        this.period = period;
    }

    public List<String> checkDir() {
        List<String> files = new ArrayList<>();
        File folder = new File(getScanDir());

        if(folder.isDirectory())
        {
            for(File item : folder.listFiles()){
                if(item.isFile()){
                    files.add(item.getName());
                }
            }
        }

        return files;
    }

    public String getScanDir() {
        return scanDir;
    }

    public long getPeriod() {
        return period;
    }
}
