import java.io.*;
import java.nio.Buffer;
import java.nio.file.*;
import java.time.format.TextStyle;
import java.util.*;

import static java.util.Locale.filter;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    public static void main(String[] args) throws Exception{
        terminalLineValue TotalLines=totalTerminalLine();
        if(!TotalLines.isTerminal){
            return;
        }
        clearTreminalScreen();
        getStreamValue(TotalLines.lines);
    }
    public static void getStreamValue(int totalLine){
        try{
            Files.list(Path.of("/proc"))
                    .map(path->path.getFileName().toString())
                    .filter(name->name.matches("\\d+"))
                    .map(Integer::parseInt)
                    .sorted()
                    .forEach( pid ->{
                        try{
                            Path path=Path.of("/proc/" + pid + "/comm");
                            String content=Files.readString(path).trim();
                            System.out.print(pid+"->"+content+"--->");
                            getMemoryMap(pid);
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

    public static  void clearTreminalScreen() throws InterruptedException, IOException {
    ProcessBuilder builder=new ProcessBuilder("bash","-c","clear");
    Process p=builder.start();
    p.waitFor();
    }
    public static terminalLineValue totalTerminalLine() {
        try {
            Process process = new ProcessBuilder("bash","-c","stty size < /dev/tty").start();
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()))){
                String line = reader.readLine();
                process.waitFor();
                if (line != null) {
                    String [] value=line.split(" ");
                    int length=Integer.parseInt(value[0]);
                    return new terminalLineValue(true,length);
                } else {
                    System.out.println(
                            "\033[1;37;41m" +
                                    "  NO TERMINAL DETECTED  \n" +
                                    "  AVOID IDE TERMINAL    " +
                                    "\033[0m"
                    );
                    return new terminalLineValue(false);
                }
            }

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return  null;
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