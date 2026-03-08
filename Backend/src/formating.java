public class formating {
    public static  void start(PidValues values){
        System.out.printf("%5d: %-10.10s %5dMB %-5s",values.pid,values.user,values.memory,values.content);
        System.out.println();
    }
}
