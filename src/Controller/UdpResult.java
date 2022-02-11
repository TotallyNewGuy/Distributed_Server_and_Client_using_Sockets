package Controller;

import java.net.DatagramSocket;
import java.net.Socket;

public class UdpResult {
    public boolean result;
    public String key;
    public String returnMsg;
    public String type;
    public DatagramSocket client;

    public UdpResult(){}

    public UdpResult(boolean result,
                  String key,
                  String returnMsg,
                  String type,
                 DatagramSocket client){
        this.result = result;
        this.key = key;
        this.returnMsg = returnMsg;
        this.type = type;
        this.client = client;
    }
}