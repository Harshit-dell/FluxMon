package Terminal;

import RandomObjects.terminalLineValue;

import java.io.BufferedReader;
import java.io.InputStreamReader;

public class TerminalLines {
    public TerminalLines(){
        totalTerminalLine();
    }
    public terminalLineValue totalTerminalLine() {

        try {
            Process process = new ProcessBuilder("bash", "-c", "stty size < /dev/tty").start();
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
                String line = reader.readLine();
                process.waitFor();
                if (line != null) {
                    String[] value = line.split(" ");
                    int length = Integer.parseInt(value[0]);
                    return new terminalLineValue(true, length);
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
        return null;
    }
}