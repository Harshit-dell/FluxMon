package Terminal;

import RandomObjects.HeaderValueObject;
import RandomObjects.PidValues;

import java.util.List;

public class formating {

    // ANSI colors
    private static final String RESET = "\033[0m";
    private static final String BOLD = "\033[1m";
    private static final String RED = "\033[31m";
    private static final String YELLOW = "\033[33m";
    private static final String GREEN = "\033[32m";

    private boolean headerPrinted = false;

    public void start(List<PidValues> processes, HeaderValueObject headerValue) {

        if (!headerPrinted) {
            printHeader();
            headerPrinted = true;
        }

        int line = 3; // start printing processes from line 3
        for (PidValues process : processes) {
            // Move cursor to the line
            System.out.printf("\033[%d;1H", line);

            // Print process info
            System.out.printf(
                    "%6d %8s %-15.15s %7s %-20.20s %-40.40s%n",
                    process.getPid(),
                    colorCpu(process.getCpuUsage()),
                    safe(process.getUser()),
                    formatMem(process.getMemory()),
                    safe(process.getContent()),
                    safe(process.getCmdline())
            );
            line++;
        }
    }

    private void printHeader() {
        System.out.print("\033[H"); // move cursor to top-left
        System.out.print("\033[2J"); // clear screen

        // Print header in yellow bold
        System.out.printf("\033[33m\033[1m%6s %8s %-15s %7s %-20s %-40s\033[0m\n",
                "PID", "CPU%", "USER", "MEM", "PROGRAM", "COMMAND");

        // separator
        System.out.println("----------------------------------------------------------------------" +
                "----------------------------------------");
    }

    private String colorCpu(double cpu) {
        String value = formatCpu(cpu);
        if (cpu > 50) return RED + value + RESET;
        if (cpu > 20) return YELLOW + value + RESET;
        return GREEN + value + RESET;
    }

    private String formatCpu(double cpu) {
        if (cpu > 0 && cpu < 0.001) return "<0.001";
        return String.format("%6.2f", cpu);
    }

    private String formatMem(int mem) {
        return String.format("%5dM", mem);
    }

    private String safe(String value) {
        return value == null ? "" : value;
    }
}