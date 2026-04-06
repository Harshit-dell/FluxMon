package Resources;

import RandomObjects.HeaderValueObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Path;

public class HeaderResources {
    public HeaderValueObject getHeaderInfo() throws Exception {
        HeaderValueObject values=new HeaderValueObject();
        values.setUptime(uptime());
        values.setCpu(totalCpu());

        return values;
    }
    private String uptime() throws  Exception{
        Path path=Path.of("/proc/uptime");
        String uptime=Files.readString(path);
        return uptime;
    }
    public  long totalCpu() throws Exception{
        BufferedReader reader=new BufferedReader(new InputStreamReader(new FileInputStream("/proc/stat")));
        String line =reader.readLine();
        String[] firstline=line.split("\\s+");
        int total=0;
        for(int i=1;i<firstline.length;i++){
            total+=Integer.parseInt(firstline[i]);
        }
        return total;
    }
}
