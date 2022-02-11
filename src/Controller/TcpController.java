package Controller;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Map;

public class TcpController{

    public TcpController(){}

    public void tcpPut(Socket client, String msg, Map<String, String> map) {
        System.out.println("\nInvoke tcp PUT operation...");
        // Message format should be
        // RequestType key, value
        // Example:
        // PUT name, Charles
        if (!msg.equals("")) {
            String key = msg.substring(0, msg.indexOf(","));
            String value = msg.substring(msg.indexOf(",") + 1);
            System.out.println("key is " + key);
            System.out.println("value is " + value);
            if (!key.equals("")) {
                map.put(key, value);
                sendToClient(client, "PUT", key, "");

            } else {
                System.out.println("Put request is failed");
            }

        } else {
            System.out.println("Put message is null");
        }

        // close client
        try {
            client.close();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public void tcpGet(Socket client, String msg, Map<String, String> map) {
        System.out.println("\nInvoke tcp GET operation...");
        // Message format should be
        // RequestType key
        // Example:
        // GET name
        if (!msg.equals("")) {
            if (map.containsKey(msg)) {
                String sendBack = map.get(msg);
                sendToClient(client, "GET", msg, sendBack);
            } else {
                try {
                    DataOutputStream deleteError = new DataOutputStream(client.getOutputStream());
                    deleteError.writeUTF("No such key (" + msg + "" + "). GET is FAIL");
                } catch (Exception e) {
                    e.printStackTrace();
                }

                System.out.println("No such key");
                System.out.println("===========================================");;
            }

        } else {
            System.out.println("msg is null");
        }

        // close client
        try {
            client.close();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

    public void tcpDelete(Socket client, String msg, Map<String, String> map) {
        System.out.println("\nInvoke tcp DELETE operation...");
        // Message format should be
        // RequestType key
        // Example:
        // DELETE name
        if (!msg.equals("")) {
            if (map.containsKey(msg)) {
                map.remove(msg);
                sendToClient(client, "DELETE", msg, "");
            } else {
                try {
                    DataOutputStream deleteError = new DataOutputStream(client.getOutputStream());
                    deleteError.writeUTF("No such key (" + msg + "" + "). DELETE is FAIL");
                } catch (Exception e) {
                    e.printStackTrace();
                }
                System.out.println("No such key");
                System.out.println("===========================================");;
            }

        } else {
            System.out.println("msg is null");
        }

        // close client
        try {
            client.close();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public void sendToClient(Socket client, String requestType, String key, String returnMsg) {
        System.out.println("Operation is successful");
        System.out.println("Send message back to client...");
        try {
            DataOutputStream outStream = new DataOutputStream(client.getOutputStream());
            if (!returnMsg.equals("") && requestType.equalsIgnoreCase("GET")) {
                outStream.writeUTF("Retrieved value with key (" + key + ") from server: " + returnMsg);
            } else {
                // send client feedback
                outStream.writeUTF(requestType + " with key: " + key + " is SUCCESS");
            }
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        System.out.println("Sending is successful");
        System.out.println("===========================================");
    }

}
