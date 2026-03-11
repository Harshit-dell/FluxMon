package Resources;

import RandomObjects.PidValues;
import Terminal.formating;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Resources {
    private final  HashMap<Integer,String> Users=new HashMap<>();
    public   void start() throws Exception {
        mapUser();
         new formating().start(getPidsValues());

    }
    public  void  mapUser() throws  IOException{
            Files.readAllLines(Path.of("/etc/passwd")).forEach(uid ->{
                String[] line=uid.split(":");
                int temp=Integer.parseInt(line[3]);
                   Users.put(temp,line[0]);
            });
    }
    public List<PidValues> getPidsValues(){
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
                               PidValues currentPidValue = new PidInformation().getValueMap(pid,Users);
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


}
