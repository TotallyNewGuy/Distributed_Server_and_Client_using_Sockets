package Result;

import java.net.Socket;

public class TcpResult {
    public boolean result;
    public String key;
    public String returnMsg;
    public String type;
    public Socket client;

    public TcpResult(){}

    public TcpResult(boolean result,
                     String key,
                     String returnMsg,
                     String type,
                     Socket client){
        this.result = result;
        this.key = key;
        this.returnMsg = returnMsg;
        this.type = type;
        this.client = client;
    }
}