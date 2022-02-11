package Controller;

import java.net.Socket;

public class Result {
    public boolean result;
    public String key;
    public String returnMsg;
    public String type;
    public Socket client;

    public Result(){}

    public Result(boolean result,
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
