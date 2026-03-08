public class formating {
    public static  void start(PidValues values){
        System.out.printf("%5d: %5s %5dMB %5s",values.pid,values.user,values.memory,values.content);
        System.out.println();
    }
}
