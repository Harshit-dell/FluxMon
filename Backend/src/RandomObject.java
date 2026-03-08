class terminalLineValue{
    boolean isTerminal;
    int lines;
    terminalLineValue(boolean isTerminal,int lines){
        this.isTerminal=isTerminal;
        this.lines=lines;
    }
    terminalLineValue(boolean isTerminal){
        this.isTerminal=isTerminal;
    }
}
 class PidValues{
    int memory;
    String user;
    String content;
    int pid;
    PidValues(){

    }
    PidValues(int memory,String user){
        this.memory=memory;
        this.user=user;
    }
}