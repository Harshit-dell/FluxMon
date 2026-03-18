package Terminal;

import RandomObjects.HeaderValueObject;
import RandomObjects.PidValues;
import java.util.List;

public class formating {

    public void start(List<PidValues> processes, HeaderValueObject headerValue) {
        //header value is extracted but not used

        System.out.printf("%5s  %-15.15s  %5sMB  %-20.20s  %-40s\n","PID","User","Memory","Program","Command");
        for (PidValues process : processes) {
            System.out.printf(
                    "%5d  %-15.15s  %5dMB  %-20.20s  %-40.40s%n",
                    process.getPid(),
                    safe(process.getUser()),
                    process.getMemory(),
                    safe(process.getContent()),
                    safe(process.getCmdline())
            );
        }
    }

    private String safe(String value) {
        return value == null ? "" : value;
    }
}