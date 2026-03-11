package Resources;

import RandomObjects.PidValues;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;

public class PidInformation {
    public PidInformation() {

    }

    public PidValues getValueMap(Integer pid, HashMap<Integer,String> Users) throws Exception {
        PidValues currentpidValue=new PidValues();
        Path contentPath=Path.of("/proc/" + pid + "/comm");
        String content= Files.readString(contentPath).trim();
        //content
        currentpidValue.setContent(content);


        Path StatusPath = Path.of("/proc/" + pid + "/status");
        try {
            String statusLine = Files.readString(StatusPath);
            for (String line : statusLine.split("\n")) {
                String[] parts = line.split("\\s+");

                if(parts[0].equals("Uid:")){
                    //user
                    //TODO here Users hashmap is not giving value after mapping all user -Done
                    currentpidValue.setUser(Users.get(Integer.parseInt(parts[1])));
                }
                else if (parts[0].equals("VmRSS:")){
                    //memory
                    currentpidValue.setMemory((Integer.parseInt(parts[1]) / 1024));
                }


            }
            return currentpidValue;


        } catch (Exception e) {
            System.out.println(e.getLocalizedMessage() + "    value extraction miss alignment");
        }
        return new PidValues() ;
    }
}
