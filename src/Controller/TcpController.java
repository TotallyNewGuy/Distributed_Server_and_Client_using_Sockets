package Controller;

import Result.TcpResult;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Map;
import java.util.logging.Logger;

public class TcpController{

    public static Logger logger = Logger.getLogger("Tcp Controller");
    public TcpController(){}


    public TcpResult tcpPut(Socket client, String msg, Map<String, String> map) {
        logger.config("Invoke tcp PUT operation...");
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
                return new TcpResult(true, msg, "", "PUT", client);
            } else {
                System.out.println("Put request is failed");
            }
        } else {
            logger.warning("Put message is null");
            System.out.println("Put message is null");
        }

        // close client
        try {
            client.close();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return new TcpResult(false, "N/A", "N/A", "FAIL", client);
    }


    public TcpResult tcpGet(Socket client, String msg, Map<String, String> map) {
        logger.config("Invoke tcp PUT operation...");
        System.out.println("\nInvoke tcp GET operation...");
        // Message format should be
        // RequestType key
        // Example:
        // GET name
        if (!msg.equals("")) {
            if (map.containsKey(msg)) {
                String sendBack = map.get(msg);
                return new TcpResult(true, msg, sendBack, "GET", client);
            } else {
                try {
                    DataOutputStream deleteError = new DataOutputStream(client.getOutputStream());
                    deleteError.writeUTF("No such key (" + msg + "" + "). GET is FAIL");
                } catch (Exception e) {
                    e.printStackTrace();
                }

                System.out.println("No such key");
                System.out.println("===========================================");
            }
        } else {
            logger.warning("message is null");
            System.out.println("message is null");
        }

        // close client
        try {
            client.close();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return new TcpResult(false, "N/A", "N/A", "FAIL", client);
    }


    public TcpResult tcpDelete(Socket client, String msg, Map<String, String> map) {
        logger.config("Invoke tcp PUT operation...");
        System.out.println("\nInvoke tcp DELETE operation...");
        // Message format should be
        // RequestType key
        // Example:
        // DELETE name
        if (!msg.equals("")) {
            if (map.containsKey(msg)) {
                map.remove(msg);
                return new TcpResult(true, msg, "", "DELETE", client);
            } else {
                try {
                    DataOutputStream deleteError = new DataOutputStream(client.getOutputStream());
                    deleteError.writeUTF("No such key (" + msg + "" + "). GET is FAIL");
                } catch (Exception e) {
                    e.printStackTrace();
                }

                System.out.println("No such key");
                System.out.println("===========================================");
            }
        } else {
            logger.warning("message is null");
            System.out.println("message is null");
        }

        // close client
        try {
            client.close();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return new TcpResult(false, "N/A", "N/A", "FAIL", client);
    }

}
