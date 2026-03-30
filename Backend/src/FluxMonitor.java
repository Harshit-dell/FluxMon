import RandomObjects.terminalLineValue;
import Resources.Resources;
import Terminal.TerminalLines;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.concurrent.atomic.AtomicBoolean;

import static java.util.Locale.filter;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class FluxMonitor {
    public AtomicBoolean running =new AtomicBoolean(true);
    private final HashMap<Integer,String> Users=new HashMap<>();

    private   void  mapUser() throws  IOException{
        Files.readAllLines(Path.of("/etc/passwd")).forEach(uid ->{
            String[] line=uid.split(":");
            int temp=Integer.parseInt(line[3]);
            Users.put(temp,line[0]);
        });
    }
    public static void main(String[] args) throws Exception{
        new FluxMonitor().start();
    }

    public void start() throws Exception {
        TerminalLines cosmetics1=new TerminalLines();
        terminalLineValue TotalLines=cosmetics1.totalTerminalLine();
        mapUser();
        if(!TotalLines.isTerminal){
            return;
        }
        enterAlternateBuffer();
        startInputListner();
    }
    public  void enterAlternateBuffer() throws Exception{
        System.out.print("\033[?1049h");
        
        System.out.print("\033[?25l");
        Runtime.getRuntime()
                .exec(new String[]{"sh","-c","stty -icanon -echo < /dev/tty"})
                .waitFor();
        System.out.flush();
    }
    public   void exitAlternateBuffer() throws IOException, InterruptedException {
        System.out.print("\033[?1049l");
        System.out.print("\033[?25h");
        Runtime.getRuntime().exec(new String[]{"sh","-c","stty sane < /dev/tty"}).waitFor();
        System.out.flush();
    }

    public  void startInputListner() throws Exception {
        Thread listner=new Thread(() ->{
            try{
                // check constantly if user pressed the key . if pressed q that exit
                System.out.print("Press q to exit:");
                while(running.get()){
                    if(System.in.available()>0){
                        //this checking was using too much memory or cpu something
                        //that my laptop fan ran on max speed added sleep to cope
                        var answer=System.in.read();
                        if(answer=='q'){
                            running.set(false);
                        }
                    }
                    Thread.sleep(100);
                }
                //exiting here is risking i think if any bug happens here user
                //stuck in the echo and canonical mode and alter Buffer mode nigga
            }
            catch (Exception e){
                System.out.println(e.getMessage());
            }
            finally {
                try {
                    exitAlternateBuffer();
                } catch (IOException | InterruptedException e) {
                    System.err.println("error in restoring terminal" + e.getLocalizedMessage());
                }
            }

        });
        listner.start();
            Resources resources=new Resources(Users);
            while(running.get()){
                System.out.print("\033[2J\033[H");   // move cursor to top ONLY
                System.out.flush();
                resources.start();
                Thread.sleep(2000);
                //TODO i can directly connect formatting form here to improve project design and add scroll


            }

    }

}