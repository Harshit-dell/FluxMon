package Resources;

import RandomObjects.HeaderValueObject;
import RandomObjects.PidValues;
import Terminal.formating;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Resources {
    private  HashMap<Integer,String> Users;
    public Resources(HashMap<Integer, String> Users){
        this.Users=Users;
    }
    private HashMap<Integer,Long> pidCpuUsage=new HashMap<>();

    public   void start() throws Exception {

            //loop implemntation
            long prevCpuUsage=0;
                List<PidValues> pidValues=getPidsValues(prevCpuUsage);
                HeaderValueObject headerValues=new HeaderResources().getHeaderInfo();
                new formating().start(pidValues,headerValues);
    }

    public  boolean isKThread(int pid) throws IOException {
        Path path = Path.of("/proc/" + pid + "/status");
        String content = Files.readString(path);
        for (String line : content.split("\n")) {
            String[] parts = line.split("\\s+");
            if (parts[0].equals("VmRSS:")) {
                return true;
            }
        }
        return false;
        //here optimization is possible but will leave it for later
    }

    public List<PidValues> getPidsValues(long prevCpuUsage){
        List<PidValues> pidsValuesList=new ArrayList<>();
        try{
                Files.list(Path.of("/proc"))
                    .map(path->path.getFileName().toString())
                    .filter(name->name.matches("\\d+"))
                    .map(Integer::parseInt)
                    .sorted()
                    .forEach( pid ->{
                        try{
                           if(isKThread(pid) ){
                               PidValues currentPidValue = new PidInformation().getPidInfo(pid,Users,pidCpuUsage,prevCpuUsage);
                               //pid
                               currentPidValue.setPid(pid);
                               pidsValuesList.add(currentPidValue);
                           }
                        }
                        catch (Exception e){
                            System.out.println(e.getMessage());
                        }
                    });
        }
        catch (Exception e){
            System.out.println(e.getMessage());
        }
        return pidsValuesList;
    }




}
