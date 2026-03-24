package Resources;

import RandomObjects.PidValues;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;

public class PidInformation{

    public PidValues getPidInfo(int pid, HashMap<Integer, String> users,HashMap<Integer,Long> pidCpuUsage,long prevCpuUsage ) {

        PidValues process = new PidValues();

        try {
            process.setPid(pid);
            process.setContent(readComm(pid));
            process.setCmdline(readCmdline(pid));
            readStatus(pid, users, process);
            process.setCpuUsage(getCpu(pidCpuUsage,prevCpuUsage,pid));

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

    private long getCpu(HashMap<Integer,Long> map,long prevTotalCpu,int pid){
        try{
            if(map.containsKey(pid)){
                map.put(pid,getProcessCpu(pid));
                prevTotalCpu=new HeaderResources().totalCpu();
            }
            else{
                long currTotalCpu=new HeaderResources().totalCpu();
                long prec_proc=map.get(pid);
                long cur_porcc=getProcessCpu(pid);
                long cpu=(cur_porcc-prec_proc)/(currTotalCpu-prevTotalCpu)*100;
                prevTotalCpu=currTotalCpu;
                map.put(pid,cur_porcc);
            return cpu;
            }
        }
        catch(Exception e){
            System.out.println("error in the cpuUsage"+e.getMessage());
        }
        return 0;
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