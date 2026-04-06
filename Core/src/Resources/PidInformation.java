package Resources;

import RandomObjects.PidValues;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;

public class PidInformation{

   public PidValues getPidInfo(int pid, HashMap<Integer, String> users,HashMap<Integer,Long> pidCpuUsage,long currCpuUsage,long prevCpuUsage ) {

        PidValues process = new PidValues();

        try {
            process.setPid(pid);
            process.setContent(readComm(pid));
            process.setCmdline(readCmdline(pid));
            readStatus(pid, users, process);
            process.setCpuUsage(getCpu(pidCpuUsage,currCpuUsage,prevCpuUsage,pid));

        } catch (Exception e) {
            System.out.println("Failed to read process " + pid + " : " + e.getMessage());
        }

        return process;
    }

    private void readStatus(int pid, HashMap<Integer, String> users, PidValues process) throws IOException {

        Path statusPath = Path.of("/proc/" + pid + "/status");
        String status = Files.readString(statusPath);

        for (String line : status.split("\n")) {

            String[] parts = line.split("\\s+");

            if (parts[0].equals("Uid:")) {
                process.setUser(users.get(Integer.parseInt(parts[1])));
            }
            else if (parts[0].equals("VmRSS:")) {
                process.setMemory(Integer.parseInt(parts[1]) / 1024);
            }
        }
    }

    private double getCpu(HashMap<Integer, Long> map,
                          long currTotalCpu,
                          long prevTotalCpu,
                          int pid) {
        try {

            long currProc = getProcessCpu(pid);

            if (!map.containsKey(pid)) {
                map.put(pid, currProc);
                return 0;
            }

            long prevProc = map.get(pid);

            long totalDelta = currTotalCpu - prevTotalCpu;
            long procDelta = currProc - prevProc;

            if (totalDelta <= 0 || procDelta < 0) {
                map.put(pid, currProc);
                return 0;
            }

            double cpu = (double) procDelta / totalDelta * 100;

            map.put(pid, currProc);
            return cpu;

        } catch (Exception e) {
            map.remove(pid); // cleanup
            return 0;
        }
    }

    private long getProcessCpu(int pid) throws IOException {
        String stat = Files.readString(Path.of("/proc/" + pid + "/stat"));

        int end = stat.lastIndexOf(")");
        String[] parts = stat.substring(end + 2).split(" ");

        long utime = Long.parseLong(parts[11]);
        long stime = Long.parseLong(parts[12]);

        long total = utime + stime;
        //time spent by proceess + time spent by os for that process
        return total;
    }

    private String readCmdline(int pid) throws IOException {
        String cmd = Files.readString(Path.of("/proc/" + pid + "/cmdline"));
        return cmd.replace('\0', ' ');
    }

    private String readComm(int pid) throws IOException {
        return Files.readString(Path.of("/proc/" + pid + "/comm")).trim();
    }
}