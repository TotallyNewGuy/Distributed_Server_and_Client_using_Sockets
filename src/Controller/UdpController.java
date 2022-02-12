package Controller;

import Result.UdpResult;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.util.Map;

public class UdpController{

    public UdpController(){}

    public UdpResult udpPut(DatagramSocket socket,
                            DatagramPacket clientPacket,
                            Map<String, String> map,
                            String msg) {

        UdpResult res = new UdpResult();

        System.out.println("\nInvoke tcp PUT operation...");
        // Message format should be
        // RequestType key, value
        // Example:
        // PUT name, Charles
        String msgContent = msg.substring(msg.indexOf(" ") + 1);

        if (!msgContent.equals("")) {
            String key = msgContent.substring(0, msgContent.indexOf(","));
            String value = msgContent.substring(msgContent.indexOf(",") + 1);
            System.out.println("key is " + key);
            System.out.println("value is " + value);

            if (!key.equals("")) {
                map.put(key, value);
                res = new UdpResult(true, key, "", "PUT", socket);
            } else {
                String failureMsg = "Received a malformed request of length: " + clientPacket.getLength() + " from: "
                        + clientPacket.getAddress() + " at Port: " + clientPacket.getPort();
                res = new UdpResult(false, key, failureMsg, "PUT", socket);
            }

        } else {
            String failureMsg = "The message content is not present.";
            res = new UdpResult(false, "", failureMsg, "PUT", socket);

        }

        return res;
    }

    public UdpResult udpGet(DatagramSocket socket,
                    DatagramPacket clientPacket,
                    Map<String, String> map,
                    String msg) {
        System.out.println("\nInvoke tcp GET operation...");
        // Message format should be
        // RequestType key
        // Example:
        // GET name
        String msgContent = msg.substring(msg.indexOf(" ") + 1);
        System.out.println("key is " + msgContent);

        UdpResult res = new UdpResult();

        if (!msgContent.equals("")) {
            if (map.containsKey(msgContent)) {
                String retrievedMsg = map.get(msgContent);
                res = new UdpResult(true, msgContent, retrievedMsg, "GET", socket);
            } else {
                String failureMsg = "There is no such key: " + msgContent;
                res = new UdpResult(false, msgContent, failureMsg, "GET", socket);
            }
        } else {
            String failureMsg = "The message content is not present.";
            res = new UdpResult(false, "", failureMsg, "GET", socket);
        }

        return res;
    }

    public UdpResult udpDelete(DatagramSocket socket,
                          DatagramPacket clientPacket,
                          Map<String, String> map,
                          String msg) {

        System.out.println("\nInvoke tcp DELETE operation...");
        // Message format should be
        // RequestType key
        // Example:
        // DELETE name
        String msgContent = msg.substring(msg.indexOf(" ") + 1);
        System.out.println("key is " + msgContent);

        UdpResult res = new UdpResult();

        if (!msgContent.equals("")) {
            if (map.containsKey(msgContent)) {
                String retrievedMsg = map.get(msgContent);
                res = new UdpResult(true, msgContent, "", "DELETE", socket);
            } else {
                String failureMsg = "There is no such key: " + msgContent;
                res = new UdpResult(false, "", failureMsg, "DELETE", socket);
                System.out.println("Operation is failed");
                System.out.println("===========================================");
            }
        } else {
            String failureMsg = "The message content is not present.";
            res = new UdpResult(false, "", failureMsg, "DELETE", socket);
        }

        return res;
    }


}
