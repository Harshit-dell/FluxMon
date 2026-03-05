import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class Resources {
    public static void getStreamValue(int totalLine){
        try{
            final int[] count={totalLine};
            Files.list(Path.of("/proc"))
                    .map(path->path.getFileName().toString())
                    .filter(name->name.matches("\\d+"))
                    .map(Integer::parseInt)
//                    .limit(totalLine)
                    .sorted()
                    .forEach( pid ->{
                        if(count[0]<=0){
                            return;
                        }
                        try{
                           if(isKThread(pid) ){
                               System.out.print("  "+pid+":  ");
                               Resources.getMemoryMap(pid);
                               Path path=Path.of("/proc/" + pid + "/comm");
                               String content=Files.readString(path).trim();
                               System.out.println(content);
                               count[0]--;
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
    }
    public static boolean isKThread(int pid) throws IOException {
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
    public static void getMemoryMap(Integer pid) {
        Path path = Path.of("/proc/" + pid + "/status");
        try {
            String content = Files.readString(path);
            for (String line : content.split("\n")) {
                String[] parts = line.split("\\s+");
                if (parts[0].equals("VmRSS:")) {
                    int memoryInGb=(Integer.parseInt(parts[1])/1024) ;
                    System.out.print(memoryInGb + " MB ");
                    return;
                }
            }
            System.out.print("KThread   ");

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

}
