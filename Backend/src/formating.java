import java.util.List;

public class formating {
    public void start(List<PidValues> pidsValuesList){
        for(PidValues values:pidsValuesList){
            System.out.printf("%5d: %-10.10s %5dMB %-5s",values.pid,values.user,values.memory,values.content);
            System.out.println();
        }
    }
}
