package Resources;

import RandomObjects.HeaderValueObject;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;

public class HeaderResources {
    public HeaderValueObject getHeaderInfo() throws Exception {
        HeaderValueObject values=new HeaderValueObject();
        values.setUptime(uptime());
        return values;
    }
    private String uptime() throws  Exception{
        Path path=Path.of("/proc/uptime");
        String uptime=Files.readString(path);
        return uptime;
    }
}
