import java.nio.file.Files;
import java.nio.file.Path;

public class Resources {
    public static void getStreamValue(int totalLine){
        try{
            Files.list(Path.of("/proc"))
                    .map(path->path.getFileName().toString())
                    .filter(name->name.matches("\\d+"))
                    .map(Integer::parseInt)
                    .limit(totalLine)
                    .sorted()
                    .forEach( pid ->{
                        try{
                            Path path=Path.of("/proc/" + pid + "/comm");
                            String content=Files.readString(path).trim();
                            System.out.print(pid+"->"+content+"--->");
                            Resources.getMemoryMap(pid);
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
    public static void getMemoryMap(Integer pid) {
        Path path = Path.of("/proc/" + pid + "/status");
        try {
            String content = Files.readString(path);
            for (String line : content.split("\n")) {
                String[] parts = line.split("\\s+");
                if (parts[0].equals("VmRSS:")) {
                    System.out.println(parts[1] + " kB");
                    return;
                }
            }
            System.out.println("Kernal Thread");

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

}
